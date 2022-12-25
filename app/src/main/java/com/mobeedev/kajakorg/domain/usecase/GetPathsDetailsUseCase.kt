package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.NoParametersUseCase

class GetPathsDetailsUseCase(private val kayakPathRepository: KayakPathRepository) :
    NoParametersUseCase<List<PathDto>>() {

    override suspend fun run(): Result<List<PathDto>> = kayakPathRepository.getPathsDetails()
}