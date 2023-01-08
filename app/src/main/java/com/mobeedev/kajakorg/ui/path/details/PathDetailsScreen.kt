package com.mobeedev.kajakorg.ui.path.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.ui.path.load.showLoadingState
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
fun PathDetailsScreen(
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
            modifier,
            viewModel,
            onBackClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun showPathDetails(
    uiState: PathDetailsViewModelState.Success,
    modifier: Modifier,
    viewModel: PathDetailsViewModel,
    onBackClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .focusTarget(),
        topBar = {
            getPathDetailTopBar()
        }
    ) {
        LazyColumn(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
                .focusTarget(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
//            pathCards(pathList, navigateToPathDetails)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun getPathDetailTopBar() {


}
