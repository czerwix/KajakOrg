package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.NoParametersUseCase
import com.mobeedev.kajakorg.ui.model.PathOverviewItem

class GetLocalPathOverviewItemUseCase(private val kayakPathRepository: KayakPathRepository) :
    NoParametersUseCase<List<PathOverviewItem>>() {

    override suspend fun run(): Result<List<PathOverviewItem>> =
        kayakPathRepository.getPathsOverviewItem()
}