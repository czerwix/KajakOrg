package com.mobeedev.kajakorg.ui.navigation

import android.net.Uri
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mobeedev.kajakorg.navigation.KajakNavigationDestination
import com.mobeedev.kajakorg.ui.path.load.LoadKajakDataRoute
import com.mobeedev.kajakorg.ui.path.overview.PathOverviewRoute

object LoadPathDestination : KajakNavigationDestination {
    override val route = "load_path_route"
    override val destination = "load_path_destination"
}

object PathOverviewDestination : KajakNavigationDestination {
    override val route = "path_overview_route"
    override val destination = "path_overview_destination"
}

object PathDetailsDestination : KajakNavigationDestination {
    //todo once screen is done add to graph
    const val pathDetailsIdArg = "path_details_Id"
    override val route = "path_details_route/{$pathDetailsIdArg}"
    override val destination = "path_details_destination"


    fun createNavigationRoute(pathDetailsIdArg: String): String {
        val encodedId = Uri.encode(pathDetailsIdArg)
        return "path_details_route/$encodedId"
    }

    fun fromNavArgs(entry: NavBackStackEntry): String {
        val encodedId = entry.arguments?.getString(pathDetailsIdArg)!!
        return Uri.decode(encodedId)
    }
}

fun NavGraphBuilder.pathGraph(
    navigateToPathOverview: () -> Unit,
    navigateToPathDetails: (Int) -> Unit,
) {
    composable(route = LoadPathDestination.route) {
        LoadKajakDataRoute(navigateToPathOverview = navigateToPathOverview)
    }
    composable(route = PathOverviewDestination.route) {
        PathOverviewRoute(navigateToPathDetails = navigateToPathDetails)
    }
}