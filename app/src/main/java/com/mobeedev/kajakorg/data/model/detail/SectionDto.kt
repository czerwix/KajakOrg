package com.mobeedev.kajakorg.data.model.detail

import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.data.datasource.local.db.path.SectionDB
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "odcinek")
data class SectionDto (
    @Attribute(name = "id")
    var id: Int = 0,
    @Attribute(name = "typ")
    var type: String = String.empty,
    @Attribute(name = "nazwa")
    var name: String = String.empty,
    @Attribute(name = "uciazliwosc")
    var nuisance: String = String.empty,
    @Attribute(name = "trudnosc")
    var difficult: String = String.empty,
    @Attribute(name = "malowniczosc")
    var picturesque: String = String.empty,
    @Attribute(name = "czystosc")
    var cleanliness: String = String.empty,
    @Attribute(name = "kolejnosc")
    var sortOrder: String = String.empty,
    @PropertyElement(name = "opis")
    var description: String = String.empty,

    @Element(name = "punkt")
    var events: MutableList<EventDto> = mutableListOf()
)

fun SectionDto.toDB(pathId: Int): SectionDB = SectionDB(
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
