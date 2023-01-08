package com.mobeedev.kajakorg.domain.repository

import com.mobeedev.kajakorg.domain.model.DataDownloadStatus
import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.model.overview.PathOverview
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.model.PathOveriewItem
import kotlinx.coroutines.flow.MutableStateFlow

interface KayakPathRepository {
    //change return type to domain models.
    suspend fun loadAllAvailablePaths(): Result<List<PathOverview>>

    suspend fun loadAllPathsDetails(
        paths: List<Int>,
        workStatusFlow: MutableStateFlow<Int?>
    ): Result<List<Path>>

    //change return type to domain models.
    suspend fun getPathsOverviewDetails(): Result<List<PathOverview>>

    suspend fun getPathsDetails(): Result<List<Path>>

    suspend fun getDataDownloadStatus(): Result<DataDownloadStatus>

    suspend fun getPathsOverviewItem(): Result<List<PathOveriewItem>>

    suspend fun getPathItemById(pathId: Int): Result<PathItem>
}