package com.mobeedev.kajakorg.data.db

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.ui.model.ChecklistItem
import com.mobeedev.kajakorg.ui.model.ChecklistValueItem
import java.util.UUID

@Entity
data class CheckListDB(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val title: String = String.empty,
    val description: String,
    val checklist: List<ChecklistValueItem>,
    val color: Int
)

fun CheckListDB.toItem() = ChecklistItem(
    id = id,
    title = title,
    description = description,
    checklist = checklist,
    color = Color(color)
)

fun ChecklistItem.toDB() = CheckListDB(
    id = id,
    title = title,
    description = description,
    checklist = checklist,
    color = color.toArgb()
)