package com.mobeedev.kajakorg.ui.path.details

import android.widget.Toast
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.path.pathIdToPicture
import com.mobeedev.kajakorg.designsystem.toolbar.CollapsingToolbar
import com.mobeedev.kajakorg.designsystem.toolbar.FixedScrollFlagState
import com.mobeedev.kajakorg.designsystem.toolbar.MaxToolbarHeight
import com.mobeedev.kajakorg.designsystem.toolbar.MinToolbarHeight
import com.mobeedev.kajakorg.designsystem.toolbar.ToolbarState
import com.mobeedev.kajakorg.designsystem.toolbar.scrollflags.ExitUntilCollapsedState
import com.mobeedev.kajakorg.ui.model.GoogleMapsStatusItem
import com.mobeedev.kajakorg.ui.path.load.showLoadingState
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@Composable
fun PathDetailsRoute(
    onBackClick: () -> Unit,
    navigateToPathMap: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PathDetailsViewModel = getViewModel()
) {
    val uiState: PathDetailsViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    PathDetailsScreen(
        uiState = uiState,
        modifier = modifier,
        viewModel = viewModel,
        onBackClick = onBackClick,
        navigateToPathMap = navigateToPathMap
    )
}

@Composable
fun PathDetailsScreen( // TODO: ADD PREVIEW
    uiState: PathDetailsViewModelState,
    modifier: Modifier = Modifier,
    viewModel: PathDetailsViewModel,
    onBackClick: () -> Unit,
    navigateToPathMap: (Int) -> Unit
) {
    when (uiState) {
        PathDetailsViewModelState.Error -> TODO()
        is PathDetailsViewModelState.InitialStart -> showLoadingState() //todo remove loading state views for testing only
        is PathDetailsViewModelState.Success -> showPathDetails(
            uiState, modifier, viewModel, onBackClick, navigateToPathMap
        )
    }
}

@Composable
private fun rememberPathDetailsToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUntilCollapsedState.Saver) {
        ExitUntilCollapsedState(toolbarHeightRange)
    }
}

@Composable
fun showPathDetails(
    uiState: PathDetailsViewModelState.Success,
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: PathDetailsViewModel,
    onBackClick: () -> Unit,
    navigateToPathMap: (Int) -> Unit
) {
    val toolbarHeightRange = with(LocalDensity.current) {
        MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberPathDetailsToolbarState(toolbarHeightRange)
    var isSettingsMenuVisible: Boolean by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached =
                    listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(0f, toolbarState.consumed)
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (available.y > 0) {
                    scope.launch {
                        animateDecay(
                            initialValue = toolbarState.height + toolbarState.offset,
                            initialVelocity = available.y,
                            animationSpec = FloatExponentialDecaySpec()
                        ) { value, velocity ->
                            toolbarState.scrollTopLimitReached =
                                listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                            toolbarState.scrollOffset =
                                toolbarState.scrollOffset - (value - (toolbarState.height + toolbarState.offset))
                            if (toolbarState.scrollOffset == 0f) scope.coroutineContext.cancelChildren()
                        }
                    }
                }

                return super.onPostFling(consumed, available)
            }
        }
    }

    Box(modifier = modifier.nestedScroll(nestedScrollConnection)) {
        PathDetailsList(pathList = uiState.path.pathSectionsEvents,
            description = if (uiState.shouldShowDescription) uiState.path.overview.description else null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = toolbarState.height + toolbarState.offset }
                .pointerInput(Unit) {
                    detectTapGestures(onPress = { scope.coroutineContext.cancelChildren() })
                },
            listState = listState,
            googleMapVisibilityState = uiState.googleMapStatus,
            contentPadding = PaddingValues(bottom = if (toolbarState is FixedScrollFlagState) MinToolbarHeight else 0.dp)
        )
        CollapsingToolbar(
            backgroundImageResId = pathIdToPicture[uiState.path.overview.id]!!,
            progress = toolbarState.progress,
            onBackArrowButtonClicked = onBackClick,
            onMapButtonClicked = { navigateToPathMap(uiState.path.overview.id) },
            onStarButtonClicked = {//todo
                Toast.makeText(
                    context,
                    "Not yet implemented. Maybe next update :D",
                    Toast.LENGTH_LONG
                ).show()
            },
            onSettingsButtonClicked = {//todo
                isSettingsMenuVisible = true
            },
            onDescriptionClicked = {
                if (uiState.shouldShowDescription.not()) {
                    viewModel.onDescriptionClicked()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                .graphicsLayer { translationY = toolbarState.offset },
            uiState.path.overview
        ) {
            showPathSettings(
                uiState,
                isSettingsMenuVisible,
                {
                    viewModel.onDisableGoogleMapsClicked(it)
                    isSettingsMenuVisible = false
                },
                { isSettingsMenuVisible = false })
        }
    }
}

@Composable
fun showPathSettings(
    uiState: PathDetailsViewModelState.Success,
    isSettingsMenuVisible: Boolean,
    onSelected: (GoogleMapsStatusItem) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = isSettingsMenuVisible,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        DropdownMenuItem(
            leadingIcon = {
                if (uiState.googleMapStatus == GoogleMapsStatusItem.DisableMap) {
                    Icon(
                        painter = painterResource(id = R.drawable.done_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        tint = Color.Black
                    )
                }
            },
            text = {
                Text(
                    text = stringResource(id = R.string.path_google_map_disable),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            onClick = {
                onSelected(GoogleMapsStatusItem.DisableMap)
            },
            enabled = uiState.googleMapStatus == GoogleMapsStatusItem.EnableMap,
            modifier = Modifier.wrapContentSize()
        )
        DropdownMenuItem(
            leadingIcon = {
                if (uiState.googleMapStatus == GoogleMapsStatusItem.EnableMap) {
                    Icon(
                        painter = painterResource(id = R.drawable.done_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        tint = Color.Black
                    )
                }
            },
            text = {
                Text(
                    text = stringResource(id = R.string.path_google_map_enable),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onClick = {
                onSelected(GoogleMapsStatusItem.EnableMap)
            },
            enabled = uiState.googleMapStatus == GoogleMapsStatusItem.DisableMap,
            modifier = Modifier.wrapContentSize()
        )
    }
}
