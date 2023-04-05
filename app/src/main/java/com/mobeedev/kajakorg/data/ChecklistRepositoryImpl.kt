package com.mobeedev.kajakorg.data

import com.mobeedev.kajakorg.data.datasource.local.ChecklistDao
import com.mobeedev.kajakorg.data.db.toDB
import com.mobeedev.kajakorg.data.db.toItem
import com.mobeedev.kajakorg.domain.error.runRecoverCatching
import com.mobeedev.kajakorg.domain.repository.ChecklistRepository
import com.mobeedev.kajakorg.ui.model.ChecklistItem

class ChecklistRepositoryImpl(val checklistDao: ChecklistDao) : ChecklistRepository {
    override suspend fun getAllChecklists(): Result<List<ChecklistItem>> = runRecoverCatching {
        checklistDao.getALlChecklists().map { it.toItem() }
    }

    override suspend fun updateChecklists(checklistItem: ChecklistItem): Result<Unit> =
        runRecoverCatching {
            checklistDao.upsertChecklistDB(checklistItem.toDB())
        }

    override suspend fun deleteChecklists(checklistItem: ChecklistItem): Result<Unit> =
        runRecoverCatching {
            checklistDao.deleteChecklist(checklistItem.toDB())
        }
}