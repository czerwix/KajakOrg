package com.mobeedev.kajakorg.ui.path.map.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.mobeedev.kajakorg.domain.usecase.GetLocalMapPathsUseCase
import com.mobeedev.kajakorg.ui.model.PathMapItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PathMapViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getLocalMapPathsUseCase: GetLocalMapPathsUseCase
) : AndroidViewModel(application) {

    private val _uiState: MutableStateFlow<PathMapViewModelState> =
        MutableStateFlow(PathMapViewModelState.InitialStart)
    val uiState: StateFlow<PathMapViewModelState> = _uiState

    init {
        getPathData()
    }

    private fun getPathData() {
        _uiState.update { PathMapViewModelState.Loading }
        getLocalMapPathsUseCase.invoke(params = GetLocalMapPathsUseCase.Params(null)) { resul ->
            resul.onSuccess { pathItem ->
                _uiState.update { PathMapViewModelState.Success(pathItem) }
            }.onFailure {
                _uiState.update { PathMapViewModelState.Error }
            }
        }
    }

    fun onPathSelected(path: PathMapItem?) {
        _uiState.update { prev ->
            if (prev is PathMapViewModelState.Success) {
                prev.copy(selectedPath = path, lastSelectedPath = prev.selectedPath)
            } else {
                PathMapViewModelState.Error
            }
        }
    }
}

sealed interface PathMapViewModelState {
    object InitialStart : PathMapViewModelState
    object Loading : PathMapViewModelState
    data class Success(
        val pathOverviewList: List<PathMapItem>,
        val selectedPath: PathMapItem? = null,
        val lastSelectedPath: PathMapItem? = null
    ) : PathMapViewModelState

    object Error : PathMapViewModelState
}