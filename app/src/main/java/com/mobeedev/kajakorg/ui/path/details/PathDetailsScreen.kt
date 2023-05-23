package com.mobeedev.kajakorg.ui.path.details

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.common.view.rememberForeverLazyListState
import com.mobeedev.kajakorg.designsystem.path.pathAuthors
import com.mobeedev.kajakorg.designsystem.theme.PrimaryColor
import com.mobeedev.kajakorg.designsystem.theme.SelectedTabColor
import com.mobeedev.kajakorg.domain.model.detail.getSections
import com.mobeedev.kajakorg.ui.model.GoogleMapsStatusItem
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.path.load.showLoadingState
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
fun showPathDetails(
    uiState: PathDetailsViewModelState.Success,
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: PathDetailsViewModel,
    onBackClick: () -> Unit,
    navigateToPathMap: (Int) -> Unit
) {
    var isSettingsMenuVisible: Boolean by remember { mutableStateOf(false) }
    val listState = rememberForeverLazyListState(key = "Path_details_${uiState.path.overview.id}")
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val collapsedSectionState: SnapshotStateList<Pair<Int, Boolean>> = remember {
        uiState.path.pathSectionsEvents.getSections()
            .mapNotNull { section -> section?.let { it.id to true } }
            .toMutableStateList()
    }

    Column(modifier = modifier) {
        //AppBar
        PathBarContent(
            uiState = uiState,
            onBackArrowButtonClicked = onBackClick,
            onMapButtonClicked = { navigateToPathMap(uiState.path.overview.id) },
            onStarButtonClicked = {//todo
                Toast.makeText(
                    context,
                    "Not yet implemented. Maybe next update :D",
                    Toast.LENGTH_LONG
                ).show()
            },
            onSettingsButtonClicked = {
                isSettingsMenuVisible = true
            }) {
            showPathSettings(
                uiState,
                isSettingsMenuVisible,
                {
                    viewModel.onDisableGoogleMapsClicked(it)
                    isSettingsMenuVisible = false
                },
                { isSettingsMenuVisible = false })
        }
        //tabs
        var tabIndex by remember { mutableStateOf(0) }

        val tabs = listOf(stringResource(id = R.string.details), stringResource(id = R.string.about_path))

        Column(modifier = Modifier.fillMaxWidth()) {
            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = PrimaryColor,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                        color = SelectedTabColor
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        selectedContentColor = SelectedTabColor,
                        unselectedContentColor = Color.White,
                    )
                }
            }
            when (tabIndex) {
                0 -> {
                    PathDetailsList(
                        pathData = uiState.path,
                        collapsedSectionState,
                        modifier = Modifier
                            .fillMaxSize(),
                        listState = listState,
                        googleMapVisibilityState = uiState.googleMapStatus,
                    )
                }

                1 -> {
                    PathAboutList(
                        pathData = uiState.path,
                        modifier = Modifier
                            .fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Composable
fun PathAboutList(pathData: PathItem, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            Text(
                text = stringResource(id = R.string.path_about_path_authors),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        item {
            Text(
                text = pathAuthors[pathData.overview.id] ?: String.empty,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
fun PathBarContent(
    uiState: PathDetailsViewModelState.Success,
    onBackArrowButtonClicked: () -> Unit,
    onMapButtonClicked: () -> Unit,
    onStarButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    settingsButtonContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(PrimaryColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        IconButton(
            onClick = onBackArrowButtonClicked,//todo
            modifier = Modifier
                .size(34.dp)
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

        Text(
            text = uiState.path.overview.name,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 28.sp,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .wrapContentHeight()
                .wrapContentWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = onMapButtonClicked,
                modifier = Modifier
                    .size(34.dp)
                    .background(
                        color = LocalContentColor.current.copy(alpha = 0.0f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.outline_map_24),
                    contentDescription = null,
                    tint = Color.White
                )
            }
//            IconButton(
//                onClick = onStarButtonClicked,
//                modifier = Modifier
//                    .size(34.dp)
//                    .background(
//                        color = LocalContentColor.current.copy(alpha = 0.0f),
//                        shape = CircleShape
//                    )
//            ) {
//                Icon(
//                    modifier = Modifier.fillMaxSize(),
//                    painter = painterResource(R.drawable.outline_star_24),
//                    contentDescription = null,
//                    tint = Color.White
//                )
//            }
            Box(modifier = Modifier.wrapContentSize()) {
                IconButton(
                    onClick = onSettingsButtonClicked,
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = LocalContentColor.current.copy(alpha = 0.0f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                settingsButtonContent()
            }
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
