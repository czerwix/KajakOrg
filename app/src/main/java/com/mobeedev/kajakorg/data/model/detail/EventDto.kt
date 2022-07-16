package com.mobeedev.kajakorg.data.model.detail

import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.data.datasource.local.db.path.EventDB

data class EventDto(
    val id: Int,
    val townName: String,
    val name: String,
    val position: LatLng,
    val atKilometer: Double,
    val label: String,
    val sortOrder: Int,

    val eventDescription: MutableList<EventDescriptionDto> = mutableListOf()
)

fun EventDto.toDB(sectionId: Int): EventDB = EventDB(
    eventId = id,
    sectionId = sectionId,
    townName = townName,
    name = name,
    position = position,
    atKilometer = atKilometer,
    label = label,
    sortOrder = sortOrder
)