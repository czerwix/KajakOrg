package com.mobeedev.kajakorg.ui.path

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.ui.MainViewModel
import com.mobeedev.kajakorg.ui.MainViewModelState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LoadKajakDataRoute(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = getViewModel(),
    navigateToPathOverview: () -> Unit
) {
    val uiState: MainViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    LoadKajakDataScreen(
        pathState = uiState,
        modifier = modifier,
        navigateToPathOverview = navigateToPathOverview
    )
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun LoadKajakDataScreen(
    pathState: MainViewModelState,
    modifier: Modifier = Modifier,
    navigateToPathOverview: () -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (pathState) {
            MainViewModelState.Error -> {
//                DownloadErrorRetry()
            }
            is MainViewModelState.Success -> {
                //show loading and go to pathOverView
                navigateToPathOverview.invoke()
            }
            else -> {
//                show loading state
            }
        }
    }
}