package com.mobeedev.kajakorg.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobeedev.kajakorg.ui.path.details.PathDetailsRoute
import com.mobeedev.kajakorg.ui.path.load.LoadKajakDataRoute
import com.mobeedev.kajakorg.ui.path.map.details.PathDetailsMapRoute
import com.mobeedev.kajakorg.ui.path.map.overview.PathMapRoute
import com.mobeedev.kajakorg.ui.path.overview.PathOverviewRoute

val pathLoadingRoute = "load_path_route"
fun NavController.navigateToPathLoading(navOptions: NavOptions? = null) {
    this.navigate(pathLoadingRoute, navOptions)
}

val pathOverviewRoute = "path_overview_route"
fun NavController.navigateToPathOverview(navOptions: NavOptions? = null) {
    this.navigate(pathOverviewRoute, navOptions)
}

const val pathDetailsIdArg = "path_details_Id"

internal class PathDetailsArgs(val pathId: Int)

fun NavController.navigateToPathDetails(pathId: Int, navOptions: NavOptions? = null) {
    this.navigate("path_details_route/$pathId", navOptions)
}

const val pathMapIdArg = "path_map_Id"

internal class PathMapArgs(val pathId: Int = -1)

val pathMapOverviewRoute = "path_map_route"
fun NavController.navigateToPathMap(navOptions: NavOptions? = null) {
    this.navigate(pathMapOverviewRoute, navOptions)
}

fun NavController.navigateToPathDetailsMap(pathId: Int, navOptions: NavOptions? = null) {
    this.navigate("path_details_map_route/$pathId", navOptions)
}

fun NavGraphBuilder.pathGraph(
    navigateToPathOverview: () -> Unit,
    navigateToPathDetails: (Int) -> Unit,
    navigateToPathMap: () -> Unit,
    navigateToPathDetailsMap: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    //Loading
    composable(route = pathLoadingRoute) {
        LoadKajakDataRoute(navigateToPathOverview = navigateToPathOverview)
    }
    //Path
    composable(route = pathOverviewRoute) {
        PathOverviewRoute(
            navigateToPathDetails = navigateToPathDetails, navigateToPathMap = navigateToPathMap
        )
    }
    composable(
        route = "path_details_route/{${pathDetailsIdArg}}",
        arguments = listOf(navArgument(pathDetailsIdArg) {
            type = NavType.IntType
        })
    ) {
        PathDetailsRoute(
            onBackClick = onBackClick, navigateToPathMap = navigateToPathDetailsMap
        )
    }
    //MAP
    composable(route = pathMapOverviewRoute) {
        PathMapRoute(onBackClick = onBackClick, navigateToPathDetails = navigateToPathDetails)
    }
    composable(
        route = "path_details_map_route/{${pathMapIdArg}}",
        arguments = listOf(navArgument(pathMapIdArg) {
            type = NavType.IntType
        })
    ) {
        PathDetailsMapRoute(onBackClick = onBackClick)
    }
}