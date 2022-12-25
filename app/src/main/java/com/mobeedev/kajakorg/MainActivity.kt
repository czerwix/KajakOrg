package com.mobeedev.kajakorg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.mobeedev.kajakorg.ui.KajakApp
import com.mobeedev.kajakorg.ui.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mainViewModel.loadData()

        setContent {
            KajakApp(calculateWindowSizeClass(this))
        }
    }
}