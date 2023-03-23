package com.mobeedev.kajakorg.data.db.path

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobeedev.kajakorg.data.model.detail.PathDto

@Entity
data class PathDB(
    @PrimaryKey
    val pathId: Int= 0,

    val name: String,
    val versionCode: Int,
    val description: String,
)

fun PathDB.toDto(): PathDto = PathDto(
    id = pathId,
    name = name,
    versionCode = versionCode,
    description=description
)