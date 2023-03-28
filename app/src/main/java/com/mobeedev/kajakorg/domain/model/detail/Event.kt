package com.mobeedev.kajakorg.domain.model.detail

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.data.db.path.EventDB
import com.mobeedev.kajakorg.data.model.detail.EventDto

data class Event(
    var id: Int,
    var townName: String,
    var position: LatLng,
    var atKilometer: Double,
    var label: String,
    override var sortOrder: Int,

    var eventDescription: List<EventDescription> = mutableListOf()
) : PathEvent

fun Event.isLocationNonZero() = (position.latitude > 0 && position.longitude > 0)

fun EventDB.toDomain(eventDescription: List<EventDescription>) = Event(
    eventId,
    townName,
    position,
    atKilometer,
    label,
    sortOrder,
    eventDescription
)

fun EventDto.toDomain() = Event(
    id,
    townName,
    position = LatLng(lat.toDouble(), lng.toDouble()),
    if (atKilometer.isNullOrEmpty()) {
        Log.w("cosik", "error with atKilometer for $this")
        0.0
    } else atKilometer.toDouble(),
    label,
    if (sortOrder.isNullOrEmpty()) {
        Log.w("cosik", "error with sortOrder for $this")
        -1
    } else sortOrder.toInt(),
    eventDescription.map { it.toDomain() })