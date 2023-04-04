package com.mobeedev.kajakorg.ui.path.overview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.path.PathOverViewElement
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.designsystem.theme.SecondaryColor
import com.mobeedev.kajakorg.designsystem.theme.White
import com.mobeedev.kajakorg.ui.model.PathOverviewItem
import com.mobeedev.kajakorg.ui.model.PathSortOrderItem
import com.mobeedev.kajakorg.ui.path.load.showLoadingState
import org.koin.androidx.compose.getViewModel

@Composable
fun PathOverviewRoute(
    modifier: Modifier = Modifier,
    viewModel: PathOverviewViewModel = getViewModel(),
    navigateToPathDetails: (Int) -> Unit,
    navigateToPathMap: () -> Unit,
    navigateToLoading: () -> Unit
) {
    val uiState: PathOverviewViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    PathOverviewScreen(
        uiState = uiState,
        modifier = modifier,
        navigateToPathDetails = navigateToPathDetails,
        navigateToPathMap = navigateToPathMap,
        navigateToLoading,
        viewModel = viewModel
    )
}

@Composable
fun PathOverviewScreen(
    uiState: PathOverviewViewModelState,
    modifier: Modifier = Modifier,
    navigateToPathDetails: (Int) -> Unit,
    navigateToPathMap: () -> Unit,
    navigateToLoading: () -> Unit,
    viewModel: PathOverviewViewModel
) {
    when (uiState) {
        PathOverviewViewModelState.Error -> {}
        PathOverviewViewModelState.InitialStart,
        PathOverviewViewModelState.Loading -> showLoadingState()

        is PathOverviewViewModelState.Success -> {
            showDataList(
                uiState,
                getFilteredPathList(uiState),
                modifier,
                navigateToPathDetails,
                navigateToPathMap,
                navigateToLoading,
                viewModel
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun showDataList(
    uiState: PathOverviewViewModelState.Success,
    filteredPathList: List<PathOverviewItem>,
    modifier: Modifier,
    navigateToPathDetails: (Int) -> Unit,
    navigateToPathMap: () -> Unit,
    navigateToLoading: () -> Unit,
    viewModel: PathOverviewViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState(),
        canScroll = {
            filteredPathList.size > 5
        })

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .focusTarget(),
        topBar = {
            Surface(shadowElevation = 4.dp) {
                getPathSearchTopBar(
                    uiState,
                    filteredPathList,
                    viewModel,
                    scrollBehavior,
                    navigateToPathMap
                )
            }
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .focusTarget(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            pathOverviewCards(filteredPathList, navigateToPathDetails)
            if (uiState.pathOverviewList.isEmpty()) {
                item() {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        OutlinedButton(onClick =navigateToLoading, colors = ButtonDefaults.outlinedButtonColors(containerColor = SecondaryColor)) {
                            Text(text = stringResource(id = R.string.download_data), color = White)
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListScope.pathOverviewCards(
    pathList: List<PathOverviewItem>,
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

private fun getFilteredPathList(uiState: PathOverviewViewModelState.Success): List<PathOverviewItem> {
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

