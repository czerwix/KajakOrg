package com.mobeedev.kajakorg.ui.path.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.designsystem.component.PathOverViewElement
import com.mobeedev.kajakorg.domain.model.overview.PathOverview
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun PathOverviewRoute(
    modifier: Modifier = Modifier,
    viewModel: PathOverViewViewModel = getViewModel(),
    navigateToPathDetails: (Int) -> Unit
) {
    val uiState: PathOverViewViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    PathOverviewScreen(
        uiState = uiState,
        modifier = modifier,
        navigateToPathDetails = navigateToPathDetails
    )
}


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun PathOverviewScreen(
    uiState: PathOverViewViewModelState,
    modifier: Modifier = Modifier,
    navigateToPathDetails: (Int) -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            PathOverViewViewModelState.Error -> TODO()
            PathOverViewViewModelState.InitialStart, PathOverViewViewModelState.Loading -> Unit
            is PathOverViewViewModelState.PartialOverviewData -> Unit
            is PathOverViewViewModelState.Success -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    pathCards(uiState.pathOverviewList, navigateToPathDetails)
                }
            }
        }
    }
}

private fun LazyListScope.pathCards(
    pathList: List<PathOverview>,
    navigateToPathDetails: (Int) -> Unit
) {
    items(pathList.size) { item ->
       PathOverViewElement(item)
    }
}

//@Preview(name = "phone", device = "spec:shape=Normal,width=360,height=640,unit=dp,dpi=480")
//@Composable
//fun PathOverViewPopulated(){
//    KajakOrgTheme {
//
//    }
//}

