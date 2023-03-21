package com.mobeedev.kajakorg.ui.path.map.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.rememberCameraPositionState
import com.mobeedev.kajakorg.common.view.checkLocationPermissions
import com.mobeedev.kajakorg.designsystem.map.DEFAULT_CAMERA_UPDATE_ANIMATION_LENGTH
import com.mobeedev.kajakorg.designsystem.map.defaultCameraPosition
import com.mobeedev.kajakorg.designsystem.map.showMapOverviewScreenScreen
import com.mobeedev.kajakorg.designsystem.path.PathOverViewElement
import com.mobeedev.kajakorg.ui.model.PathMapItem
import com.mobeedev.kajakorg.ui.model.getBounds
import org.koin.androidx.compose.getViewModel

@Composable
fun PathMapRoute(
    onBackClick: () -> Unit,
    navigateToPathDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PathMapViewModel = getViewModel(),
) {
    val uiState: PathMapViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    checkLocationPermissions(onPermissionGranted = {
        PathMapScreen(
            uiState, viewModel, true, onBackClick, navigateToPathDetails, modifier
        )
    }, onPermissionDenied = {
        PathMapScreen(
            uiState, viewModel, false, onBackClick, navigateToPathDetails, modifier
        )
    })
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PathMapScreen(
    uiState: PathMapViewModelState,
    viewModel: PathMapViewModel,
    locationStatus: Boolean,
    onBackClick: () -> Unit,
    navigateToPathDetails: (Int) -> Unit,
    modifier: Modifier

) {
    when (uiState) {
        PathMapViewModelState.InitialStart, PathMapViewModelState.Loading -> {
            showPathMapScreen(
                pathsList = null,
                selectedPath = null,
                lastSelectedPath = null,
                viewModel = viewModel,
                modifier = modifier,
                onBackClick = onBackClick,
                navigateToPathDetails,
                isLocationPermissionGranted = locationStatus
            )
        }

        is PathMapViewModelState.Success -> {
            showPathMapScreen(
                pathsList = uiState.pathOverviewList,
                selectedPath = uiState.selectedPath,
                lastSelectedPath = uiState.lastSelectedPath,
                viewModel = viewModel,
                modifier = modifier,
                onBackClick = onBackClick,
                navigateToPathDetails,
                isLocationPermissionGranted = locationStatus
            )
        }

        PathMapViewModelState.Error -> {
//            TODO show map data loading error
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun showPathMapScreen(
    pathsList: List<PathMapItem>?,
    selectedPath: PathMapItem?,
    lastSelectedPath: PathMapItem?,
    viewModel: PathMapViewModel,
    modifier: Modifier,
    onBackClick: () -> Unit,
    navigateToPathDetails: (Int) -> Unit,
    isLocationPermissionGranted: Boolean
) {
    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }
    val density = LocalDensity.current
    var pathSelectedVisible by remember { mutableStateOf(false) }
    pathSelectedVisible = selectedPath != null

    Box(Modifier.fillMaxSize()) {
        //MAP
        showMapOverviewScreenScreen(//todo change to overviewMap
            pathList = pathsList,
            selectedPath,
            isLocationPermissionGranted = isLocationPermissionGranted,
            onMarkerClicked = { path ->
                viewModel.onPathSelected(path)
            },
            cameraPositionState
        )

        if (pathSelectedVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { viewModel.onPathSelected(null) }
            )
            //zoom to path
            LaunchedEffect(key1 = true) {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngBounds(
                        selectedPath!!.getBounds(),
                        with(density) { 26.dp.roundToPx() }),
                    durationMs = DEFAULT_CAMERA_UPDATE_ANIMATION_LENGTH
                )
            }
        }
        //show pathOverView
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedVisibility(
                modifier = Modifier
                    .padding(bottom = 24.dp),
                visible = pathSelectedVisible,
                enter = slideInVertically { with(density) { 40.dp.roundToPx() } }
                        + expandVertically(expandFrom = Alignment.Bottom)
                        + fadeIn(initialAlpha = 0.3f),
                exit = slideOutVertically { with(density) { 40.dp.roundToPx() } }
                        + shrinkVertically()
                        + fadeOut()
            ) {
                (selectedPath ?: lastSelectedPath)?.apply {
                    PathOverViewElement(overview, navigateToPathDetails)
                }
            }
        }

//        IconButton(
//            onClick = onBackClick,
//            modifier = Modifier
//                .size(48.dp)
//                .padding(start = 16.dp, top = 16.dp)
//                .background(
//                    color = LocalContentColor.current.copy(alpha = 0.0f),
//                    shape = CircleShape
//                )
//        ) {
//            Icon(
//                modifier = Modifier.fillMaxSize(),
//                imageVector = Icons.Rounded.ArrowBack,
//                contentDescription = null,
//                tint = Color.White
//            )
//        }
    }
}
