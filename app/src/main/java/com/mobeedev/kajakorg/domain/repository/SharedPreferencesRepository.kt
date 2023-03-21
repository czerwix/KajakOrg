package com.mobeedev.kajakorg.domain.repository

import com.mobeedev.kajakorg.ui.model.GoogleMapsStatusItem

interface SharedPreferencesRepository {
    suspend fun getGoogleMapStatus(): Result<GoogleMapsStatusItem>
    suspend fun updateGoogleMapStatus(googleMapStatus: GoogleMapsStatusItem): Result<Unit>
}