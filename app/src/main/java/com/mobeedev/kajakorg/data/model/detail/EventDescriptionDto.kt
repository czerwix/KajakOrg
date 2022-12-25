package com.mobeedev.kajakorg.data.model.detail

import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.data.datasource.local.db.path.EventDescriptionDB
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "opispunktu")
data class EventDescriptionDto(
    @Attribute(name = "typ")
    val eventType: String = String.empty,

    @Attribute(name = "kolejnosc")
    val sortOrder: String = "-1",

    @TextContent
    val description: String? = String.empty
)

fun EventDescriptionDto.toDB(eventId: Int) = EventDescriptionDB(
    eventId = eventId,
    type = EventType.fromValue(eventType),
    sortOrder = if (sortOrder.isEmpty()) {
        -1
    } else {
        sortOrder.toInt()
    },
    description = description ?: String.empty
)