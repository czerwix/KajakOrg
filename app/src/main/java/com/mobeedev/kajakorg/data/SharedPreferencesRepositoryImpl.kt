package com.mobeedev.kajakorg.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.mobeedev.kajakorg.common.extensions.dataStore
import com.mobeedev.kajakorg.common.extensions.isNotNull
import com.mobeedev.kajakorg.data.datasource.local.PreferencesKeys
import com.mobeedev.kajakorg.domain.error.runRecoverCatching
import com.mobeedev.kajakorg.domain.repository.SharedPreferencesRepository
import com.mobeedev.kajakorg.ui.model.GoogleMapsStatusItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SharedPreferencesRepositoryImpl(private val context: Context) : SharedPreferencesRepository {
    override suspend fun getGoogleMapStatus(): Result<GoogleMapsStatusItem> =
        runRecoverCatching {
            context.dataStore.data.map { prefs ->
                val savedValue: String? = prefs[PreferencesKeys.googleMapStatus]
                if (savedValue.isNotNull()) {
                    GoogleMapsStatusItem.valueOf(savedValue!!)
                } else {
                    GoogleMapsStatusItem.EnableMap
                }
            }.first()
        }

    override suspend fun updateGoogleMapStatus(googleMapStatus: GoogleMapsStatusItem) =
        runRecoverCatching {
            context.dataStore.edit { prefs ->
                prefs[PreferencesKeys.googleMapStatus] = googleMapStatus.toString()
            }
            Unit
        }
}