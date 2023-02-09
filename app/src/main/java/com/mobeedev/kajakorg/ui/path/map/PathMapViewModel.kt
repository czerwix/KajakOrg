package com.mobeedev.kajakorg.ui.path.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.mobeedev.kajakorg.domain.usecase.GetLocalMapPathsUseCase
import com.mobeedev.kajakorg.ui.model.PathMapItem
import com.mobeedev.kajakorg.ui.navigation.PathMapArgs
import com.mobeedev.kajakorg.ui.navigation.pathDetailsIdArg
import com.mobeedev.kajakorg.ui.navigation.pathMapIdArg
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PathMapViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getLocalMapPathsUseCase: GetLocalMapPathsUseCase
    //todo add usecase to retrieve all paths and only GPS location list of events/sections.
) : AndroidViewModel(application) {

    private val pathArgs = PathMapArgs(checkNotNull(savedStateHandle[pathMapIdArg]))

    private val _uiState: MutableStateFlow<PathMapViewModelState> =
        MutableStateFlow(PathMapViewModelState.InitialStart(pathArgs.pathId))
    val uiState: StateFlow<PathMapViewModelState> = _uiState

    init {
        getPathData()
    }

    private fun getPathData() {
        with(_uiState.value) {
            var argsPathId =
                if (this is PathMapViewModelState.InitialStart && this.pathId >= 0) this.pathId else null

            _uiState.update { PathMapViewModelState.Loading }
            getLocalMapPathsUseCase.invoke(params = GetLocalMapPathsUseCase.Params(argsPathId)) { resul ->
                resul.onSuccess { pathItem ->
                    _uiState.update { PathMapViewModelState.Success(pathItem) }
                }.onFailure {
                    _uiState.update { PathMapViewModelState.Error }
                }
            }
        }
    }
    //todo add filtering etc. to path list.
}

sealed interface PathMapViewModelState {
    data class InitialStart(val pathId: Int) : PathMapViewModelState
    object Loading : PathMapViewModelState
    data class Success(val pathOverviewList: List<PathMapItem>) : PathMapViewModelState
    object Error : PathMapViewModelState
}