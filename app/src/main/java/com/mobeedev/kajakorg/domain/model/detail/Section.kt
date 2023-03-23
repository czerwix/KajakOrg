package com.mobeedev.kajakorg.domain.model.detail

import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.data.db.path.SectionDB
import com.mobeedev.kajakorg.data.model.detail.SectionDto

data class Section(
    var id: Int = 0,
    var type: String = String.empty,
    var name: String = String.empty,
    var nuisance: String = String.empty,
    var difficulty: String = String.empty,
    var picturesque: String = String.empty,
    var cleanliness: String = String.empty,
    override var sortOrder: Int = -1,
    var description: String = String.empty,

    var events: List<Event> = mutableListOf()
) : PathEvent

fun SectionDB.toDomain(events: List<Event>) = Section(
    id = sectionId,
    type = type,
    name = name,
    nuisance = nuisance,
    difficulty = difficult,
    picturesque = picturesque,
    cleanliness = cleanliness,
    sortOrder = sortOrder.toInt(),
    description = description,
    events = events
)

fun SectionDto.toDomain() =
    Section(
        id,
        type,
        name,
        nuisance,
        difficult,
        picturesque,
        cleanliness,
        sortOrder.toInt(),
        description,
        events.map { it.toDomain() })
