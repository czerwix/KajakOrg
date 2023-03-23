package com.mobeedev.kajakorg.data.db.path

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobeedev.kajakorg.data.model.detail.SectionDto

@Entity
data class SectionDB(
    @PrimaryKey
    val sectionId: Int = 0,
    val pathId: Int,

    val type: String,
    val name: String,
    val nuisance: String,
    val difficult: String,
    val picturesque: String,
    val cleanliness: String,
    val sortOrder: String,
    val description: String,
)

fun SectionDB.toDto(): SectionDto = SectionDto(
    id = sectionId,
    type = type,
    name = name,
    nuisance = nuisance,
    difficult = difficult,
    picturesque = picturesque,
    cleanliness = cleanliness,
    sortOrder = sortOrder,
    description = description
)