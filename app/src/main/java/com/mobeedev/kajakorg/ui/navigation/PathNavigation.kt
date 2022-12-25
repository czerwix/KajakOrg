package com.mobeedev.kajakorg.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobeedev.kajakorg.navigation.KajakNavigationDestination
import com.mobeedev.kajakorg.ui.path.LoadKajakDataRoute

object LoadPathDestination : KajakNavigationDestination {
    override val route = "load_path_route"
    override val destination = "load_path_destination"
}

fun NavGraphBuilder.pathGraph(
    navigateToPathOverview: () -> Unit,
) {
    composable(route = LoadPathDestination.route) {
        LoadKajakDataRoute(navigateToPathOverview = navigateToPathOverview)
    }
}