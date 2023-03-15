package com.mobeedev.kajakorg.domain.model.detail

interface PathEvent {
    var sortOrder: Int
}

fun List<PathEvent>.getSections(): List<Section?> = filterIsInstance<Section>()

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
