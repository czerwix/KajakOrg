package com.mobeedev.kajakorg.ui.path.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobeedev.kajakorg.designsystem.path.PathEventWithMapElement
import com.mobeedev.kajakorg.designsystem.path.PathSectionElement
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.PathEvent
import com.mobeedev.kajakorg.domain.model.detail.Section

@Composable
fun PathDetailsList(
    pathList: List<PathEvent>,
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
        pathDetailsCards(pathList)
    }
}

fun LazyListScope.pathDetailsCards(
    pathList: List<PathEvent>,
) {
    item {//todo replace with separators here :D
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
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
