package com.mobeedev.kajakorg.data.datasource.local.db.overview

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto

@Entity
data class PathOverviewDB(
    @PrimaryKey
    val pathOverviewId: Int,

    val name: String,
    val versionCode: Int,
    val length: Double,
    val numberOfMarkers: Int,
    val numberOfSections: Int,
    val difficulty: String,
    val nuisance: String
)

fun PathOverviewDB.toDto() = PathOverviewDto(
    id = pathOverviewId,
    name,
    versionCode,
    length,
    numberOfMarkers,
    numberOfSections,
    difficulty,
    nuisance
)