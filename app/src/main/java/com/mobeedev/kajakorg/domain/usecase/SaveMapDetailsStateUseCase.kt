package com.mobeedev.kajakorg.domain.usecase

import com.google.android.gms.maps.model.CameraPosition
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase

class SaveMapDetailsStateUseCase(
    private val kayakPathRepository: KayakPathRepository
) : UseCase<Unit, SaveMapDetailsStateUseCase.Params>() {

    override suspend fun run(params: Params): Result<Unit> =
        kayakPathRepository.saveMapDetailsState(
            params.position,
            params.currentPage,
            params.bottomLayoutVisibility,
            params.pathId
        )

    data class Params(
        val position: CameraPosition,
        val currentPage: Int,
        val bottomLayoutVisibility: Boolean,
        val pathId: Int
    )
}