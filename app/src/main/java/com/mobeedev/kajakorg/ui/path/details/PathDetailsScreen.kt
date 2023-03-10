package com.mobeedev.kajakorg.ui.path.details

import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.designsystem.path.pathIdToPicture
import com.mobeedev.kajakorg.designsystem.toolbar.CollapsingToolbar
import com.mobeedev.kajakorg.designsystem.toolbar.FixedScrollFlagState
import com.mobeedev.kajakorg.designsystem.toolbar.MaxToolbarHeight
import com.mobeedev.kajakorg.designsystem.toolbar.MinToolbarHeight
import com.mobeedev.kajakorg.designsystem.toolbar.ToolbarState
import com.mobeedev.kajakorg.designsystem.toolbar.scrollflags.ExitUntilCollapsedState
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
    val listState = rememberLazyListState()

    val scope = rememberCoroutineScope()

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
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = toolbarState.height + toolbarState.offset }
                .pointerInput(Unit) {
                    detectTapGestures(onPress = { scope.coroutineContext.cancelChildren() })
                },
            listState = listState,
            contentPadding = PaddingValues(bottom = if (toolbarState is FixedScrollFlagState) MinToolbarHeight else 0.dp)
        )
        CollapsingToolbar(backgroundImageResId = pathIdToPicture[uiState.path.overview.id]!!,
            progress = toolbarState.progress,
            onBackArrowButtonClicked = onBackClick,
            onMapButtonClicked = { navigateToPathMap(uiState.path.overview.id) },
            onStarButtonClicked = {},//todo
            onSettingsButtonClicked = {},//todo
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                .graphicsLayer { translationY = toolbarState.offset },
            uiState.path.overview
        )
    }
}