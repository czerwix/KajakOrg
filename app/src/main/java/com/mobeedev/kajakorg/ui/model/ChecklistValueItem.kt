package com.mobeedev.kajakorg.ui.model

import com.mobeedev.kajakorg.common.extensions.empty
import java.util.UUID

data class ChecklistValueItem(
    val id: UUID = UUID.randomUUID(),
    val isDone: Boolean = false,
    val value: String = String.empty
){
    companion object{
        const val SEPARATOR_VALUE = "---- ---- ----"
    }
}

