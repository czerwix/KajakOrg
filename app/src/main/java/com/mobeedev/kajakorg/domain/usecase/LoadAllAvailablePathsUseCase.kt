package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.NoParametersUseCase

class LoadAllAvailablePathsUseCase(private val kayakPathRepository: KayakPathRepository) :
    NoParametersUseCase<List<PathOverviewDto>>() {

    override suspend fun run(): Result<List<PathOverviewDto>> = kayakPathRepository.loadAllAvailablePaths()
}