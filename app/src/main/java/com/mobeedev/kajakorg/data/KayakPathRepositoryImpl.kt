package com.mobeedev.kajakorg.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.mobeedev.kajakorg.common.extensions.dataStore
import com.mobeedev.kajakorg.data.datasource.local.KajakOrgHtmlParser.parsePathHTML
import com.mobeedev.kajakorg.data.datasource.local.LocalPathSource
import com.mobeedev.kajakorg.data.datasource.local.PreferencesKeys
import com.mobeedev.kajakorg.data.datasource.remote.RemotePathSource
import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto
import com.mobeedev.kajakorg.domain.error.DataErrors
import com.mobeedev.kajakorg.domain.error.runRecoverCatching
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime

val ID_REGEX = Regex("[0-9]+")

class KayakPathRepositoryImpl(
    private val remotePathSource: RemotePathSource,
    private val localPathSource: LocalPathSource,
    private val context: Context
) : KayakPathRepository {

    override suspend fun loadAllAvailablePaths() = runRecoverCatching {
        remotePathSource.getPathsList()
    }.map {
        parsePathHTML(it)
    }.onSuccess {
        localPathSource.savePathsOverview(it)
    }

    override suspend fun loadAllPathsDetails(paths: List<Int>) = runRecoverCatching {
        val pathDetails = mutableListOf<PathDto>()

        paths.forEach { pathId ->
            //todo for now removing a badly formatted XML path with id 91 and 72...
            //todo in the future extend xmlParser library and allow badly formatted xml files.
            //or fix issues on website or add maybe google drive source with upodated values?
            if (pathId == 91 || pathId == 72) {
                Log.d(
                    "--Debug--",
                    "Path pathId: $pathId is excluded from the app due to badly formatted XML"
                )
            } else {
                Log.d("--Debug--", "started pathId: $pathId")

                val result = remotePathSource.getPath(pathId)
                pathDetails.add(result.pathDto)

                Log.d(
                    "--Debug--",
                    "parsed pathId: ${result.pathDto.id} name:${result.pathDto.name}"
                )
            }
        }

        pathDetails
    }.onSuccess {
        localPathSource.savePaths(it)
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.lastUpdateDate] = ZonedDateTime.now().toString()
        }
    }.map {
        it.size > 0//todo think of better solution to know if paths are loaded
    }

    override suspend fun getPathsOverviewDetails(): Result<List<PathOverviewDto>> =
        runRecoverCatching {
            localPathSource.getPathsOverview()
        }

    override suspend fun getPathsDetails(): Result<List<PathDto>> = runRecoverCatching {
        localPathSource.getPaths()
    }

    override suspend fun getLastUpdateDate(): Result<ZonedDateTime> = runRecoverCatching {
        var lastUpdateString: Flow<String> = context.dataStore.data.map { prefs ->
            prefs[PreferencesKeys.lastUpdateDate] ?: throw DataErrors.LastUpdateDateNotSet(
                "No Previous Data information set. Please load data from Kajak.org.pl first"
            )
        }
        ZonedDateTime.parse(lastUpdateString.first())
    }
}