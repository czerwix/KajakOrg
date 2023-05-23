package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.data.db.PathMapDetailScreenState
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase

class GetMapDetailsStateUseCase(
    private val kayakPathRepository: KayakPathRepository
) : UseCase<PathMapDetailScreenState, GetMapDetailsStateUseCase.Params>() {

    override suspend fun run(params: Params): Result<PathMapDetailScreenState> =
        kayakPathRepository.getMapDetailsState(params.pathId)

    data class Params(val pathId: Int)
}