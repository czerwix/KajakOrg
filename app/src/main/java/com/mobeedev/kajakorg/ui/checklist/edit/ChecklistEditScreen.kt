package com.mobeedev.kajakorg.ui.checklist.edit

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistAddItem
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistDescriptionItem
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistEditItem
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistTitleItem
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModel
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModelState
import org.koin.androidx.compose.getViewModel

@Composable
fun ChecklistEditRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistViewModel = getViewModel(),
) {
    val uiState: ChecklistViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    ChecklistEditScreen(
        uiState = uiState,
        viewModel = viewModel,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun ChecklistEditScreen(
    uiState: ChecklistViewModelState,
    viewModel: ChecklistViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier
) {
    when (uiState) {
        ChecklistViewModelState.Error -> Unit //todo
        ChecklistViewModelState.InitialStart, ChecklistViewModelState.Loading -> Unit // TODO

        is ChecklistViewModelState.Success -> {
            onBackClick()
            Log.d(
                "ChecklistEditScreen Error",
                "ChecklistEditScreen requires Edit State! Found: Success."
            )
        }

        is ChecklistViewModelState.Edit -> showChecklistEditScreen(
            uiState = uiState,
            viewModel = viewModel,
            onBackClick = onBackClick,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun showChecklistEditScreen(
    uiState: ChecklistViewModelState.Edit,
    viewModel: ChecklistViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier
) {
    Column {
        //navigation/icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 16.dp, top = 16.dp)
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }

        //Edit checklist
        val checklistValueList = uiState.editCheckList.checklist
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .focusTarget()
        ) {
            item(key = "TopPadding") {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .animateItemPlacement()
                )
            }
            //Title Edit
            item(key = "Title") {
                ChecklistTitleItem(
                    uiState.editCheckList, {
                        viewModel.onTitleChanged(it)
                    },
                    modifier = Modifier.animateItemPlacement()
//                        isCustomColorSelected = //todo add color when is selected
                )
            }
            //Note Edit
            item(key = "Note") {
                ChecklistDescriptionItem(
                    uiState.editCheckList, {
                        viewModel.onDescriptionChanged(it)
                    }, modifier = Modifier.animateItemPlacement()
//                        isCustomColorSelected = //todo add color when is selected
                )
            }
            //checklist items
            items(
                count = checklistValueList.size,
                key = { checklistValueList[it].id }) { index ->
                ChecklistEditItem(
                    item = checklistValueList[index],
                    index = index,
                    onCheckListener = { index, isChecked ->
                        viewModel.onValueItemCheck(index, isChecked)
                    },
                    onTextChange = { index, text ->
                        viewModel.onChecklistValueTextChange(index, text)
                    },
                    onDeleteClicked = {
                        viewModel.onDeleteValueItem(it)
                    },
                    modifier = Modifier.animateItemPlacement()
//                            isCustomColorSelected //todo add color when is selected
                )
            }
            //Add checkList item
            item(key = "AddItem") {
                ChecklistAddItem(
                    {
                        viewModel.onAddItemClicked()
                    },
                    modifier = Modifier.animateItemPlacement(),
//                    isCustomColorSelected = //todo add color when is selected
                )
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

