package com.mobeedev.kajakorg.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.tracing.trace
import com.mobeedev.kajakorg.navigation.KajakNavigationDestination

@Composable
fun rememberKajakAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
): KajakAppState {
    return remember(navController, windowSizeClass) {
        KajakAppState(navController, windowSizeClass)
    }
}

@Stable
class KajakAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    /**
     * @param destination: The [VamaNavigationDestination] the app needs to navigate to.
     * @param route: Optional route to navigate to in case the destination contains arguments.
     */
    fun navigate(destination: KajakNavigationDestination, route: String? = null) {
        trace("Navigation: $destination") {
            navController.navigate(route ?: destination.route)
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}