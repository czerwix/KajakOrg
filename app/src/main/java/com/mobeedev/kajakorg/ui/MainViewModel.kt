package com.mobeedev.kajakorg.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.mobeedev.kajakorg.domain.error.onFailure
import com.mobeedev.kajakorg.domain.usecase.*

class MainViewModel(
    application: Application,
    private val loadAllAvailablePathsUseCase: LoadAllAvailablePathsUseCase,
    private val loadAllPathsDetailsUseCase: LoadAllPathsDetailsUseCase,
    private val getPathsOverviewUseCase: GetPathsOverviewUseCase,
    private val getPathsDetailsUseCase: GetPathsDetailsUseCase,
    private val getLastUpdateDateUseCase: GetLastUpdateDateUseCase
) : AndroidViewModel(application) {
//tmp functions for testing data/domain layers


    fun loadDataWithDDOSProtection() {
        getLastUpdateDateUseCase.invoke { result ->
            result.onSuccess {
                //todo if last X amount of time passed since last update.
                //and confirm with user if he is sure he wants to update, in order to minimise webpage stress.
                it.dayOfMonth
            }.onFailure {
                //check if error is NoLatsUpdateFound and load data
                loadData()
            }
        }
    }

    fun loadData() {
        loadAllAvailablePathsUseCase.invoke { result ->
            result.onSuccess {
                Log.d("COSIK-----", "$it")
                loadPaths(it.map { pathOverview -> pathOverview.id })
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