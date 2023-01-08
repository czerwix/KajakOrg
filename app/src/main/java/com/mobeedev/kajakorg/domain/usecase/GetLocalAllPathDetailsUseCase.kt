package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.NoParametersUseCase

class GetLocalAllPathDetailsUseCase(private val kayakPathRepository: KayakPathRepository) :
    NoParametersUseCase<List<Path>>() {

    override suspend fun run(): Result<List<Path>> =
        kayakPathRepository.getPathsDetails()
}