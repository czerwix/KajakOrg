package com.mobeedev.kajakorg.ui.path.map.details

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.rememberCameraPositionState
import com.mobeedev.kajakorg.common.view.checkLocationPermissions
import com.mobeedev.kajakorg.designsystem.map.DEFAULT_CAMERA_UPDATE_ANIMATION_LENGTH
import com.mobeedev.kajakorg.designsystem.map.EVENT_ZOOM_LEVEL
import com.mobeedev.kajakorg.designsystem.map.MapEventCard
import com.mobeedev.kajakorg.designsystem.map.defaultCameraPosition
import com.mobeedev.kajakorg.designsystem.map.showMapPathDetailsScreen
import com.mobeedev.kajakorg.designsystem.path.pathEventWithoutMapElementHeight
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.PathEvent
import com.mobeedev.kajakorg.domain.model.detail.Section
import com.mobeedev.kajakorg.domain.model.detail.eventId
import com.mobeedev.kajakorg.domain.model.detail.flatPathMapSectionEventList
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.path.load.showLoadingState
import com.mobeedev.kajakorg.ui.path.map.getCameraPositionWithOffset
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import kotlin.math.absoluteValue

@Composable
fun PathDetailsMapRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PathDetailMapViewModel = getViewModel(),
) {
    val uiState: PathDetailsMapViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    checkLocationPermissions(onPermissionGranted = {
        PathDetailsMapScreen(
            uiState, onBackClick, true, modifier
        )
    }, onPermissionDenied = {
        PathDetailsMapScreen(
            uiState, onBackClick, false, modifier
        )
    })
}

@Composable
fun PathDetailsMapScreen(
    uiState: PathDetailsMapViewModelState,
    onBackClick: () -> Unit,
    locationStatus: Boolean,
    modifier: Modifier
) {
    when (uiState) {
        PathDetailsMapViewModelState.Error -> TODO()
        is PathDetailsMapViewModelState.InitialStart, PathDetailsMapViewModelState.Loading -> showLoadingState()
        is PathDetailsMapViewModelState.Success -> {
            showSuccessMapDetailsScreen(
                uiState.path,
                onBackClick,
                locationStatus,
                modifier
            )
        }
    }
}

private fun getCameraPosition(
    event: PathEvent,
    zoomLevel: Double,
    context: Context,
    offsetInDp: Dp = 40.dp
) = when (event) {
    is Section -> {
        getCameraPositionWithOffset(
            position = event.events.first().position,
            offsetInDp,
            zoomLevel = zoomLevel,
            180.0,
            context = context
        )
    }

    is Event -> {
        getCameraPositionWithOffset(
            position = event.position,
            offsetInDp,
            zoomLevel = zoomLevel,
            180.0,
            context = context
        )
    }

    else -> {
        defaultCameraPosition
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalFoundationApi::class)
@Composable
fun showSuccessMapDetailsScreen(
    path: PathItem,
    onBackClick: () -> Unit,
    isLocationPermissionGranted: Boolean,
    modifier: Modifier
) {
    val mapEventFlatList = path.pathSectionsEvents.flatPathMapSectionEventList()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val mapOffset = remember { configuration.screenHeightDp / 16 }
    val pagerState = rememberPagerState()
    val cameraPositionState = rememberCameraPositionState {
        position =
            getCameraPosition(
                path.pathSectionsEvents.first(),
                EVENT_ZOOM_LEVEL,
                context,
                mapOffset.dp
            )
    }
    Box(modifier) {
        //MAP
        showMapPathDetailsScreen(
            path = path,
            isLocationPermissionGranted = isLocationPermissionGranted,
            onMarkerClicked = { event ->
                scope.launch {
                    cameraPositionState.animate(
                        update = CameraUpdateFactory.newCameraPosition(
                            getCameraPosition(event, EVENT_ZOOM_LEVEL, context, mapOffset.dp)
                        ),
                        durationMs = DEFAULT_CAMERA_UPDATE_ANIMATION_LENGTH
                    )
                }
                scope.launch {
                    pagerState.animateScrollToPage(mapEventFlatList.indexOfFirst { it.eventId() == event.id })
                }
            }, cameraPositionState
        )
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            //TOP BAR
            Row(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 16.dp, top = 16.dp)
                        .background(
                            color = LocalContentColor.current.copy(alpha = 0.0f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            //BottomCards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 16.dp)
                    .weight(1f, false)
            ) {
                showEventPager(mapEventFlatList, pagerState) {
                    scope.launch {
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newCameraPosition(
                                getCameraPosition(it, EVENT_ZOOM_LEVEL, context, mapOffset.dp)
                            ),
                            durationMs = DEFAULT_CAMERA_UPDATE_ANIMATION_LENGTH
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun showEventPager(
    pathSectionsEvents: List<PathEvent>,
    pagerState: PagerState,
    onPathEventSelected: (PathEvent) -> Unit
) {
    LaunchedEffect(pagerState) {
        // Collect from the pager state a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onPathEventSelected(pathSectionsEvents[page])
        }
    }

    HorizontalPager(
        pageCount = pathSectionsEvents.size,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 32.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(pathEventWithoutMapElementHeight)
    ) { page: Int ->
        Card(
            Modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset =
                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                    // We animate the scaleX + scaleY, between 85% and 100%
                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            MapEventCard(pathSectionsEvents[page])
        }
    }
}
