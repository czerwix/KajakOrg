package com.mobeedev.kajakorg.domain.repository

import com.google.android.gms.maps.model.CameraPosition
import com.mobeedev.kajakorg.data.db.PathMapDetailScreenState
import com.mobeedev.kajakorg.domain.model.DataDownloadStatus
import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.model.overview.PathOverview
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.model.PathMapItem
import com.mobeedev.kajakorg.ui.model.PathOverviewItem
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

    suspend fun getPathsOverviewItem(): Result<List<PathOverviewItem>>

    suspend fun getPathItemById(pathId: Int): Result<PathItem>

    suspend fun getPathMap(pathId: Int?): Result<List<PathMapItem>>

    suspend fun saveMapDetailsState(
        position: CameraPosition,
        currentPage: Int,
        bottomLayoutVisibility: Boolean,
        pathId: Int
    ): Result<Unit>

    suspend fun getMapDetailsState(pathId: Int): Result<PathMapDetailScreenState>
}