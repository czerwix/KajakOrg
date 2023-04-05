package com.mobeedev.kajakorg.ui.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobeedev.kajakorg.common.extensions.isNotNull
import com.mobeedev.kajakorg.domain.error.onFailure
import com.mobeedev.kajakorg.domain.usecase.DeleteChecklistUseCase
import com.mobeedev.kajakorg.domain.usecase.GetChecklistUseCase
import com.mobeedev.kajakorg.domain.usecase.UpdateChecklistUseCase
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModelState.Error.doOnEdit
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModelState.Loading.doOnSuccess
import com.mobeedev.kajakorg.ui.model.ChecklistItem
import com.mobeedev.kajakorg.ui.model.ChecklistSettingsMenu
import com.mobeedev.kajakorg.ui.model.ChecklistValueItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ChecklistViewModel(
    application: Application,
    val getChecklistUseCase: GetChecklistUseCase,
    val updateChecklistUseCase: UpdateChecklistUseCase,
    val deleteChecklistUseCase: DeleteChecklistUseCase
) : AndroidViewModel(application) {

    private val _uiState: MutableStateFlow<ChecklistViewModelState> =
        MutableStateFlow(ChecklistViewModelState.InitialStart)
    val uiState: StateFlow<ChecklistViewModelState> = _uiState

    private val editUpdateFlow: MutableStateFlow<ChecklistItem> = MutableStateFlow(ChecklistItem())
    private val saveEditUpdatesFlow = editUpdateFlow.debounce(timeoutMillis = 1000)

    init {
        getChecklistData()
    }

    private fun getChecklistData() {
        getChecklistUseCase.invoke { result ->
            result.onSuccess { checklistData ->
                _uiState.update { ChecklistViewModelState.Success(checklistData) }
            }.onFailure {
                _uiState.update { ChecklistViewModelState.Error }
            }
        }
    }

    fun onEditClicked(selectedID: UUID) {
        editUpdateFlow.update {
            (_uiState.value as? ChecklistViewModelState.Success)?.checklists
                ?.find { it.id == selectedID } ?: ChecklistItem()
        }
        _uiState.update { prev ->
            if (prev is ChecklistViewModelState.Success) {
                ChecklistViewModelState.Edit(editUpdateFlow.value, prev.checklists)
            } else {
                ChecklistViewModelState.Error
            }
        }

        viewModelScope.launch {
            startAutoSave()
        }
    }

    fun onOverviewShow() {
        //todo update checklist data
        _uiState.update { prev ->
            if (prev is ChecklistViewModelState.Edit) {
                ChecklistViewModelState.Success(prev.checklists)
            } else {
                ChecklistViewModelState.Error
            }
        }
        getChecklistData()
    }

    suspend fun startAutoSave() {//todo think about canceling this job
        saveEditUpdatesFlow.collect {
            updateChecklistUseCase.invoke(params = UpdateChecklistUseCase.Params(it))
        }
    }

    fun onTitleChanged(newTitle: String) {
        editUpdateFlow.update { prevValue ->
            prevValue.copy(title = newTitle)
        }
    }

    fun onDescriptionChanged(newDescription: String) {
        editUpdateFlow.update { prevValue ->
            prevValue.copy(description = newDescription)
        }
    }

    fun onAddItemClicked() {
        editUpdateFlow.update { prevValue ->
            prevValue.copy(
                checklist = prevValue.checklist.toMutableList().apply {
                    add(ChecklistValueItem())
                }
            )
        }
        _uiState.update { prevValue -> prevValue.doOnEdit { it.copy(editCheckList = editUpdateFlow.value) } }
    }

    fun onAddIteSeparatorClicked() {
        editUpdateFlow.update { prevValue ->
            prevValue.copy(
                checklist = prevValue.checklist.toMutableList().apply {
                    add(ChecklistValueItem(value = ChecklistValueItem.SEPARATOR_VALUE))
                }
            )
        }
        _uiState.update { prevValue -> prevValue.doOnEdit { it.copy(editCheckList = editUpdateFlow.value) } }
    }

    fun onDeleteValueItem(deleteAt: Int) {
        editUpdateFlow.update { prevValue ->
            prevValue.copy(
                checklist = prevValue.checklist.toMutableList().apply {
                    remove(prevValue.checklist[deleteAt])
                    if (lastOrNull()?.value == ChecklistValueItem.SEPARATOR_VALUE) {
                        removeLast()
                    }
                }
            )
        }
        _uiState.update { prevValue -> prevValue.doOnEdit { it.copy(editCheckList = editUpdateFlow.value) } }
    }

    fun onValueItemCheck(index: Int, isChecked: Boolean) {
        editUpdateFlow.update { prevValue ->
            prevValue.copy(
                checklist = prevValue.checklist.toMutableList().apply {
                    this[index] = this[index].copy(isDone = isChecked)
                }
            )
        }
        _uiState.update { prevValue -> prevValue.doOnEdit { it.copy(editCheckList = editUpdateFlow.value) } }
    }

    fun onChecklistValueTextChange(index: Int, text: String) {
        editUpdateFlow.update { prevValue ->
            prevValue.copy(
                checklist = prevValue.checklist.toMutableList().apply {
                    this[index] = this[index].copy(value = text)
                }
            )
        }
    }

    fun onDeleteChecklist(id: UUID) {
        _uiState.value.doOnSuccess { state ->
            val checklistDeleted = state.checklists.find { it.id == id }
            if (checklistDeleted.isNotNull()) {
                deleteChecklistUseCase.invoke(
                    params = DeleteChecklistUseCase.Params(
                        checklistDeleted!!
                    )
                ) { result ->
                    result.onSuccess {
                        getChecklistData()
                    }
                }
            }
        }
    }

    fun onCheckSetting(checkSettingSelected: ChecklistSettingsMenu) {
        if (checkSettingSelected == ChecklistSettingsMenu.CheckAll) {
            editUpdateFlow.update { prevValue ->
                prevValue.copy(
                    checklist = prevValue.checklist.map { it.copy(isDone = true) }
                )
            }
        } else {
            editUpdateFlow.update { prevValue ->
                prevValue.copy(
                    checklist = prevValue.checklist.map { it.copy(isDone = false) }
                )
            }
        }
        _uiState.update { prevValue ->
            prevValue.doOnEdit {
                val newState = it.copy(editCheckList = editUpdateFlow.value)
                updateChecklistUseCase.invoke(params = UpdateChecklistUseCase.Params(newState.editCheckList))

                newState
            }
        }
    }
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

    fun ChecklistViewModelState.doOnEdit(block: (Edit) -> ChecklistViewModelState?): ChecklistViewModelState {
        var newState = this
        if (this is Edit) {
            newState = block(this) ?: newState
        }
        return newState
    }

    fun ChecklistViewModelState.doOnSuccess(block: (Success) -> Unit): Unit {
        if (this is Success) {
            block(this)
        }
    }
}