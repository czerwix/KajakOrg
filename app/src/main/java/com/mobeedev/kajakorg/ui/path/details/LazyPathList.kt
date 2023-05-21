package com.mobeedev.kajakorg.ui.path.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.designsystem.path.PathDescriptionElement
import com.mobeedev.kajakorg.designsystem.path.PathEventWithMapSimpler
import com.mobeedev.kajakorg.designsystem.path.PathSectionElement
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.Section
import com.mobeedev.kajakorg.ui.model.GoogleMapsStatusItem
import com.mobeedev.kajakorg.ui.model.PathItem

@Composable
fun PathDetailsList(
    pathData: PathItem,
    collapsedState: SnapshotStateList<Pair<Int, Boolean>>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    googleMapVisibilityState: GoogleMapsStatusItem,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        pathDetailsCards(pathData, collapsedState, googleMapVisibilityState)
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.pathDetailsCards(
    pathData: PathItem,
    collapsedSectionState: SnapshotStateList<Pair<Int, Boolean>>,
    googleMapVisibilityState: GoogleMapsStatusItem
) {//todo add animations to list change with Modifier.animateItemPlacement() sent to each item in list
    item(key = "TopPadding") {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .animateItemPlacement()
        )
    }

    item(key = "PathDescriptionListKey") {
        PathDescriptionElement(pathData, modifier = Modifier.animateItemPlacement())
    }

    pathData.pathSectionsEvents.forEach { element ->
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
                    modifier = Modifier.animateItemPlacement(),
                    isSectionCollapsed = collapsedSectionState
                        .find { it.first == element.id }?.second
                        ?: true,
                    shouldShowExpandToolbar = collapsedSectionState.size > 1
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
                    PathEventWithMapSimpler(
                        item = nestedSectionEvent,
                        onClick = {},
                        googleMapVisibilityState,
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
                PathEventWithMapSimpler(
                    item = element,
                    onClick = {},
                    googleMapVisibilityState = googleMapVisibilityState,
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
    item(key = "BottomPadding") {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .animateItemPlacement()
        )
    }
}
