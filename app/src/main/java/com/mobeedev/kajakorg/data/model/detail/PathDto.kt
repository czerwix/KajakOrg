package com.mobeedev.kajakorg.data.model.detail

import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.data.datasource.db.path.PathDB
import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "szlak")
data class PathDto(
    @Attribute(name = "id")
    var id: Int = 0,
    @Attribute(name = "nazwa")
    var name: String = String.empty,
    @Attribute(name = "wersja")
    var versionCode: Int = -1,
    @PropertyElement(name = "opis")
    var description: String? = String.empty,


    //        typesByElement = {
//            @ElementNameMatcher(type = Author.class),
//                @ElementNameMatcher(type = Journalist.class)
//        }
//    )
    @Element(name = "odcinek")
    var sections: MutableList<SectionDto>? = mutableListOf(),
    @Element(name = "punkt")
    var events: MutableList<EventDto>? = mutableListOf()
)

fun PathDto.toDB(): PathDB = PathDB(
    pathId = id,
    name = name,
    versionCode = versionCode,
    description = description ?: String.empty
)