package com.mobeedev.kajakorg.ui.path.load

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.component.DownloadErrorRetry
import com.mobeedev.kajakorg.ui.MainDataLoadingViewModel
import com.mobeedev.kajakorg.ui.MainViewModelState
import org.koin.androidx.compose.getViewModel

@Composable
fun LoadKajakDataRoute(
    modifier: Modifier = Modifier,
    viewModel: MainDataLoadingViewModel = getViewModel(),
    navigateToPathOverview: () -> Unit
) {
    val uiState: MainViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    LoadKajakDataScreen(
        pathState = uiState,
        modifier = modifier,
        navigateToPathOverview = navigateToPathOverview
    ) { viewModel.onErrorReload() }
}

@Composable
fun LoadKajakDataScreen(
    pathState: MainViewModelState,
    modifier: Modifier = Modifier,
    navigateToPathOverview: () -> Unit,
    onError: () -> Unit
) {
    Log.d("---Screen---", "State changed to $pathState")
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (pathState) {
            MainViewModelState.Error -> {
                DownloadErrorRetry(onError)
            }

            MainViewModelState.LoadedAllDataSuccessfully -> navigateToPathOverview.invoke()

            is MainViewModelState.SuccessOverview, is MainViewModelState.SuccessDetail -> {
                navigateToPathOverview.invoke()
            }

            is MainViewModelState.Loading -> {
                showLoadingState(pathState.loadingItemNumber, pathState.itemCount)
            }

            else -> showLoadingState()
        }
    }
}

@Composable
fun showLoadingState(loadingNow: Int = -1, numberOfPaths: Int = -1) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.kayak_paddle))
        LottieAnimation(
            composition,
            iterations = LottieConstants.IterateForever,
            restartOnPlay = true,
            modifier = Modifier
                .height(240.dp)
                .fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.loading_data),
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .wrapContentSize()
        )
        if (numberOfPaths > 0) {
            Text(
                text = stringResource(R.string.loading_path_data, loadingNow, numberOfPaths),
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .wrapContentSize()
            )
        }
    }
}