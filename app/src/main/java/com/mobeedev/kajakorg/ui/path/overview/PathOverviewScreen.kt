package com.mobeedev.kajakorg.ui.path.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.designsystem.component.PathOverViewElement
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.ui.model.PathOveriewItem
import com.mobeedev.kajakorg.ui.model.PathSortOrderItem
import com.mobeedev.kajakorg.ui.path.load.showLoadingState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun PathOverviewRoute(
    modifier: Modifier = Modifier,
    viewModel: PathOverviewViewModel = getViewModel(),
    navigateToPathDetails: (Int) -> Unit
) {
    val uiState: PathOverviewViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    PathOverviewScreen(
        uiState = uiState,
        modifier = modifier,
        navigateToPathDetails = navigateToPathDetails,
        viewModel = viewModel
    )
}

@Composable
fun PathOverviewScreen(
    uiState: PathOverviewViewModelState,
    modifier: Modifier = Modifier,
    navigateToPathDetails: (Int) -> Unit,
    viewModel: PathOverviewViewModel
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            PathOverviewViewModelState.Error -> TODO()
            PathOverviewViewModelState.InitialStart,
            PathOverviewViewModelState.Loading,
            is PathOverviewViewModelState.PartialOverviewData -> showLoadingState()
            is PathOverviewViewModelState.Success -> {
                showDataList(
                    uiState,
                    getFilteredPathList(uiState),
                    modifier,
                    navigateToPathDetails,
                    viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun showDataList(
    uiState: PathOverviewViewModelState.Success,
    pathList: List<PathOveriewItem>,
    modifier: Modifier,
    navigateToPathDetails: (Int) -> Unit,
    viewModel: PathOverviewViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState(),
        canScroll = {
            pathList.size > 5
        })

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .focusTarget(),
        topBar = {
            getPathSearchTopBar(uiState, viewModel, scrollBehavior)
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .focusTarget(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            pathCards(pathList, navigateToPathDetails)
        }
    }
}

private fun LazyListScope.pathCards(
    pathList: List<PathOveriewItem>,
    navigateToPathDetails: (Int) -> Unit
) {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
    }
    items(pathList, { it.id }) { item ->
        PathOverViewElement(item, navigateToPathDetails)
    }
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
    }
}

private fun getFilteredPathList(uiState: PathOverviewViewModelState.Success): List<PathOveriewItem> {
    val filteredList = if (uiState.textFilter.isNotBlank()) {
        uiState.pathOverviewList.filter {
            it.name.contains(uiState.textFilter, true)
        }
    } else {
        uiState.pathOverviewList
    }

    return when (uiState.sortOrder) {
        PathSortOrderItem.AToZ -> filteredList.sortedBy { it.name }
        PathSortOrderItem.ZToA -> filteredList.sortedByDescending { it.name }
        PathSortOrderItem.Longest -> filteredList.sortedByDescending { it.length }
        PathSortOrderItem.Shortest -> filteredList.sortedBy { it.length }
    }
}

@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
@Composable
fun PathOverViewPopulated() {
    KajakTheme {

    }
}

