package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.error.runRecoverCatching
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.model.PathMapItem

class GetLocalMapPathsUseCase (private val kayakPathRepository: KayakPathRepository) :
    UseCase<List<PathMapItem>, GetLocalMapPathsUseCase.Params>() {

    override suspend fun run(params: Params): Result<List<PathMapItem>> =
         kayakPathRepository.getPathMap(params.pathId)

    data class Params(val pathId: Int? = null)
}