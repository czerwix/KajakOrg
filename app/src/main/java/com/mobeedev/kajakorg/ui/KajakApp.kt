package com.mobeedev.kajakorg.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.navigation.KajakNavHost
import com.mobeedev.kajakorg.ui.navigation.BottomNavigationDestinations

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun KajakApp(
    windowSizeClass: WindowSizeClass,
    appState: KajakAppState = rememberKajakAppState(windowSizeClass)
) {
    KajakTheme {
        Surface(
            color = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal
                            )
                        )
                ) {
                    KajakNavHost(
                        navController = appState.navController,
                        onBackClick = appState::onBackClick,
                        onNavigateToDestination = appState::navigate,
                        windowSizeClass = appState.windowSizeClass,
                        modifier = Modifier
                            .padding(padding)
                            .consumeWindowInsets(padding)
                    )
                }
            }
        }
    }
}

