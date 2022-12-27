package com.mobeedev.kajakorg.domain.model.detail

import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.data.datasource.local.db.path.EventDB
import com.mobeedev.kajakorg.data.model.detail.EventDto

data class Event(
    var id: Int,
    val townName: String,
    val position: LatLng,
    val atKilometer: Double,
    val label: String,
    val sortOrder: Int,

    var eventDescription: List<EventDescription> = mutableListOf()
)

fun EventDB.toDomain(eventDescription: List<EventDescription>) = Event(
    eventId,
    townName,
    position,
    atKilometer, label, sortOrder
)

fun EventDto.toDomain() = Event(
    id,
    townName,
    position = LatLng(lat.toDouble(), lng.toDouble()),
    atKilometer.toDouble(),
    label,
    sortOrder.toInt(),
    eventDescription.map { it.toDomain() })