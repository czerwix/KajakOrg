package com.mobeedev.kajakorg.ui.path.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.designsystem.component.DownloadErrorRetry
import com.mobeedev.kajakorg.designsystem.component.PathOverViewElement
import com.mobeedev.kajakorg.ui.MainViewModel
import com.mobeedev.kajakorg.ui.MainViewModelState
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun PathOverviewRoute(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = getViewModel(),
    navigateToPathDetails: (Int) -> Unit
) {
    val uiState: MainViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    PathOverviewScreen(
        pathState = uiState,
        modifier = modifier,
        navigateToPathDetails = navigateToPathDetails
    )
}


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun PathOverviewScreen(
    pathState: MainViewModelState,
    modifier: Modifier = Modifier,
    navigateToPathDetails: (Int) -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (pathState) {
            MainViewModelState.Error -> DownloadErrorRetry({})
            MainViewModelState.InitialStart -> TODO()
            is MainViewModelState.Loading -> TODO()
            is MainViewModelState.Success -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    pathCards(pathState.pathList, navigateToPathDetails)
                }
            }
        }
    }
}

private fun LazyListScope.pathCards(
    pathList: List<PathDto>,
    navigateToPathDetails: (Int) -> Unit
) {
    items(pathList){item->
//       PathOverViewElement(item)
    }
}

//@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
//@Composable
//fun PathOverViewPopulated(){
//    KajakOrgTheme {
//
//    }
//}

