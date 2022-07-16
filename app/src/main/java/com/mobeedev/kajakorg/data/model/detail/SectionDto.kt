package com.mobeedev.kajakorg.data.model.detail

import com.google.android.gms.maps.model.LatLng
import com.mobeedev.kajakorg.data.datasource.local.db.path.SectionDB

data class SectionDto(
    val id: Int,
    val type: String,
    val name: String,
    val nuisance: String,
    val difficult: String,
    val picturesque: String,
    val cleanliness: String,
    val sortOrder: String,
    val description: String,

    val events: MutableList<EventDto> = mutableListOf()
)

fun SectionDto.toDB(pathId:Int): SectionDB = SectionDB(
    sectionId = id,
    pathId = pathId,
    type = type,
    name = name,
    nuisance = nuisance,
    difficult = difficult,
    picturesque = picturesque,
    cleanliness = cleanliness,
    sortOrder = sortOrder,
    description = description
)
