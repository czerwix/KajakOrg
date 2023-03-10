package com.mobeedev.kajakorg.domain.model.detail

interface PathEvent {
    var sortOrder: Int
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
