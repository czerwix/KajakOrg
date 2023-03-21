package com.mobeedev.kajakorg.data.datasource.db.path

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.data.model.detail.EventDto

@Entity
data class EventDB(
    @PrimaryKey
    val eventId: Int = 0,
    val pathId: Int = -1,
    val sectionId: Int = -1,

    val townName: String,
    val position: LatLng,
    val atKilometer: Double,
    val label: String,
    val sortOrder: Int,
)

fun EventDB.toDto(): EventDto = EventDto(
    id = eventId,
    townName = townName,
    lat = position.latitude.toString(),
    lng = position.longitude.toString(),
    atKilometer = atKilometer.toString(),
    label = label,
    sortOrder = sortOrder.toString()
)