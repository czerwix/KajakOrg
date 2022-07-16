package com.mobeedev.kajakorg.data.datasource.local.db.path

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.data.model.detail.EventDto

@Entity
data class EventDB(
    @PrimaryKey
    val eventId: Int = 0,
    val sectionId: Int,

    val townName: String,
    val name: String,
    val position: LatLng,
    val atKilometer: Double,
    val label: String,
    val sortOrder: Int,
)

fun EventDB.toDto(): EventDto = EventDto(
    id = eventId,
    townName = townName,
    name = name,
    position = position,
    atKilometer = atKilometer,
    label = label,
    sortOrder = sortOrder
)