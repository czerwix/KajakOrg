package com.mobeedev.kajakorg.data.model.detail

import com.mobeedev.kajakorg.data.datasource.local.db.path.PathDB

data class PathDto(
    val id:Int,
    val name:String,
    val versionCode: Int,
    val description:String,
    val sections:MutableList<SectionDto> = mutableListOf()
)

fun PathDto.toDB():PathDB = PathDB(
    pathId = id,
    name = name,
    versionCode = versionCode,
    description=description
)