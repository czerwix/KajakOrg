package com.mobeedev.kajakorg.data.model.detail

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
    var type: String = "",
    @Attribute(name = "nazwa")
    var name: String = "",
    @Attribute(name = "uciazliwosc")
    var nuisance: String = "",
    @Attribute(name = "trudnosc")
    var difficult: String = "",
    @Attribute(name = "malowniczosc")
    var picturesque: String = "",
    @Attribute(name = "czystosc")
    var cleanliness: String = "",
    @Attribute(name = "kolejnosc")
    var sortOrder: String = "",
    @PropertyElement(name = "opis")
    var description: String = "",

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
