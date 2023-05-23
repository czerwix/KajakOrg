package com.mobeedev.kajakorg.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng

@Entity
data class PathMapDetailScreenState(
    @PrimaryKey
    val pathId: Int,
    val cameraPositionLat: Double?,
    val cameraPositionLng: Double?,
    val cameraPositionZoom: Float?,
    val cameraPositionTilt: Float?,
    val cameraPositionBearing: Float?,
    val currentPage: Int?,
    val bottomLayoutVisibility: Boolean?
) {

    fun getCameraPosition() = CameraPosition(
        LatLng(
            cameraPositionLat ?: 0.0,
            cameraPositionLng ?: 0.0
        ),
        cameraPositionZoom ?: 0f,
        cameraPositionTilt ?: 0f,
        cameraPositionBearing ?: 0f
    )

}