package com.mobeedev.kajakorg.ui.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mobeedev.kajakorg.domain.error.onFailure
import com.mobeedev.kajakorg.domain.usecase.GetChecklistUseCase
import com.mobeedev.kajakorg.domain.usecase.UpdateChecklistUseCase
import com.mobeedev.kajakorg.ui.model.ChecklistItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class ChecklistViewModel(
    application: Application,
    val getChecklistUseCase: GetChecklistUseCase,
    val updateChecklistUseCase: UpdateChecklistUseCase
) : AndroidViewModel(application) {

    private val _uiState: MutableStateFlow<ChecklistViewModelState> =
        MutableStateFlow(ChecklistViewModelState.InitialStart)
    val uiState: StateFlow<ChecklistViewModelState> = _uiState

    init {
        getChecklistData()
    }

    private fun getChecklistData() {
        _uiState.update { ChecklistViewModelState.Loading }
        getChecklistUseCase.invoke { result ->
            result.onSuccess { checklistData ->
                _uiState.update { ChecklistViewModelState.Success(checklistData) }
            }.onFailure {
                _uiState.update { ChecklistViewModelState.Error }
            }
        }
    }

    fun onEditClicked(selectedID: UUID) {
        _uiState.update { prev ->
            if (prev is ChecklistViewModelState.Success) {
                ChecklistViewModelState.Edit(
                    prev.checklists.find { it.id == selectedID } ?: ChecklistItem(),
                    prev.checklists)
            } else {
                ChecklistViewModelState.Error
            }
        }
    }

    fun onOverviewShow() {
        _uiState.update { prev ->
            if (prev is ChecklistViewModelState.Edit) {
                ChecklistViewModelState.Success(prev.checklists)
            } else {
                ChecklistViewModelState.Error
            }
        }
    }
    //todo create the rest of functionality for updating etc
}

sealed interface ChecklistViewModelState {
    object InitialStart : ChecklistViewModelState
    object Loading : ChecklistViewModelState
    data class Success(val checklists: List<ChecklistItem>) : ChecklistViewModelState
    data class Edit(
        val editCheckList: ChecklistItem = ChecklistItem(),
        val checklists: List<ChecklistItem>
    ) : ChecklistViewModelState

    object Error : ChecklistViewModelState
}