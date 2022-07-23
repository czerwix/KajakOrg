package com.mobeedev.kajakorg.data

import android.util.Log
import com.mobeedev.kajakorg.data.datasource.local.LocalPathSource
import com.mobeedev.kajakorg.data.datasource.remote.RemotePathSource
import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto
import com.mobeedev.kajakorg.domain.error.runRecoverCatching
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import org.jsoup.nodes.Document

val ID_REGEX = Regex("[0-9]+")

class KayakPathRepositoryImpl(
    private val remotePathSource: RemotePathSource,
    private val localPathSource: LocalPathSource
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
            //todo for nmow removing a badly formatted XML path with id 91 and 72... todo in the future extend xmlParser library and allow badly formatted xml files.
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
        localPathSource.savePaths(pathDetails)
        pathDetails
    }.map {
        it.size > 0//todo think of better solution to know if paths are loaded
    }

    //todo extract to dedicated parser
    private fun parsePathHTML(response: Document): List<PathOverviewDto> {
        val pathsRawData =
            response.select("table").select("tr").map { it.toString() }.toMutableList()
                .apply { removeFirst() }
        val pathsParsed = mutableListOf<PathOverviewDto>()

        pathsRawData.forEach { pathraw: String ->
            val wipItem = pathraw.replace("<tr>", "")
                .replace("<td>", "")
                .replace("</tr>", "")
                .split("</td>")
                .map { it.replace("//n", "").trim() }

            pathsParsed.add(
                PathOverviewDto(
                    ID_REGEX.find(wipItem[7])?.value?.toInt()
                        ?: error("Path can not have null ID."),
                    wipItem[0],//todo extract to const
                    wipItem[1].toInt(),
                    wipItem[2].toDouble(),
                    wipItem[3].toInt(),
                    wipItem[4].toInt(),
                    wipItem[5],
                    wipItem[6]
                )
            )
        }


        return pathsParsed
    }
}