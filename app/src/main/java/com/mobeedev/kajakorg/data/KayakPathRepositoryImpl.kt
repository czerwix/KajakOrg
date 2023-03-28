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
import com.mobeedev.kajakorg.domain.error.runRecoverCatching
import com.mobeedev.kajakorg.domain.model.DataDownloadState
import com.mobeedev.kajakorg.domain.model.DataDownloadStatus
import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.model.detail.toDomain
import com.mobeedev.kajakorg.domain.model.detail.toItem
import com.mobeedev.kajakorg.domain.model.overview.PathOverview
import com.mobeedev.kajakorg.domain.model.overview.toDomain
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.model.PathMapItem
import com.mobeedev.kajakorg.ui.model.PathOveriewItem
import kotlinx.coroutines.flow.MutableStateFlow
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
    }.map { listOfPaths -> listOfPaths.map { it.toDomain() } }

    override suspend fun loadAllPathsDetails(
        paths: List<Int>,
        workStatusFlow: MutableStateFlow<Int?>
    ) = runRecoverCatching {
        val pathDetails = mutableListOf<PathDto>()

        paths.forEach { pathId ->
            Log.d("--Debug--", "started pathId: $pathId")
            val result = remotePathSource.getPath(pathId)
            pathDetails.add(result.pathDto)
            Log.d(
                "--Debug--",
                "parsed pathId: ${result.pathDto.id} name:${result.pathDto.name}"
            )
            workStatusFlow.emit(pathDetails.size)
        }

        pathDetails
    }.onSuccess {
        localPathSource.savePaths(it)
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.lastUpdateDate] = ZonedDateTime.now().toString()
            prefs[PreferencesKeys.dataDownloadState] = DataDownloadState.DONE.toString()
        }
        workStatusFlow.emit(Int.MAX_VALUE)
    }.map { pathList -> pathList.map { it.toDomain() } }

    override suspend fun getPathsOverviewDetails(): Result<List<PathOverview>> =
        runRecoverCatching {
            localPathSource.getPathsOverview()
        }

    override suspend fun getPathsDetails(): Result<List<Path>> = runRecoverCatching {
        localPathSource.getPaths()
    }

    override suspend fun getDataDownloadStatus(): Result<DataDownloadStatus> = runRecoverCatching {
        var lastUpdateAt: String? = null
        var dataDownloadState: String? = null
        context.dataStore.data.map { prefs ->
            lastUpdateAt = prefs[PreferencesKeys.lastUpdateDate]
            dataDownloadState = prefs[PreferencesKeys.dataDownloadState]
        }

        when (dataDownloadState) {
            null -> DataDownloadStatus()
            DataDownloadState.PARTIAL.toString() ->
                DataDownloadStatus(status = DataDownloadState.PARTIAL)

            else -> {
                DataDownloadStatus(ZonedDateTime.parse(lastUpdateAt), DataDownloadState.DONE)
            }
        }
    }

    override suspend fun getPathsOverviewItem(): Result<List<PathOveriewItem>> =
        runRecoverCatching {
            localPathSource.getPathsOverviewItem()
        }

    override suspend fun getPathItemById(pathId: Int): Result<PathItem> = runRecoverCatching {
        val pathOverviewItem = localPathSource.getPathOverviewItem(pathId)
        localPathSource.getPath(pathId)
            .toItem(pathOverviewItem)
    }

    override suspend fun getPathMap(pathId: Int?): Result<List<PathMapItem>> = runRecoverCatching {
        if (pathId == null) {
            localPathSource.getAllPathMapData()
        } else {
            listOf(localPathSource.getPathMapData(pathId))
        }
    }
}