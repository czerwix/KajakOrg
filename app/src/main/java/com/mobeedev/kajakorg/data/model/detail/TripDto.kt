package com.mobeedev.kajakorg.data.model.detail

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "splywww")
data class TripDto(
    @Element(name = "szlak")
    var pathDto: PathDto = PathDto(),

    @Attribute(name = "wersja")
    var version: Int = -1
)
