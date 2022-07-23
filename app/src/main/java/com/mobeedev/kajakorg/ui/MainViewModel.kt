package com.mobeedev.kajakorg.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.mobeedev.kajakorg.domain.usecase.GetAllAvailablePathsUseCase
import com.mobeedev.kajakorg.domain.usecase.GetAllPathsDetailsUseCase

class MainViewModel(
    application: Application,
    private val getAllAvailablePathsUseCase: GetAllAvailablePathsUseCase,
    private val getAllPathsDetailsUseCase: GetAllPathsDetailsUseCase
) : AndroidViewModel(application) {


    fun loadData() {
        getAllAvailablePathsUseCase.invoke { result ->
            result.onSuccess {
                Log.d("COSIK-----", "$it")
                loadPaths(it.map { pathOverview -> pathOverview.id })
            }.onFailure {
                Log.d("COSIK-----", "$it")
            }
        }
    }

    fun loadPaths(pathIds: List<Int>) {
        getAllPathsDetailsUseCase.invoke(
            params = GetAllPathsDetailsUseCase.Params(pathIds)
        ) { result ->
            result.onSuccess {
                Log.d("COSIK-----", " managed to load all paths!!!!!! $it")
            }.onFailure {
                Log.d("COSIK-----", "Couldn't load paths :((( $it")
            }
        }
    }

}