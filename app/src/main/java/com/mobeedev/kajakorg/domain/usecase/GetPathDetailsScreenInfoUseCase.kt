package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.error.runRecoverCatching
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.repository.SharedPreferencesRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase
import com.mobeedev.kajakorg.ui.model.PathDetailsInfo

class GetPathDetailsScreenInfoUseCase(
    private val kayakPathRepository: KayakPathRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : UseCase<PathDetailsInfo, GetLocalPathDetailsUseCase.Params>() {

    override suspend fun run(params: GetLocalPathDetailsUseCase.Params): Result<PathDetailsInfo> =
        runRecoverCatching {
            val pathData = kayakPathRepository.getPathItemById(params.pathId)
            val googleMapStatus = sharedPreferencesRepository.getGoogleMapStatus()

            PathDetailsInfo(
                path = pathData.getOrThrow(),
                googleMapStatus = googleMapStatus.getOrThrow()
            )
        }

    data class Params(val pathId: Int)
}