package com.mobeedev.kajakorg.domain.model.detail

import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.data.db.path.EventDescriptionDB
import com.mobeedev.kajakorg.data.model.detail.EventDescriptionDto
import com.mobeedev.kajakorg.data.model.detail.EventType

data class EventDescription(
    val eventType: EventType,
    val sortOrder: Int,
    val description: String,
)

fun EventDescriptionDto.toDomain() = EventDescription(
    eventType = EventType.fromValue(eventType),
    sortOrder = if (sortOrder.isEmpty()) {
        -1
    } else {
        sortOrder.toInt()
    }, description = description ?: String.empty
)

fun EventDescription.toData() = EventDescriptionDto(
    eventType.toString(), sortOrder.toString(), description
)

fun EventDescriptionDB.toDomain() = EventDescription(
    eventType = type, sortOrder, description
)