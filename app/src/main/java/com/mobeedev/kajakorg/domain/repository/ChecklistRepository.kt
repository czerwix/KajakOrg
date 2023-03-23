package com.mobeedev.kajakorg.domain.repository

import com.mobeedev.kajakorg.ui.model.ChecklistItem

interface ChecklistRepository {
    suspend fun getAllChecklists(): Result<List<ChecklistItem>>
    suspend fun updateChecklists(checklistItem: ChecklistItem): Result<Unit>
}