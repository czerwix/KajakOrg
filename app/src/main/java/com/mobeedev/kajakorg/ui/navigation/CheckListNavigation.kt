package com.mobeedev.kajakorg.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

val checklistRoute = "check_list_route"
fun NavController.navigateToChecklist(navOptions: NavOptions? = null) {
    this.navigate(checklistRoute, navOptions)
}

val checklistEditRoute = "check_list_edit_route"
fun NavController.navigateToChecklistEdit(navOptions: NavOptions? = null) {
    this.navigate(checklistEditRoute, navOptions)
}

fun NavGraphBuilder.checklistGraph(
    navigateToCheckListEdit: () -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = checklistRoute) {
//todo add route destination here
    }
    composable(route = checklistEditRoute) {//todo add arguments

    }
}