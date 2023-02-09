package com.mobeedev.kajakorg.ui.path.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.ui.model.PathSortOrderItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun getPathSearchTopBar(
    uiState: PathOverviewViewModelState.Success,
    viewModel: PathOverviewViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    onMapClicked: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isSortMenuVisible by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(title = {},
        navigationIcon = {},
        scrollBehavior = scrollBehavior,
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp)
                        .background(Color.Transparent)
                        .weight(1f),
                    value = uiState.textFilter,
                    onValueChange = { viewModel.filterByName(it) },
                    placeholder = {
                        Text(text = stringResource(id = R.string.path_search))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black,
                        containerColor = Color.White
                    ),
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = uiState.textFilter.isNotBlank(),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            IconButton(onClick = { viewModel.onFilterCleared() }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    null
                                )
                            }
                        }
                    },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        focusManager.moveFocus(FocusDirection.Exit)
                    }),
                )

                IconButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = onMapClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_map_24),
                        null
                    )
                }

                Box(modifier = Modifier.wrapContentSize()) {
                    IconButton(
                        modifier = Modifier.wrapContentSize(),
                        onClick = {
                            isSortMenuVisible = true
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.sort_icon),
                            null
                        )
                    }
                    showPathSortMenu(
                        uiState,
                        isSortMenuVisible,
                        {
                            viewModel.onSortOrderClicked(it)
                            isSortMenuVisible = false
                        },
                        { isSortMenuVisible = false }
                    )
                }
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun showPathSortMenu(
    uiState: PathOverviewViewModelState.Success,
    isSortMenuVisible: Boolean,
    onSelected: (PathSortOrderItem) -> Unit,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        expanded = isSortMenuVisible,
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
                if (uiState.sortOrder == PathSortOrderItem.AToZ) {
                    Icon(
                        painter = painterResource(id = R.drawable.done_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        tint = Color.Black
                    )
                }
                Text(
                    text = stringResource(id = R.string.path_sort_a_z),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onClick = {
                onSelected(PathSortOrderItem.AToZ)
            },
            enabled = uiState.sortOrder != PathSortOrderItem.AToZ,
            modifier = Modifier.wrapContentSize()
        )
        DropdownMenuItem(
            text = {
                if (uiState.sortOrder == PathSortOrderItem.ZToA) {
                    Icon(
                        painter = painterResource(id = R.drawable.done_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        tint = Color.Black
                    )
                }
                Text(
                    text = stringResource(id = R.string.path_sort_z_a),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onClick = { onSelected(PathSortOrderItem.ZToA) },
            enabled = uiState.sortOrder != PathSortOrderItem.ZToA,
            modifier = Modifier.wrapContentSize()
        )
        DropdownMenuItem(
            text = {
                if (uiState.sortOrder == PathSortOrderItem.Longest) {
                    Icon(
                        painter = painterResource(id = R.drawable.done_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        tint = Color.Black
                    )
                }
                Text(
                    text = stringResource(id = R.string.path_sort_longest_shortest),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
            },
            onClick = { onSelected(PathSortOrderItem.Longest) },
            enabled = uiState.sortOrder != PathSortOrderItem.Longest,
            modifier = Modifier.wrapContentSize()
        )
        DropdownMenuItem(
            text = {
                if (uiState.sortOrder == PathSortOrderItem.Shortest) {
                    Icon(
                        painter = painterResource(id = R.drawable.done_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        tint = Color.Black
                    )
                }
                Text(
                    text = stringResource(id = R.string.path_sort_shortest_longest),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
            },
            onClick = { onSelected(PathSortOrderItem.Shortest) },
            enabled = uiState.sortOrder != PathSortOrderItem.Shortest,
            modifier = Modifier.wrapContentSize()
        )
    }
}
