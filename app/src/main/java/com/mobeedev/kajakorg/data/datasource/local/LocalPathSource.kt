package com.mobeedev.kajakorg.data.datasource.local

import com.mobeedev.kajakorg.data.datasource.local.db.overview.toDto
import com.mobeedev.kajakorg.data.datasource.local.db.path.PathDB
import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.data.model.detail.toDB
import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto
import com.mobeedev.kajakorg.data.model.overview.toDB

class LocalPathSource(
    private val kajakPathDao: KajakPathDao
) {

    suspend fun savePathsOverview(paths: List<PathOverviewDto>) =
        kajakPathDao.insertAllPathsOverview(paths.map { it.toDB() })

    suspend fun savePaths(paths: List<PathDto>) = paths.forEach { path->
        kajakPathDao.insert(path)
    }

    suspend fun savePath(path: PathDto) = kajakPathDao.insert(path)

    suspend fun getPathsOverview(): List<PathOverviewDto> =
        kajakPathDao.getAllPathsOverview().map { it.toDto() }

    suspend fun getPath(pathId: Int): PathDto = kajakPathDao.getPathDto(pathId)
}