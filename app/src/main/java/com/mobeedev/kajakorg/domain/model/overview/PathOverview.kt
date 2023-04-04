package com.mobeedev.kajakorg.domain.model.overview

import com.mobeedev.kajakorg.data.db.overview.PathOverviewDB
import com.mobeedev.kajakorg.data.model.overview.PathOverviewDto
import com.mobeedev.kajakorg.ui.model.PathOverviewItem


data class PathOverview(
    val id: Int,

    val name: String,
    val versionCode: Int,
    val length: Double,
    val numberOfMarkers: Int,
    val numberOfSections: Int,
    val difficulty: String,
    val nuisance: String,
)

fun PathOverview.toData() = PathOverviewDto(
    id,
    name,
    versionCode,
    length,
    numberOfMarkers,
    numberOfSections,
    difficulty,
    nuisance
)

fun PathOverviewDto.toDomain() = PathOverview(
    id,
    name,
    versionCode,
    length,
    numberOfMarkers,
    numberOfSections,
    difficulty,
    nuisance
)

fun PathOverview.toDB() = PathOverviewDB(
    id,
    name,
    versionCode,
    length,
    numberOfMarkers,
    numberOfSections,
    difficulty,
    nuisance
)

fun PathOverviewDB.toDomain() = PathOverview(
    pathOverviewId,
    name,
    versionCode,
    length,
    numberOfMarkers,
    numberOfSections,
    difficulty,
    nuisance
)

fun PathOverviewDB.toItem(description: String) = PathOverviewItem(
    pathOverviewId,
    name,
    versionCode,
    length,
    numberOfMarkers,
    numberOfSections,
    difficulty,
    nuisance,
    description
)