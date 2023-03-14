package com.mobeedev.kajakorg.designsystem.map

import android.util.Log
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.mobeedev.kajakorg.designsystem.path.PathOverviewElementHeight
import com.mobeedev.kajakorg.ui.model.PathMapItem
import com.mobeedev.kajakorg.ui.model.getNonZeroEvents
import com.mobeedev.kajakorg.ui.path.map.UserLocationSource

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun showMapOverviewScreenScreen(
    pathList: List<PathMapItem>?,
    selectedPath: PathMapItem?,
    isLocationPermissionGranted: Boolean,
    onMarkerClicked: (PathMapItem?) -> Unit,
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier
) {
    var dataList = if (selectedPath == null) {
        pathList
    } else {
        mutableListOf(selectedPath)
    }
    var bottomPadding =
        with(LocalDensity.current) { if (dataList?.size == 1) (PathOverviewElementHeight+20.dp).roundToPx() else 0 }

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
            uiSettings = uiSettings,
        ) {
            MapEffect(bottomPadding) { map ->
                map.setPadding(0, 0, 0, bottomPadding)
            }

            dataList?.forEach { pathMapItem ->
                Polyline(
                    points = pathMapItem.getNonZeroEvents().map { it.position },
                    clickable = true,
                    Color.Blue, //todo think about adding some transparency here since we do not know how exactly the river flows + change to my color blue for better resources management
                    onClick = { onMarkerClicked(pathMapItem) }
                )
                pathMapItem.eventList.firstOrNull()?.let { event ->
                    Marker(
                        state = MarkerState(position = event.position),
                        title = event.label,
                        draggable = false,
                        onClick = {
                            onMarkerClicked(pathMapItem)
                            true
                        }
                    )
                }
            }
        }
    }
}