package com.mobeedev.kajakorg.domain.usecase

import com.mobeedev.kajakorg.domain.repository.ChecklistRepository
import com.mobeedev.kajakorg.domain.usecase.comon.UseCase
import com.mobeedev.kajakorg.ui.model.ChecklistItem

class DeleteChecklistUseCase(
    private val checklistRepository: ChecklistRepository
) : UseCase<Unit, DeleteChecklistUseCase.Params>() {

    override suspend fun run(params: Params): Result<Unit> =
        checklistRepository.deleteChecklists(params.checklistItem)

    data class Params(val checklistItem: ChecklistItem)
}