package com.mobeedev.kajakorg.ui.path.map

import android.content.Context
import androidx.compose.ui.unit.Dp
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.mobeedev.kajakorg.common.extensions.dpToPx
import kotlin.math.cos
import kotlin.math.pow

/**
 * This won't be accurate, because the resolution of a map with the mercator projection (like Google maps) is dependent on the latitude.
 *
 * It's possible to calculate using this formula:
 *
 * 156543.03392 * Math.cos(latLng.lat() * Math.PI / 180) / Math.pow(2, zoom)
 *
 * This is based on the assumption that the earth's radius is 6378137m. Which is the value we use :)
 * (https://groups.google.com/g/google-maps-js-api-v3/c/hDRO4oHVSeM)
 */
fun getScaleMetersPerPx(position: LatLng, zoom: Double): Double =
    156543.03392 * cos(position.latitude * Math.PI / 180) / 2.0.pow(zoom)

/**
 * Returns the LatLng resulting from moving a distance from an origin in the specified heading (expressed in degrees clockwise from north).
 * Params://todo
 * heading – The direction of heading from position in degrees clockwise from north.
 */
fun calculateLatLngDpOffset(
    position: LatLng,
    offsetInDP: Dp,
    zoomLevel: Double,
    heading: Double,
    context: Context
): LatLng {
    val metersPerPixel = getScaleMetersPerPx(position, zoomLevel)
    val translationValue = context.dpToPx(offsetInDP.value)
    return SphericalUtil.computeOffset(position, metersPerPixel * translationValue, heading)
}

fun getCameraPositionWithOffset(
    position: LatLng,
    offsetInDP: Dp,
    zoomLevel: Double,
    heading: Double,
    context: Context
): CameraPosition {
    return CameraPosition.fromLatLngZoom(
        calculateLatLngDpOffset(
            position,
            offsetInDP,
            zoomLevel,
            heading,
            context
        ), zoomLevel.toFloat()
    )
}
