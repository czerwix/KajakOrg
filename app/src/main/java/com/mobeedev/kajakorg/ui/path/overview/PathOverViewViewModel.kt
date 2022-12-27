package com.mobeedev.kajakorg.ui.path.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mobeedev.kajakorg.domain.error.onFailure
import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.model.overview.PathOverview
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathsDetailsUseCase
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathsOverviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PathOverViewViewModel(
    application: Application,
    private val getPathsOverviewUseCase: GetLocalPathsOverviewUseCase,
    private val getPathsDetailsUseCase: GetLocalPathsDetailsUseCase,
) : AndroidViewModel(application) {

    init {
        getPathsOverview()
    }

    private val _uiState: MutableStateFlow<PathOverViewViewModelState> =
        MutableStateFlow(PathOverViewViewModelState.InitialStart)
    val uiState: StateFlow<PathOverViewViewModelState> = _uiState

    fun getPathsOverview() {
        _uiState.update { PathOverViewViewModelState.Loading }
        getPathsOverviewUseCase.invoke { result ->
            result.onSuccess { pathList ->
                _uiState.update { PathOverViewViewModelState.PartialOverviewData(pathList) }
                getPathDetails()
            }.onFailure {
                _uiState.update { PathOverViewViewModelState.Error }
            }
        }
    }

    fun getPathDetails() {
        getPathsDetailsUseCase.invoke { result ->
            result.onSuccess { pathDetails ->
                val state = _uiState.value
                if (state is PathOverViewViewModelState.PartialOverviewData) {
                    _uiState.update {
                        PathOverViewViewModelState.Success(
                            state.pathOverviewList,
                            pathDetails
                        )
                    }
                } else {
                    //todo show error to user with retry to getting data
                }
            }.onFailure {
                _uiState.update { PathOverViewViewModelState.Error }
            }
        }
    }
}

sealed interface PathOverViewViewModelState {
    object InitialStart : PathOverViewViewModelState
    object Loading : PathOverViewViewModelState
    data class PartialOverviewData(val pathOverviewList: List<PathOverview>) :
        PathOverViewViewModelState

    data class Success(val pathOverviewList: List<PathOverview>, val pathDetails: List<Path>) :
        PathOverViewViewModelState

    object Error : PathOverViewViewModelState
}