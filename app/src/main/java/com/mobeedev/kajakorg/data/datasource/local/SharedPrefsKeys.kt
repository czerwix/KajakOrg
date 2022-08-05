package com.mobeedev.kajakorg.data.datasource.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val lastUpdateDate = stringPreferencesKey("last_update_at")
}