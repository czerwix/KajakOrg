package com.mobeedev.kajakorg.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mobeedev.kajakorg.domain.error.onFailure
import com.mobeedev.kajakorg.domain.model.DataDownloadState
import com.mobeedev.kajakorg.domain.model.detail.Path
import com.mobeedev.kajakorg.domain.model.overview.PathOverview
import com.mobeedev.kajakorg.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

const val MINIMAL_DAYS_AMOUNT_BEFORE_RELOAD = 7

class MainDataLoadingViewModel(
    application: Application,
    private val loadAllAvailablePathsUseCase: LoadAllAvailablePathsUseCase,
    private val loadAllPathsDetailsUseCase: LoadAllPathsDetailsUseCase,
    private val getPathsOverviewUseCase: GetLocalPathsOverviewUseCase,
    private val getPathsDetailsUseCase: GetLocalAllPathDetailsUseCase,
    private val getLastUpdateDateUseCase: GetLastUpdateDateUseCase
) : AndroidViewModel(application) {

    init {
//        onCreated()
        getPathsOverview()
    }

    private val _uiState: MutableStateFlow<MainViewModelState> =
        MutableStateFlow(MainViewModelState.InitialStart)
    val uiState: StateFlow<MainViewModelState> = _uiState.asStateFlow()

    fun onCreated() {
        getLastUpdateDateUseCase.invoke { result ->
            result.onSuccess { dataStatus ->
                when (dataStatus.status) {
                    DataDownloadState.DONE -> {
                        if (ChronoUnit.DAYS.between(
                                ZonedDateTime.now(),
                                dataStatus.lastUpdateDate
                            ) > MINIMAL_DAYS_AMOUNT_BEFORE_RELOAD
                        ) {
                            _uiState.update { MainViewModelState.ConfirmDataReload }
                        } else {
                            getPathsOverview()
                        }
                    }
                    DataDownloadState.PARTIAL -> TODO()
                    DataDownloadState.EMPTY -> loadPathsData()
                }
            }.onFailure {
                _uiState.update { MainViewModelState.Loading() }
                loadPathsData()
            }
        }
    }

    fun getPathsOverview() {
        getPathsOverviewUseCase.invoke { result ->
            result.onSuccess { pathList ->
                _uiState.update { MainViewModelState.SuccessOverview(pathList) }
            }.onFailure {
                _uiState.update { MainViewModelState.Error }
            }
        }
    }

    fun onErrorReload() {
        loadPathsData()
    }

    private fun loadPathsData() {
        loadAllAvailablePathsUseCase.invoke { result ->
            _uiState.update { MainViewModelState.Loading() }

            result.onSuccess {
                _uiState.update { MainViewModelState.Loading() }
                loadPaths(it.map { pathOverview -> pathOverview.id })
            }.onFailure {
                _uiState.update { MainViewModelState.Error }
            }
        }
    }

    private fun loadPaths(pathIds: List<Int>) {
        viewModelScope.launch {
            loadAllPathsDetailsUseCase.workStatus.collect { loadedPaths ->
                if (pathIds.size == loadedPaths) {
                    getPathsOverview()
                } else {
                    _uiState.update {
                        MainViewModelState.Loading(
                            loadedPaths ?: 0,
                            pathIds.size
                        )
                    }
                }
            }
        }

        loadAllPathsDetailsUseCase.invoke(params = LoadAllPathsDetailsUseCase.Params(pathIds)) { result ->
            result.onSuccess {
                getPathsOverview()
            }.onFailure {
                _uiState.update { MainViewModelState.Error }
            }
        }
    }
}

sealed interface MainViewModelState {
    object InitialStart : MainViewModelState
    data class Loading(
        val loadingItemNumber: Int = -1,
        val itemCount: Int = -1
    ) : MainViewModelState

    data class SuccessDetail(val pathList: Path) : MainViewModelState
    data class SuccessOverview(val pathList: List<PathOverview>) : MainViewModelState

    object ConfirmDataReload : MainViewModelState
    object Error : MainViewModelState
}