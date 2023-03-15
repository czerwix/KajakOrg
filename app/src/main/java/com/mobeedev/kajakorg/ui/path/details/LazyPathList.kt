package com.mobeedev.kajakorg.ui.path.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.designsystem.path.PathDescriptionElement
import com.mobeedev.kajakorg.designsystem.path.PathEventWithMapElement
import com.mobeedev.kajakorg.designsystem.path.PathSectionElement
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.PathEvent
import com.mobeedev.kajakorg.domain.model.detail.Section
import com.mobeedev.kajakorg.domain.model.detail.getSections

@Composable
fun PathDetailsList(
    pathList: List<PathEvent>,
    description: String? = null,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val collapsedSectionState: SnapshotStateList<Pair<Int, Boolean>> = remember {
        pathList.getSections().mapNotNull { section -> section?.let { it.id to true } }
            .toMutableStateList()
    }

    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        pathDetailsCards(pathList, description, collapsedSectionState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.pathDetailsCards(
    pathList: List<PathEvent>,
    description: String? = null,
    collapsedSectionState: SnapshotStateList<Pair<Int, Boolean>>
) {//todo add animations to list change with Modifier.animateItemPlacement() sent to each item in list
    item {//todo replace with separators here :D
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .animateItemPlacement()
        )
    }
    if (description != null) {
        item(key = "PathDescriptionListKey") {
            PathDescriptionElement(description, modifier = Modifier.animateItemPlacement())
        }
    }
    pathList.forEach { element ->
        if (element is Section) {
            item(key = element.hashCode()) {
                PathSectionElement(
                    item = element,
                    onClick = {
                        val sectionIndex =
                            collapsedSectionState.indexOfFirst { it.first == element.id }
                        val currentValue =
                            collapsedSectionState[collapsedSectionState.indexOfFirst { it.first == element.id }]
                        collapsedSectionState[sectionIndex] =
                            currentValue.copy(second = currentValue.second.not())
                    },
                    modifier = Modifier.animateItemPlacement()
                )
                Divider(
                    thickness = 10.dp,
                    color = Color.Transparent,
                    modifier = Modifier.animateItemPlacement()
                )
            }
            if (collapsedSectionState.find { it.first == element.id }?.second != true || collapsedSectionState.size == 1) {
                items(items = element.events,
                    key = { it.hashCode() }) { nestedSectionEvent ->
                    PathEventWithMapElement(
                        item = nestedSectionEvent,
                        onClick = {},
                        modifier = Modifier.animateItemPlacement()
                    )
                    Divider(
                        thickness = 10.dp,
                        color = Color.Transparent,
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        } else if (element is Event) {
            item(key = element.hashCode()) {
                PathEventWithMapElement(
                    item = element,
                    onClick = {},
                    modifier = Modifier.animateItemPlacement()
                )
                Divider(
                    thickness = 10.dp,
                    color = Color.Transparent,
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
    item {
        Box(//todo replace with separators here :D
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .animateItemPlacement()
        )
    }
}
