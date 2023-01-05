package com.mobeedev.kajakorg.ui.model

import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.domain.model.overview.PathOverview

data class PathOveriewItem(
    val id: Int,

    val name: String,
    val versionCode: Int,
    val length: Double,
    val numberOfMarkers: Int,
    val numberOfSections: Int,
    val difficulty: String,
    val nuisance: String,
    val description: String,
    var pictureID: String = String.empty
)

fun PathOverview.toItem(description: String) = PathOveriewItem(
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