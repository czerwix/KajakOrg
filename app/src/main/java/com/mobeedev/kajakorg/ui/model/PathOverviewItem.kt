package com.mobeedev.kajakorg.ui.model

import com.mobeedev.kajakorg.domain.model.overview.PathOverview

data class PathOverviewItem(
    val id: Int,

    val name: String,
    val versionCode: Int,
    val length: Double,
    val numberOfMarkers: Int,
    val numberOfSections: Int,
    val difficulty: String,
    val nuisance: String,
    val description: String)

fun PathOverview.toItem(description: String) = PathOverviewItem(
    id,
    name,
    versionCode,
    length,
    numberOfMarkers,
    numberOfSections,
    difficulty,
    nuisance,
    description
)