package com.mobeedev.kajakorg.domain.repository

import com.google.android.gms.maps.model.CameraPosition
import com.mobeedev.kajakorg.ui.model.GoogleMapsStatusItem

interface SharedPreferencesRepository {
    suspend fun getGoogleMapStatus(): Result<GoogleMapsStatusItem>
    suspend fun updateGoogleMapStatus(googleMapStatus: GoogleMapsStatusItem): Result<Unit>
}