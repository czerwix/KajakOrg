package com.mobeedev.kajakorg.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.mobeedev.kajakorg.data.model.detail.PathDto
import com.mobeedev.kajakorg.domain.error.onFailure
import com.mobeedev.kajakorg.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    application: Application,
    private val loadAllAvailablePathsUseCase: LoadAllAvailablePathsUseCase,
    private val loadAllPathsDetailsUseCase: LoadAllPathsDetailsUseCase,
    private val getPathsOverviewUseCase: GetPathsOverviewUseCase,
    private val getPathsDetailsUseCase: GetPathsDetailsUseCase,
    private val getLastUpdateDateUseCase: GetLastUpdateDateUseCase
) : AndroidViewModel(application) {

    private val _uiState: MutableStateFlow<MainViewModelState> =
        MutableStateFlow(MainViewModelState.InitialStart)
    val uiState: StateFlow<MainViewModelState> = _uiState

    fun loadDataWithDDOSProtection() {
        getLastUpdateDateUseCase.invoke { result ->
            result.onSuccess {
                //todo if last X amount of time passed since last update.
                //and confirm with user if he is sure he wants to update, in order to minimise webpage stress.
                getPathsOverview()
            }.onFailure {
                //check if error is NoLatsUpdateFound and load data
                loadData()
            }
        }
    }

    fun getPathsOverview() {
        getPathsOverviewUseCase.invoke { result ->
            result.onSuccess {
//                return to UI
            }.onFailure {
                //todo
            }
        }
    }

    fun loadData() {
        loadAllAvailablePathsUseCase.invoke { result ->
            result.onSuccess {
                Log.d("COSIK-----", "$it")
                loadPaths(it.map { pathOverview -> pathOverview.id })//todo maybe we can show some cool animation here. first step is done.
            }.onFailure {
                Log.d("COSIK-----", "$it")
            }
        }
    }

    fun loadPaths(pathIds: List<Int>) {
        loadAllPathsDetailsUseCase.invoke(
            params = LoadAllPathsDetailsUseCase.Params(pathIds)
        ) { result ->
            result.onSuccess {
                Log.d("COSIK-----", " managed to load all paths!!!!!! $it")
            }.onFailure {
                Log.d("COSIK-----", "Couldn't load paths :((( $it")
            }
        }
    }
}

sealed interface MainViewModelState {
    object InitialStart : MainViewModelState
    data class Loading(val downloadedPercent: Int = 0) : MainViewModelState
    data class Success(val pathList: List<PathDto>) : MainViewModelState
    object Error : MainViewModelState
}