package com.mobeedev.kajakorg.domain.repository

import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto
import java.time.ZonedDateTime

interface KayakPathRepository {
    //change return type to domain models.
    suspend fun loadAllAvailablePaths(): Result<List<PathOverviewDto>>

    suspend fun loadAllPathsDetails(paths: List<Int>): Result<Boolean>

    //change return type to domain models.
    suspend fun getPathsOverviewDetails(): Result<List<PathOverviewDto>>

    suspend fun getPathsDetails(): Result<List<PathDto>>

    suspend fun getLastUpdateDate(): Result<ZonedDateTime>
}