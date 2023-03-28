package com.mobeedev.kajakorg.ui.model

import androidx.compose.ui.graphics.Color
import com.mobeedev.kajakorg.common.extensions.empty
import java.util.UUID

data class ChecklistItem(//todo create separate models for domain layer
    val id: UUID = UUID.randomUUID(),
    val title: String = String.empty,
    val description: String = String.empty,
    val checklist: List<ChecklistValueItem> = listOf(),
    val color: Color = Color.Unspecified
)