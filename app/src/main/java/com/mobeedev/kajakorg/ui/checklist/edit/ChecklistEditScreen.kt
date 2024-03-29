package com.mobeedev.kajakorg.ui.checklist.edit

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistAddItem
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistDescriptionItem
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistEditItem
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistSeparatorElement
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistTitleItem
import com.mobeedev.kajakorg.designsystem.scrollToOnFocus
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModel
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModelState
import com.mobeedev.kajakorg.ui.model.ChecklistSettingsMenu
import com.mobeedev.kajakorg.ui.model.ChecklistValueItem
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
        var isCheckListMenuVisible by remember { mutableStateOf(false) }
        var isDeletePopupVisible by remember { mutableStateOf(false) }
        var deleteIndex by remember { mutableStateOf(-1) }

        val checklistValueList = uiState.editCheckList.checklist
        val focusManager = LocalFocusManager.current
        //navigation/icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            //back button
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
            //fill empty space
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            //settings button
            Box(modifier = Modifier.wrapContentSize()) {
                IconButton(
                    onClick = {
                        isCheckListMenuVisible = true
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 16.dp, top = 16.dp)
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.round_more_vert_24),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                showChecklistMenu(
                    isCheckListMenuVisible,
                    {
                        viewModel.onCheckSetting(it)
                        isCheckListMenuVisible = false
                    },
                    { isCheckListMenuVisible = false }
                )
            }
        }

        if (isDeletePopupVisible) {
            Popup(
                alignment = Alignment.CenterStart,
                onDismissRequest = { isDeletePopupVisible = false },
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(10.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    modifier = modifier
                        .wrapContentSize()
                        .padding(start = 8.dp, end = 8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(id = R.string.delete_confirmation),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(
                                    top = 32.dp, start = 16.dp, end = 16.dp
                                ),
                        )
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 16.dp)
                        ) {
                            Button(
                                onClick = {
                                    isDeletePopupVisible = false
                                },
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .height(40.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.delete_no),
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                )
                            }
                            Button(
                                onClick = {
                                    viewModel.onDeleteValueItem(deleteIndex)
                                    isDeletePopupVisible = false
                                },
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .height(40.dp)
                                    .padding(start = 16.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.delete_yes),
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        //Edit checklist
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
                        .scrollToOnFocus()
                )
            }
            //Title Edit
            item(key = "Title") {
                ChecklistTitleItem(
                    uiState.editCheckList, {
                        viewModel.onTitleChanged(it)
                    },
                    modifier = Modifier
                        .animateItemPlacement()
                        .scrollToOnFocus()
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
                val item = checklistValueList[index]
                if (item.value == ChecklistValueItem.SEPARATOR_VALUE) {
                    ChecklistSeparatorElement(
                        index = index,
                        onDeleteClicked = {
                            deleteIndex = it
                            isDeletePopupVisible = true
                        },
                        modifier = modifier
                            .animateItemPlacement()
                    )
                } else {
                    ChecklistEditItem(
                        item = item,
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
                        modifier = modifier
                            .animateItemPlacement()
                            .scrollToOnFocus(),
                        focusManager = focusManager
//                            isCustomColorSelected //todo add color when is selected
                    )

                }
            }
            //Add checkList item
            item(key = "AddItem") {
                ChecklistAddItem(
                    onAddClicked = {
                        viewModel.onAddItemClicked()
                    },
                    onAddSeparatorClicked = {
                        viewModel.onAddIteSeparatorClicked()
                    },
                    modifier = Modifier
                        .animateItemPlacement()
                        .scrollToOnFocus(),
                    focusManager = focusManager
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun showChecklistMenu(
    ischeckListMenuVisible: Boolean,
    onSelected: (ChecklistSettingsMenu) -> Unit,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        expanded = ischeckListMenuVisible,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .wrapContentSize()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(id = R.string.checklist_settings_check_all),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onClick = {
                onSelected(ChecklistSettingsMenu.CheckAll)
            },
            modifier = Modifier.wrapContentSize()
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(id = R.string.checklist_settings_uncheck_all),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onClick = { onSelected(ChecklistSettingsMenu.UncheckAll) },
            modifier = Modifier.wrapContentSize()
        )
    }
}

