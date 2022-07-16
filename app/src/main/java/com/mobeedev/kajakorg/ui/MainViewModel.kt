package com.mobeedev.kajakorg.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mobeedev.kajakorg.domain.usecase.GetAllAvailablePathsUseCase

class MainViewModel(
    application: Application,
    private val getAllAvailablePathsUseCase: GetAllAvailablePathsUseCase
) : AndroidViewModel(application) {


}