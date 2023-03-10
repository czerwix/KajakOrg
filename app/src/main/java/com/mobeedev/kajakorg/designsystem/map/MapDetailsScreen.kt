package com.mobeedev.kajakorg.designsystem.map

import android.location.Location
import android.os.Looper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.model.toPathEventsList
import com.mobeedev.kajakorg.ui.path.map.MyLocationSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

val defaultLocation = Location("DefaultLocationProvider").apply {
    latitude = 52.1127
    longitude = 19.2119
}
val defaultCameraPosition = CameraPosition.fromLatLngZoom(LatLng(52.1127, 19.2119), 5.6f)

private var locationCallback: LocationCallback? = null
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun showMapPathDetailsScreen(
    path: PathItem,
    isLocationPermissionGranted: Boolean,
    onMarkerClicked: (Event) -> Unit,
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier
) {
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.HYBRID,
                isBuildingEnabled = true,
                isIndoorEnabled = false,
                isTrafficEnabled = false,
                isMyLocationEnabled = true
            )
        )
    }
    val locationSource = MyLocationSource()

    if (isLocationPermissionGranted) {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(LocalContext.current)
        val locationState =
            getLocationStateFlow(fusedLocationClient).collectAsState(defaultLocation)

        LaunchedEffect(locationState.value) {
            locationSource.onLocationChanged(locationState.value)
        }
    }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            locationSource = locationSource,
            properties = mapProperties,
        ) {
            val eventList = path.pathSectionsEvents.toPathEventsList()
            Polyline(//sorting should be good here
                points = eventList.map { it.position }
                    .filterNot { it.latitude == 0.0 || it.longitude == 0.0 },
                clickable = false,
                Color.Blue//todo think about adding some transparency here since we do not know how exactly the river flows
            )

            eventList.forEach { event ->
                Marker(
                    state = MarkerState(position = event.position),
                    title = event.label,
                    draggable = false,
                    onClick = {
                        onMarkerClicked(event)
                        true
                    }
                )
            }
        }
    }
}
