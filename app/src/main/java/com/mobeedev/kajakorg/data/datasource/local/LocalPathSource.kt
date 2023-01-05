package com.mobeedev.kajakorg.data.datasource.local

import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto
import com.mobeedev.kajakorg.data.model.overview.toDB
import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.model.overview.PathOverview
import com.mobeedev.kajakorg.domain.model.overview.toDomain
import com.mobeedev.kajakorg.ui.model.PathOveriewItem

class LocalPathSource(
    private val kajakPathDao: KajakPathDao
) {

    suspend fun savePathsOverview(paths: List<PathOverviewDto>) =
        kajakPathDao.insertAllPathsOverview(paths.map { it.toDB() })

    suspend fun savePaths(paths: List<PathDto>) = paths.forEach { path ->
        kajakPathDao.insert(path)
    }

    suspend fun savePath(path: PathDto) = kajakPathDao.insert(path)

    suspend fun getPathsOverview(): List<PathOverview> =
        kajakPathDao.getAllPathsOverview().map { it.toDomain() }

    suspend fun getPathsOverviewItem(): List<PathOveriewItem> =
        kajakPathDao.getPathsOverviewItem()

    suspend fun getPaths(): List<Path> = kajakPathDao.getAllPathsIds().map {
        getPath(it)
    }

    suspend fun getPath(pathId: Int): Path = kajakPathDao.getPathDomain(pathId)

}