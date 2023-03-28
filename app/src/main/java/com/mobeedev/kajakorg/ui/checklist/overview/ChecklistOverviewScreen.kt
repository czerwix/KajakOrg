package com.mobeedev.kajakorg.ui.checklist.overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistOverviewElement
import com.mobeedev.kajakorg.designsystem.theme.White
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModel
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModelState
import org.koin.androidx.compose.getViewModel
import java.util.UUID

@Composable
fun ChecklistOverviewRoute(
    modifier: Modifier = Modifier,
    viewModel: ChecklistViewModel = getViewModel(),
    navigateToChecklistEdit: () -> Unit
) {
    val uiState: ChecklistViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    ChecklistOverviewScreen(
        uiState = uiState,
        viewModel = viewModel,
        navigateToChecklistEdit = navigateToChecklistEdit,
        modifier = modifier
    )
}

@Composable
fun ChecklistOverviewScreen(
    uiState: ChecklistViewModelState,
    viewModel: ChecklistViewModel,
    navigateToChecklistEdit: () -> Unit,
    modifier: Modifier
) {
    when (uiState) {
        ChecklistViewModelState.Error -> Unit //todo
        ChecklistViewModelState.InitialStart, ChecklistViewModelState.Loading -> Unit // TODO

        is ChecklistViewModelState.Success -> showChecklistList(
            uiState = uiState,
            viewModel = viewModel,
            navigateToChecklistEdit = {
                viewModel.onEditClicked(it)
                navigateToChecklistEdit()
            },
            modifier = modifier
        )

        is ChecklistViewModelState.Edit -> viewModel.onOverviewShow()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun showChecklistList(
    uiState: ChecklistViewModelState.Success,
    viewModel: ChecklistViewModel,
    navigateToChecklistEdit: (UUID) -> Unit,
    modifier: Modifier
) {
    Column {
        Surface(
            shadowElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = stringResource(id = R.string.checklist_overview_title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                )
            }
        }

        val data = uiState.checklists
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .focusTarget(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(key = "TopPadding") {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .animateItemPlacement()
                )
            }
            items(data.size, { data[it].id }) { index ->
                ChecklistOverviewElement(
                    checklistItem = data[index],
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .animateItemPlacement(),
                    onDeleteClick = {
                        viewModel.onDeleteChecklist(it)
                    },
                    onClick = navigateToChecklistEdit
                )
            }
            item("AddButton") {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .animateItemPlacement()
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors(White),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                            .clickable {
                                navigateToChecklistEdit(UUID.randomUUID())
                            }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_add_24),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                                .size(48.dp)
                                .padding(start = 8.dp, end = 8.dp)
                        )
                    }
                }
            }
            item(key = "BottomPadding") {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .animateItemPlacement()
                )
            }
        }
    }
}
