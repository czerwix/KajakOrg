package com.mobeedev.kajakorg.data

import com.mobeedev.kajakorg.data.datasource.local.LocalPathSource
import com.mobeedev.kajakorg.data.datasource.remote.RemotePathSource
import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto
import com.mobeedev.kajakorg.domain.error.runRecoverCatching
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import org.jsoup.Jsoup

class KayakPathRepositoryImpl(
    private val remotePathSource: RemotePathSource,
    private val localPathSource: LocalPathSource
) : KayakPathRepository {

    override suspend fun loadAllAvailablePaths() = runRecoverCatching {
        remotePathSource.getPathsList()
    }.map {
        parsePathXML(it)
    }
//        .onSuccess {
//        localPathSource.savePathsOverview(it)
//    }

    private fun parsePathXML(response: String): String {
        val paths = mutableListOf<PathOverviewDto>()
        val webPage = Jsoup.parse(response)

        val pathTable = webPage.select("table")


        //todo parse xml response

        return "cosik"
    }
}