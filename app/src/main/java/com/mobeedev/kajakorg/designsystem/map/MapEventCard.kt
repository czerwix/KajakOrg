package com.mobeedev.kajakorg.designsystem.map

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.mobeedev.kajakorg.designsystem.path.PathEventWithoutMapElement
import com.mobeedev.kajakorg.designsystem.path.PathSectionElement
import com.mobeedev.kajakorg.designsystem.path.PathSectionSimpleElement
import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.PathEvent
import com.mobeedev.kajakorg.domain.model.detail.Section

@Composable
fun ColumnScope.MapEventCard(pathEvent: PathEvent) {
    if (pathEvent is Section) {
        PathSectionSimpleElement(pathEvent)
    } else if (pathEvent is Event) {
        PathEventWithoutMapElement(pathEvent)
    }
}