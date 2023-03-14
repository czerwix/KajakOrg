package com.mobeedev.kajakorg.ui.path.details

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.designsystem.path.PathDescriptionElement
import com.mobeedev.kajakorg.designsystem.path.PathEventWithMapElement
import com.mobeedev.kajakorg.designsystem.path.PathSectionElement
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.PathEvent
import com.mobeedev.kajakorg.domain.model.detail.Section

@Composable
fun PathDetailsList(
    pathList: List<PathEvent>,
    description: String? = null,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        pathDetailsCards(pathList, description)
    }
}

fun LazyListScope.pathDetailsCards(
    pathList: List<PathEvent>,
    description: String? = null
) {//todo add animations to list change with Modifier.animateItemPlacement() sent to each item in list
    item {//todo replace with separators here :D
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
    }
    if (description != null) {
        item(key = "PathDescriptionListKey") {
            PathDescriptionElement(description)
        }
    }
    pathList.forEach { element ->
        if (element is Section) {
            item(key = element.hashCode()) {
                PathSectionElement(item = element, onClick = {})//todo add onclick here
                Divider(thickness = 10.dp, color = Color.Transparent)
            }
            items(items = element.events, key = { it.hashCode() }) { nestedSectionEvent ->
                PathEventWithMapElement(item = nestedSectionEvent, onClick = {})
                Divider(thickness = 10.dp, color = Color.Transparent)
            }
        } else if (element is Event) {
            item(key = element.hashCode()) {
                PathEventWithMapElement(item = element, onClick = {})
                Divider(thickness = 10.dp, color = Color.Transparent)
            }
        }
    }
    item {
        Box(//todo replace with separators here :D
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
    }
}
