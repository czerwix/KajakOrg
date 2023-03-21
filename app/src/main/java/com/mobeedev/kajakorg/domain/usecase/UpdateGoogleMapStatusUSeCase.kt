package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.repository.SharedPreferencesRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase
import com.mobeedev.kajakorg.ui.model.GoogleMapsStatusItem

class UpdateGoogleMapStatusUSeCase(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : UseCase<Unit, UpdateGoogleMapStatusUSeCase.Params>() {

    override suspend fun run(params: Params): Result<Unit> =
        sharedPreferencesRepository.updateGoogleMapStatus(params.googleMapStatus)

    data class Params(val googleMapStatus: GoogleMapsStatusItem)
}