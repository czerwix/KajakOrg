package com.mobeedev.kajakorg.data.model.overview

import com.mobeedev.kajakorg.data.datasource.db.overview.PathOverviewDB

data class PathOverviewDto(
    val id: Int,

    val name: String,
    val versionCode: Int,
    val length: Double,
    val numberOfMarkers: Int,
    val numberOfSections: Int,
    val difficulty: String,
    val nuisance: String,
)

fun PathOverviewDto.toDB() = PathOverviewDB(
    pathOverviewId = id,
    name,
    versionCode,
    length,
    numberOfMarkers,
    numberOfSections,
    difficulty,
    nuisance
)