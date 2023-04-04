package com.mobeedev.kajakorg.ui.model

import com.google.android.gms.maps.model.LatLngBounds

data class PathMapItem(
    var overview: PathOverviewItem,
    var eventList: List<EventMapItem>
)

fun PathMapItem.getNonZeroEvents(): List<EventMapItem> =
    eventList.filterNot { it.position.latitude == 0.0 || it.position.longitude == 0.0 }

fun PathMapItem.getBounds(): LatLngBounds {
    val builder = LatLngBounds.Builder()
    eventList.forEach {
        builder.include(it.position)
    }
    return builder.build()
}