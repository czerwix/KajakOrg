package com.mobeedev.kajakorg.ui.checklist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobeedev.kajakorg.R
import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.designsystem.checklist.ChecklistOverviewElement
import com.mobeedev.kajakorg.designsystem.theme.White
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import org.koin.androidx.compose.getViewModel
import java.util.UUID

@Composable
fun ChecklistRoute(
    modifier: Modifier = Modifier,
    viewModel: ChecklistViewModel = getViewModel(),
    navigateToChecklistEdit: () -> Unit
) {
    val uiState: ChecklistViewModelState by viewModel.uiState.collectAsStateWithLifecycle()

    ChecklistScreen(
        uiState = uiState,
        viewModel = viewModel,
        navigateToChecklistEdit = navigateToChecklistEdit,
        modifier = modifier
    )

}

@Composable
fun ChecklistScreen(
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

@Composable
fun showChecklistList(
    uiState: ChecklistViewModelState.Success,
    viewModel: ChecklistViewModel,
    navigateToChecklistEdit: (UUID) -> Unit,
    modifier: Modifier
) {
    Box {
        //Maybe Add some kind of top bar?
        //checklist List
        val data = remember { mutableStateOf(uiState.checklists) }
        val state = rememberReorderableLazyListState(onMove = { from, to ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        })
        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .reorderable(state)
                .detectReorderAfterLongPress(state)
        ) {
            items(data.value.size, { data.value[it].id }) { index ->
                ReorderableItem(state, key = data.value[index].id, index = index) { isDragging ->
                    val elevation =
                        animateDpAsState(if (isDragging) 16.dp else 0.dp, label = String.empty)
                    val itemModifier = Modifier
                        .shadow(elevation.value)
                    ChecklistOverviewElement(data.value[index], navigateToChecklistEdit)
                }
            }
            item {
                //todo add the empty element
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
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_add_24),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }
    }
}
