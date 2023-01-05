package com.mobeedev.kajakorg.ui.path.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mobeedev.kajakorg.common.extensions.empty
import com.mobeedev.kajakorg.domain.error.onFailure
import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.model.overview.PathOverview
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathsDetailsUseCase
import com.mobeedev.kajakorg.domain.usecase.GetLocalPathsOverviewUseCase
import com.mobeedev.kajakorg.ui.model.PathOveriewItem
import com.mobeedev.kajakorg.ui.model.PathSortOrderItem
import com.mobeedev.kajakorg.ui.model.toItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PathOverviewViewModel(
    application: Application,
    private val getPathsOverviewUseCase: GetLocalPathsOverviewUseCase,
    private val getPathsDetailsUseCase: GetLocalPathsDetailsUseCase,
) : AndroidViewModel(application) {

    private val _uiState: MutableStateFlow<PathOverviewViewModelState> =
        MutableStateFlow(PathOverviewViewModelState.InitialStart)
    val uiState: StateFlow<PathOverviewViewModelState> = _uiState

    init {
        getPathsOverview()
    }

    fun getPathsOverview() {
        _uiState.update { PathOverviewViewModelState.Loading }
        getPathsOverviewUseCase.invoke { result ->
            result.onSuccess { pathList ->
                _uiState.update { PathOverviewViewModelState.PartialOverviewData(pathList) }
                getPathDetails()
            }.onFailure {
                _uiState.update { PathOverviewViewModelState.Error }
            }
        }
    }

    fun getPathDetails() {
        getPathsDetailsUseCase.invoke { result ->
            result.onSuccess { pathDetails ->
                val state = _uiState.value
                if (state is PathOverviewViewModelState.PartialOverviewData) {
                    _uiState.update {
                        PathOverviewViewModelState.Success(
                            parsePathItemList(state.pathOverviewList, pathDetails)
                        )
                    }
                } else {
                    //todo show error to user with retry to getting data
                }
            }.onFailure {
                _uiState.update { PathOverviewViewModelState.Error }
            }
        }
    }

    fun filterByName(searchPhrase: String) {
        val state = _uiState.value
        when {
            state !is PathOverviewViewModelState.Success -> return
            searchPhrase.isBlank() -> {
                _uiState.update { state.copy(textFilter = String.empty) }
            }
            else -> {
                _uiState.update { state.copy(textFilter = searchPhrase) }
            }
        }
    }

    fun onFilterCleared() {
        val state = _uiState.value
        if (state is PathOverviewViewModelState.Success) {
            _uiState.update { state.copy(textFilter = String.empty) }
        }
    }

    private fun parsePathItemList(pathOverview: List<PathOverview>, pathDetails: List<Path>) =
        pathOverview.map { overviewElement ->
            overviewElement.toItem(
                pathDetails.find { it.id == overviewElement.id }?.description
                    ?: String.empty
            )
        }.sortedBy { it.name }

    fun onSortOrderClicked(sortOrderItem: PathSortOrderItem) {
        val state = _uiState.value
        if (state is PathOverviewViewModelState.Success) {
            _uiState.update { state.copy(sortOrder = sortOrderItem) }
        }
    }
}

sealed interface PathOverviewViewModelState {
    object InitialStart : PathOverviewViewModelState
    object Loading : PathOverviewViewModelState
    data class PartialOverviewData(val pathOverviewList: List<PathOverview>) :
        PathOverviewViewModelState

    data class Success(
        val pathOverviewList: List<PathOveriewItem>,
        val textFilter: String = String.empty,
        val sortOrder: PathSortOrderItem = PathSortOrderItem.AToZ
    ) : PathOverviewViewModelState

    object Error : PathOverviewViewModelState
}