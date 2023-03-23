package com.mobeedev.kajakorg.designsystem.map

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
import com.google.android.gms.location.LocationServices
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.isLocationNonZero
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.model.toPathEventsList
import com.mobeedev.kajakorg.ui.path.map.UserLocationSource

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
    var uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = true,
                rotationGesturesEnabled = true,
                scrollGesturesEnabled = true,
                scrollGesturesEnabledDuringRotateOrZoom = true,
                tiltGesturesEnabled = true,
                zoomControlsEnabled = false
            )
        )
    }
    val locationSource = UserLocationSource()

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
            uiSettings = uiSettings
        ) {
            val eventList = path.pathSectionsEvents.toPathEventsList()
            Polyline(//sorting should be good here
                points = eventList.filter { it.isLocationNonZero() }.map { it.position },
                clickable = false,
                Color.Blue//todo think about adding some transparency here since we do not know how exactly the river flows
            )

            eventList.forEach { event ->
                if (event.isLocationNonZero())
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
