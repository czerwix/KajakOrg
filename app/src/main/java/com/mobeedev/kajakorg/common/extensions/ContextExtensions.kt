package com.mobeedev.kajakorg.common.extensions

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(
    name = "KajakDataStoreSharedPrefs"
)