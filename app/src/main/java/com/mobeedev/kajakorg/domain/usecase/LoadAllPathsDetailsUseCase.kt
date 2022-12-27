package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.WorkStatusExposedUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class LoadAllPathsDetailsUseCase(private val kayakPathRepository: KayakPathRepository) :
    WorkStatusExposedUseCase<List<Path>, LoadAllPathsDetailsUseCase.Params, Int>() {

    data class Params(val pathIds: List<Int>)

    override suspend fun run(
        params: Params,
        workStatusFlow: MutableStateFlow<Int?>
    ): Result<List<Path>> = kayakPathRepository.loadAllPathsDetails(params.pathIds, workStatusFlow)

}
