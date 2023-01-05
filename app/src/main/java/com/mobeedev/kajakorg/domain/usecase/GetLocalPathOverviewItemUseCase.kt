package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.NoParametersUseCase
import com.mobeedev.kajakorg.ui.model.PathOveriewItem

class GetLocalPathOverviewItemUseCase(private val kayakPathRepository: KayakPathRepository) :
    NoParametersUseCase<List<PathOveriewItem>>() {

    override suspend fun run(): Result<List<PathOveriewItem>> =
        kayakPathRepository.getPathsOverviewItem()
}