package com.mobeedev.kajakorg.ui.model

import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.PathEvent
import com.mobeedev.kajakorg.domain.model.detail.Section

data class PathItem(
    val overview: PathOveriewItem,
    var pathSectionsEvents:List<PathEvent>
)


