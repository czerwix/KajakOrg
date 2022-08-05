package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase

class LoadAllPathsDetailsUseCase(private val kayakPathRepository: KayakPathRepository) :
    UseCase<Boolean, LoadAllPathsDetailsUseCase.Params>() {

    override suspend fun run(params: Params): Result<Boolean> =
        kayakPathRepository.loadAllPathsDetails(params.pathIds)

    data class Params(val pathIds: List<Int>)
}