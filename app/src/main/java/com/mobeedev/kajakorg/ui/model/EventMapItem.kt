package com.mobeedev.kajakorg.ui.model

import com.google.android.gms.maps.model.LatLng

data class EventMapItem(
    var id: Int,
    var position: LatLng,
    var atKilometer: Double,
    var label: String,
    var sortOrder: Int
)
