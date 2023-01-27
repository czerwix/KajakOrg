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
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.designsystem.path.pathIdToPicture
import com.mobeedev.kajakorg.designsystem.toolbar.CollapsingToolbar
import com.mobeedev.kajakorg.designsystem.toolbar.FixedScrollFlagState
import com.mobeedev.kajakorg.designsystem.toolbar.ToolbarState
import com.mobeedev.kajakorg.designsystem.toolbar.scrollflags.ExitUntilCollapsedState
import com.mobeedev.kajakorg.ui.path.load.showLoadingState
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun PathDetailsRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PathDetailsViewModel = getViewModel(),
) {
    val uiState: PathDetailsViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    PathDetailsScreen(
        uiState = uiState,
        modifier = modifier,
        viewModel = viewModel,
        onBackClick = onBackClick
    )
}

@Composable
fun PathDetailsScreen( // TODO: ADD PREVIEW
    uiState: PathDetailsViewModelState,
    modifier: Modifier = Modifier,
    viewModel: PathDetailsViewModel,
    onBackClick: () -> Unit
) {
    when (uiState) {
        PathDetailsViewModelState.Error -> TODO()
        is PathDetailsViewModelState.InitialStart -> showLoadingState() //todo remove loading state views for testing only
        is PathDetailsViewModelState.Success -> showPathDetails(
            uiState,
            viewModel,
            onBackClick,
            modifier
        )
    }
}

private val MinToolbarHeight = 60.dp
private val MaxToolbarHeight = 360.dp

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ExitUntilCollapsedState.Saver) {
        ExitUntilCollapsedState(toolbarHeightRange)
    }
}

@Composable
fun showPathDetails(
    uiState: PathDetailsViewModelState.Success,
    viewModel: PathDetailsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val toolbarHeightRange = with(LocalDensity.current) {
        MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
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
        PathDetailsList( //todo add my lazy list here
            pathList = uiState.path.pathSectionsEvents,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = toolbarState.height + toolbarState.offset }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { scope.coroutineContext.cancelChildren() }
                    )
                },
            listState = listState,
            contentPadding = PaddingValues(bottom = if (toolbarState is FixedScrollFlagState) MinToolbarHeight else 0.dp)
        )
        CollapsingToolbar(
            backgroundImageResId = pathIdToPicture[uiState.path.overview.id]!!,
            progress = toolbarState.progress,
            onPrivacyTipButtonClicked = {},//todo
            onSettingsButtonClicked = {},//todo
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                .graphicsLayer { translationY = toolbarState.offset },
            uiState.path.overview
        )
    }
}


//@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Composable
//fun showPathDetails(
//    uiState: PathDetailsViewModelState.Success,
//    modifier: Modifier,
//    viewModel: PathDetailsViewModel,
//    onBackClick: () -> Unit
//) {
//    val scrollState: ScrollState = rememberScrollState(0)
//    Box(modifier = Modifier.fillMaxSize()) {//maybe extract this to separate file and make it more abstract  :D
//        showHeader()
//        showBody(uiState, scrollState)
////        showToolbar()
////        showTitle()
//    }


//    val scrollBehavior =
//        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
//
//    Scaffold(
//        modifier = Modifier
//            .nestedScroll(scrollBehavior.nestedScrollConnection)
//            .fillMaxSize()
//            .focusTarget(),
//        topBar = {
//            getPathDetailTopBar(uiState, viewModel, scrollBehavior, onBackClick)
//        }
//    ) {
//        LazyColumn(
//            modifier = modifier
//                .padding(it)
//                .fillMaxSize()
//                .focusTarget(),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
////            pathCards(pathList, navigateToPathDetails)
//        }
//    }

//}
//
//private val headerHeight = 275.dp//todo figure out a place for this const.
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
//@Composable
//fun showHeader() {
//
//    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(headerHeight)
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.kajak1),
//            contentDescription = String.empty,
//            contentScale = ContentScale.FillBounds
//        )
//
//        Box(
//            Modifier
//                .fillMaxSize()
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(Color.Transparent, Color(0xAA000000)),
//                        startY = 3 * headerHeightPx / 4 // to wrap the title only
//                    )
//                )
//        )
//    }
//}

//@Composable
//private fun showBody(
//    uiState: PathDetailsViewModelState.Success,
//    scrollState: ScrollState
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
////        modifier = Modifier.verticalScroll(scrollState)
//    ) {
//        Spacer(Modifier.height(headerHeight))//todo uncomment when continuing work with topBar
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .focusTarget(),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            pathDetailsCards(uiState.path.pathSectionsEvents)
//        }
//    }
//}

//private fun LazyListScope.pathDetailsCards(pathList: List<PathEvent>) {
//    item {//todo replace with separators here :D
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(16.dp)
//        )
//    }
//    pathList.forEach { element ->
//        if (element is Section) {
//            item(key = element.hashCode()) {
//                PathSectionElement(item = element, onClick = {})//todo add onclick here
//            }
//            items(items = element.events, key = { it.hashCode() }) { nestedSectionEvent ->
//                PathEventElement(item = nestedSectionEvent, onClick = {})
//            }
//        } else if (element is Event) {
//            item(key = element.hashCode()) {
//                PathEventElement(item = element, onClick = {})
//            }
//        }
//    }
//    item {
//        Box(//todo replace with separators here :D
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(16.dp)
//        )
//    }
//}
