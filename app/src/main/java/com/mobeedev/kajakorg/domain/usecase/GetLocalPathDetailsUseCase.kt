package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.data.db.PathMapDetailScreenState
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase
import com.mobeedev.kajakorg.ui.model.PathItem

class GetLocalPathDetailsUseCase(
    private val kayakPathRepository: KayakPathRepository,
    private val getMapDetailsStateUseCase: GetMapDetailsStateUseCase
) :
    UseCase<Pair<PathItem, PathMapDetailScreenState?>, GetLocalPathDetailsUseCase.Params>() {

    override suspend fun run(params: Params): Result<Pair<PathItem, PathMapDetailScreenState?>> {//make pretty when i have time
        val state = getMapDetailsStateUseCase.run(GetMapDetailsStateUseCase.Params(params.pathId))

        return Result.success(
            kayakPathRepository.getPathItemById(params.pathId).getOrThrow() to state.getOrNull()
        )
    }

    data class Params(val pathId: Int)
}