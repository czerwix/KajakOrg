package com.mobeedev.kajakorg.data.model.detail

import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.data.db.path.EventDB
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "punkt")
data class EventDto(
    @Attribute(name = "id")
    var id: Int = 0,
    @Attribute(name = "miejscowosc")
    var townName: String = String.empty,
    @Attribute(name = "ns")
    var lat: String = String.empty,
    @Attribute(name = "we")
    var lng: String = String.empty,
    @Attribute(name = "km")
    var atKilometer: String = String.empty,
    @Attribute(name = "etykieta")
    var label: String = String.empty,
    @Attribute(name = "kolejnosc")
    var sortOrder: String = "-1",

    @Element(name = "opispunktu")
    var eventDescription: MutableList<EventDescriptionDto> = mutableListOf()
)

fun EventDto.toSectionDB(sectionId: Int): EventDB = EventDB(
    eventId = id,
    sectionId = sectionId,
    townName = townName,
    position = LatLng(lat.toDouble(), lng.toDouble()),
    atKilometer = if (atKilometer.isNullOrEmpty()) {
        0.0
    } else {
        atKilometer.toDouble()
    },
    label = label,
    sortOrder = if (sortOrder.isEmpty()) {
        -1
    } else {
        sortOrder.toInt()
    },
)

fun EventDto.toPathDB(pathId: Int): EventDB = EventDB(
    eventId = id,
    pathId = pathId,
    townName = townName,
    position = LatLng(lat.toDouble(), lng.toDouble()),
    atKilometer = if (atKilometer.isNullOrEmpty()) {
        0.0
    } else {
        atKilometer.toDouble()
    },
    label = label,
    sortOrder = if (sortOrder.isEmpty()) {
        -1
    } else {
        sortOrder.toInt()
    },
)