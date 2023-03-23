package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.model.DataDownloadStatus
import com.mobeedev.kajakorg.domain.repository.ChecklistRepository
import com.mobeedev.kajakorg.domain.repository.KayakPathRepository
import com.mobeedev.kajakorg.domain.usecase.comon.NoParametersUseCase
import com.mobeedev.kajakorg.ui.model.ChecklistItem

class GetChecklistUseCase(private val checklistRepository: ChecklistRepository) :
    NoParametersUseCase<List<ChecklistItem>>() {

    override suspend fun run(): Result<List<ChecklistItem>> = checklistRepository.getAllChecklists()
}