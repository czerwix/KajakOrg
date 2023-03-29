package com.mobeedev.kajakorg.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mobeedev.kajakorg.ui.about.info.AboutAppRoute

val aboutRoute = "about_route"
fun NavController.navigateToAbout(navOptions: NavOptions? = null) {
    this.navigate(aboutRoute, navOptions)
}

val explainRoute = "explain_route"
fun NavController.navigateToExplain(navOptions: NavOptions? = null) {
    this.navigate(explainRoute, navOptions)
}

fun NavGraphBuilder.aboutGraph(
    navigateToExplain: () -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = aboutRoute) {
        AboutAppRoute(navigateToExplain = navigateToExplain)
    }
    composable(route = explainRoute) {
        //todo
    }
}