package com.mobeedev.kajakorg.ui.path.map.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.mobeedev.kajakorg.common.extensions.isNotNull
import com.mobeedev.kajakorg.common.extensions.isNull
import com.mobeedev.kajakorg.domain.usecase.GetLocalMapPathsUseCase
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathDetailsUseCase
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.navigation.PathMapArgs
import com.mobeedev.kajakorg.ui.navigation.pathMapIdArg
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PathDetailMapViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getPathDetailsUseCase: GetLocalPathDetailsUseCase
) : AndroidViewModel(application) {

    private val pathArgs = PathMapArgs(checkNotNull(savedStateHandle[pathMapIdArg]))

    private val _uiState: MutableStateFlow<PathDetailsMapViewModelState> =
        MutableStateFlow(PathDetailsMapViewModelState.InitialStart(pathArgs.pathId))
    val uiState: StateFlow<PathDetailsMapViewModelState> = _uiState

    init {
        getPathData()
    }

    private fun getPathData() {
        with(_uiState.value) {
        if(pathArgs.isNotNull())

            _uiState.update { PathDetailsMapViewModelState.Loading }
            getPathDetailsUseCase.invoke(params = GetLocalPathDetailsUseCase.Params(pathArgs.pathId)) { resul ->
                resul.onSuccess { pathItem ->
                    _uiState.update { PathDetailsMapViewModelState.Success(pathItem) }
                }.onFailure {
                    _uiState.update { PathDetailsMapViewModelState.Error }
                }
            }
        }
    }
    //todo add filtering etc. to path list.
}

sealed interface PathDetailsMapViewModelState {
    data class InitialStart(val pathId: Int) : PathDetailsMapViewModelState
    object Loading : PathDetailsMapViewModelState
    data class Success(val path: PathItem) : PathDetailsMapViewModelState
    object Error : PathDetailsMapViewModelState
}