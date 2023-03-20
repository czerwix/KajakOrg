package com.mobeedev.kajakorg.ui.path.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathDetailsUseCase
import com.mobeedev.kajakorg.ui.model.PathDetailsSettingsOrderItem
import com.mobeedev.kajakorg.ui.model.PathItem
import com.mobeedev.kajakorg.ui.navigation.PathDetailsArgs
import com.mobeedev.kajakorg.ui.navigation.pathDetailsIdArg
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PathDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val getPathDetailsUseCase: GetLocalPathDetailsUseCase
) : AndroidViewModel(application) {

    private val pathArgs = PathDetailsArgs(checkNotNull(savedStateHandle[pathDetailsIdArg]))

    private val _uiState: MutableStateFlow<PathDetailsViewModelState> =
        MutableStateFlow(PathDetailsViewModelState.InitialStart(pathArgs.pathId))
    val uiState: StateFlow<PathDetailsViewModelState> = _uiState

    init {
        getPathData()
    }

    private fun getPathData() {
        with(_uiState.value) {
            if (this !is PathDetailsViewModelState.InitialStart) return
            getPathDetailsUseCase.invoke(params = GetLocalPathDetailsUseCase.Params(pathId)) { resul ->
                resul.onSuccess { pathItem ->
                    _uiState.update { PathDetailsViewModelState.Success(pathItem) }
                }.onFailure {
                    _uiState.update { PathDetailsViewModelState.Error }
                }
            }
        }
    }

    fun onDescriptionClicked() {
        _uiState.update {
            if (it is PathDetailsViewModelState.Success) {
                it.copy(shouldShowDescription = true)
            } else {
                it
            }
        }
    }

    fun onDisableGoogleMapsClicked(pathDetailsSettingsOrderItem: PathDetailsSettingsOrderItem) {
        _uiState.update {
            if (it is PathDetailsViewModelState.Success) {
                it.copy(googleMapStatus = pathDetailsSettingsOrderItem)
            } else {
                it
            }
        }
    }
}

sealed interface PathDetailsViewModelState {
    data class InitialStart(val pathId: Int) : PathDetailsViewModelState
    data class Success(
        val path: PathItem,
        val shouldShowDescription: Boolean = false,
        val googleMapStatus: PathDetailsSettingsOrderItem = PathDetailsSettingsOrderItem.EnableMap
    ) : PathDetailsViewModelState

    object Error : PathDetailsViewModelState
}