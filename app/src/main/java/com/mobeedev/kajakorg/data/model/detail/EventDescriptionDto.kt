package com.mobeedev.kajakorg.data.model.detail

import com.mobeedev.kajakorg.data.datasource.local.db.path.EventDescriptionDB

data class EventDescriptionDto(
    val type: EventType,
    val sortOrder: Int,
    val description: String
)

fun EventDescriptionDto.toDB(eventId: Int) = EventDescriptionDB(
    eventId = eventId,
    type = type,
    sortOrder = sortOrder,
    description = description
)