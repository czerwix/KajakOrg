package com.mobeedev.kajakorg.domain.repository

import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto

interface KayakPathRepository {

    suspend fun loadAllAvailablePaths(): Result<List<PathOverviewDto>>

    suspend fun loadAllPathsDetails(paths:List<Int>): Result<Boolean>
}