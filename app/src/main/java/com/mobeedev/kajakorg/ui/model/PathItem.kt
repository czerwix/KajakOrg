package com.mobeedev.kajakorg.ui.model

import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.PathEvent
import com.mobeedev.kajakorg.domain.model.detail.Section
import com.mobeedev.kajakorg.domain.model.detail.getEventMapItem

data class PathItem(
    val overview: PathOverviewItem,
    var pathSectionsEvents: List<PathEvent>
)

fun List<PathEvent>.toPathEventsList(): List<Event> =
    fold(mutableListOf<Event>()) { acc, pathEvent ->
        when (pathEvent) {
            is Section -> acc.addAll(pathEvent.events)
            is Event -> acc.add(pathEvent)
        }
        acc
    }.toList()

fun PathItem.toMapItem() =
    PathMapItem(overview = overview, eventList = pathSectionsEvents.getEventMapItem())