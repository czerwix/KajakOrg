package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.error.runRecoverCatching
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase
import com.mobeedev.kajakorg.ui.model.PathItem

class GetLocalPathDetailsUseCase(private val kayakPathRepository: KayakPathRepository) :
    UseCase<PathItem, GetLocalPathDetailsUseCase.Params>() {

    override suspend fun run(params: Params): Result<PathItem> = runRecoverCatching {
        return kayakPathRepository.getPathItemById(params.pathId)
    }

    data class Params(val pathId: Int)
}