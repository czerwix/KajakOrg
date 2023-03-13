package com.mobeedev.kajakorg.designsystem.map

import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

const val EVENT_ZOOM_LEVEL = 17.0
const val DEFAULT_CAMERA_UPDATE_ANIMATION_LENGTH = 1000

val defaultLocation = Location("DefaultLocationProvider").apply {
    latitude = 52.1127
    longitude = 19.2119
}
val defaultCameraPosition = CameraPosition.fromLatLngZoom(LatLng(52.1127, 19.2119), 5.6f)

var locationCallback: LocationCallback? = null
val locationRequest: LocationRequest =
    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(20))
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(30))
        .setMaxUpdateDelayMillis(TimeUnit.SECONDS.toMillis(5))
        .build()

fun getLocationStateFlow(fusedLocationClient: FusedLocationProviderClient) =
    callbackFlow {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location)
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.myLooper()
            )
        } catch (e: SecurityException) {
            throw IllegalStateException("Location permissions are not granted!")
        }
        awaitClose { fusedLocationClient.removeLocationUpdates(locationCallback!!) }
    }