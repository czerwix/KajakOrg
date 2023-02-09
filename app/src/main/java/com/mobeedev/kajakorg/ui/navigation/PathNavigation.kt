package com.mobeedev.kajakorg.ui.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.mobeedev.kajakorg.ui.path.details.PathDetailsRoute
import com.mobeedev.kajakorg.ui.path.load.LoadKajakDataRoute
import com.mobeedev.kajakorg.ui.path.map.PathMapRoute
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

fun NavController.navigateToPathDetails(pathId: Int) {
    this.navigate("path_details_route/$pathId")
}

const val pathMapIdArg = "path_map_Id"

internal class PathMapArgs(val pathId: Int = -1)

fun NavController.navigateToPathMap(pathId: Int) {
    this.navigate("path_map_route/$pathId")
}

fun NavGraphBuilder.pathGraph(
    navigateToPathOverview: () -> Unit,
    navigateToPathDetails: (Int) -> Unit,
    navigateToPathMap: () -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = pathLoadingRoute) {
        LoadKajakDataRoute(navigateToPathOverview = navigateToPathOverview)
    }
    composable(route = pathOverviewRoute) {
        PathOverviewRoute(navigateToPathDetails = navigateToPathDetails, navigateToPathMap = navigateToPathMap)
    }
    composable(
        route = "path_details_route/{${pathDetailsIdArg}}",
        arguments = listOf(navArgument(pathDetailsIdArg) {
            type = NavType.IntType
        })
    ) {
        PathDetailsRoute(onBackClick = onBackClick)
    }
    composable(
        route = "path_map_route/{${pathMapIdArg}}",
        arguments = listOf(navArgument(pathMapIdArg) {
            type = NavType.IntType
        })
    ) {
        PathMapRoute(onBackClick = onBackClick)
    }
}