package com.mobeedev.kajakorg.ui.model

import com.mobeedev.kajakorg.domain.model.detail.Event
import com.mobeedev.kajakorg.domain.model.detail.Section

data class PathItem(
    val overview: PathOveriewItem,
    var sections: List<Section>,
    var events: List<Event>
)


