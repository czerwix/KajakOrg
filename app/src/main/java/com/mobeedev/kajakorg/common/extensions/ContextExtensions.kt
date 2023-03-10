package com.mobeedev.kajakorg.common.extensions

import android.content.Context
import android.util.TypedValue
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(
    name = "KajakDataStoreSharedPrefs"
)

fun Context.dpToPx(dp: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp,
    this.resources.displayMetrics
)