package com.mobeedev.kajakorg.ui.path.map.overview

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mobeedev.kajakorg.R
import org.koin.androidx.compose.getViewModel

@Composable
fun PathMapRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PathMapViewModel = getViewModel(),
) {
    val uiState: PathMapViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    PathMapScreen(
        uiState = uiState,
        modifier = modifier,
        viewModel = viewModel,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PathMapScreen(
    uiState: PathMapViewModelState,
    viewModel: PathMapViewModel,
    modifier: Modifier,
    onBackClick: () -> Unit
) {
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    if (locationPermissionState.allPermissionsGranted) {
        handleMapStateAndLocation(uiState, viewModel, modifier, onBackClick, true)
    } else {
        if (locationPermissionState.shouldShowRationale) {
//            showPermissionRequest(locationPermissionState)
        } else {
            SideEffect {
                locationPermissionState.launchMultiplePermissionRequest()
            }
            handleMapStateAndLocation(uiState, viewModel, modifier, onBackClick, false)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun handleMapStateAndLocation(
    uiState: PathMapViewModelState,
    viewModel: PathMapViewModel,
    modifier: Modifier,
    onBackClick: () -> Unit,
    isLocationPermissionGranted: Boolean
) {
    when (uiState) {
        PathMapViewModelState.Loading -> {}
//            showPathMap(
//            null,
//            viewModel,
//            modifier,
//            onBackClick,
//            isLocationPermissionGranted
//        )

        is PathMapViewModelState.Success -> {
//            showPathMap(
//                uiState.pathOverviewList,
//                viewModel,
//                modifier,
//                onBackClick,
//                isLocationPermissionGranted
//            )
        }

        else -> Unit
    }
}
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun showPathMap(
//    pathsList: List<PathMapItem>?,
//    viewModel: PathMapViewModel,
//    modifier: Modifier,
//    onBackClick: () -> Unit,
//    isLocationPermissionGranted: Boolean
//) {
//    val cameraPositionState = rememberCameraPositionState {
//        position = defaultCameraPosition
//    }
//    var mapProperties by remember {
//        mutableStateOf(MapProperties(mapType = MapType.HYBRID))
//    }
//    val locationSource = MyLocationSource()
//
//    if (isLocationPermissionGranted) {
//        val fusedLocationClient =
//            LocationServices.getFusedLocationProviderClient(LocalContext.current)
//        val locationState =
//            getLocationStateFlow(fusedLocationClient).collectAsState(defaultLocation)
//
//        LaunchedEffect(locationState.value) {
//            locationSource.onLocationChanged(locationState.value)
//        }
//    }
//
//    Box(Modifier.fillMaxSize()) {
//     GoogleMap(
//            modifier = Modifier.matchParentSize(),
//            cameraPositionState = cameraPositionState,
//            locationSource = locationSource,
//            properties = mapProperties,
//        ) {
//            pathsList?.forEach { path: PathMapItem ->
//
//                Polyline(
//                    points = path.points.sortedBy { it.atKilometer }.map { it.position }
//                        .filterNot { it.latitude == 0.0 || it.longitude == 0.0 },
//                    clickable = true,
//                    Color.Blue
//                )
//            }
//        }
//    }
//}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun showLocationPermissionRequest(locationPermissionState: MultiplePermissionsState) {
    Text(stringResource(id = R.string.location_rationale))
    Button(onClick = { locationPermissionState.launchMultiplePermissionRequest() }) {
        Text(stringResource(id = R.string.location_request))
    }
}