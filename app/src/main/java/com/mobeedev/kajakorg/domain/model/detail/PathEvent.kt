package com.mobeedev.kajakorg.domain.model.detail

import com.mobeedev.kajakorg.ui.model.EventMapItem

interface PathEvent {
    var sortOrder: Int
}

fun List<PathEvent>.getSections(): List<Section?> = filterIsInstance<Section>()

fun List<PathEvent>.getEventMapItem(): List<EventMapItem> {
    val outputList = mutableListOf<EventMapItem>()
    forEach {
        when (it) {
            is Section -> {

                outputList.addAll(it.events.map { event ->
                    EventMapItem(
                        event.id,
                        event.position,
                        event.atKilometer,
                        event.label,
                        event.sortOrder
                    )
                })
            }

            is Event -> outputList.add(
                EventMapItem(
                    it.id,
                    it.position,
                    it.atKilometer,
                    it.label,
                    it.sortOrder
                )
            )
        }
    }
    return outputList
}

fun List<PathEvent>.flatPathMapSectionEventList(): List<PathEvent> {
    val outputList = mutableListOf<PathEvent>()
    forEach {
        when (it) {
            is Section -> {
                outputList.add(it)
                outputList.addAll(it.events)
            }

            is Event -> outputList.add(it)
        }
    }
    return outputList
}

fun PathEvent.eventId(): Int = when (this) {
    is Event -> id
    else -> -1
}

fun PathEvent.sectionId(): Int = when (this) {
    is Section -> id
    else -> -1
}
