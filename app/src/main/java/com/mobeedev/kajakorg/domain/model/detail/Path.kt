package com.mobeedev.kajakorg.domain.model.detail

import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.data.datasource.local.db.path.PathDB
import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.model.PathOveriewItem

data class Path(
    var id: Int = 0,
    var name: String = String.empty,
    var versionCode: Int = -1,
    var description: String = String.empty,


    var sections: List<Section> = mutableListOf(),
    var events: List<Event> = mutableListOf()
)

fun PathDB.toDomain(sections: List<Section>, events: List<Event>) = Path(
    id = pathId, name, versionCode, description, sections, events
)

fun PathDto.toDomain() = Path(
    id,
    name,
    versionCode,
    description ?: String.empty,
    sections?.map { it.toDomain() } ?: mutableListOf(),
    events?.map { it.toDomain() } ?: mutableListOf()
)

fun Path.toItem(overview: PathOveriewItem) = PathItem(
    overview = overview,
    sections = sections,
    events = events
)