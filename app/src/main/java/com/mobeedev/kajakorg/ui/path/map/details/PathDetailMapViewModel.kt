package com.mobeedev.kajakorg.ui.path.map.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.google.android.gms.maps.model.CameraPosition
import com.mobeedev.kajakorg.common.extensions.isNotNull
import com.mobeedev.kajakorg.data.db.PathMapDetailScreenState
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathDetailsUseCase
import com.mobeedev.kajakorg.domain.usecase.SaveMapDetailsStateUseCase
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.navigation.PathMapArgs
import com.mobeedev.kajakorg.ui.navigation.pathMapIdArg
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PathDetailMapViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getPathDetailsUseCase: GetLocalPathDetailsUseCase,
    private val saveMapDetailsBoundsUseCase: SaveMapDetailsStateUseCase,
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
            if (pathArgs.isNotNull())

                _uiState.update { PathDetailsMapViewModelState.Loading }
            getPathDetailsUseCase.invoke(params = GetLocalPathDetailsUseCase.Params(pathArgs.pathId)) { resul ->
                resul.onSuccess { pathSavedState ->
                    _uiState.update {
                        PathDetailsMapViewModelState.Success(
                            pathSavedState.first,
                            pathSavedState.second
                        )
                    }
                }.onFailure {
                    _uiState.update { PathDetailsMapViewModelState.Error }
                }
            }
        }
    }

    fun saveScreenState(
        position: CameraPosition,
        currentPage: Int,
        bottomLayoutVisibility: Boolean,
        pathId: Int
    ) {
        saveMapDetailsBoundsUseCase.invoke(
            params = SaveMapDetailsStateUseCase.Params(
                position,
                currentPage,
                bottomLayoutVisibility,
                pathId
            )
        )
    }
    //todo add filtering etc. to path list.
}

sealed interface PathDetailsMapViewModelState {
    data class InitialStart(val pathId: Int) : PathDetailsMapViewModelState
    object Loading : PathDetailsMapViewModelState
    data class Success(val path: PathItem, val savedState: PathMapDetailScreenState? = null) :
        PathDetailsMapViewModelState

    object Error : PathDetailsMapViewModelState
}