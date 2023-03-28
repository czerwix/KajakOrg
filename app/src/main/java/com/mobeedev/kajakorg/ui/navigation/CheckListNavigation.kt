package com.mobeedev.kajakorg.ui.navigation

import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModel
import com.mobeedev.kajakorg.ui.checklist.edit.ChecklistEditRoute
import com.mobeedev.kajakorg.ui.checklist.overview.ChecklistOverviewRoute
import org.koin.androidx.compose.getViewModel

val checklistRoute = "check_list_route"
fun NavController.navigateToChecklist(navOptions: NavOptions? = null) {
    this.navigate(checklistRoute, navOptions)
}

val checklistEditRoute = "check_list_edit_route"
fun NavController.navigateToChecklistEdit(navOptions: NavOptions? = null) {
    this.navigate(checklistEditRoute, navOptions)
}


fun NavGraphBuilder.checklistGraph(
    checklistViewModel :ChecklistViewModel,
    navigateToCheckListEdit: () -> Unit,
    onBackClick: () -> Unit
) {
    composable(route = checklistRoute) {
        ChecklistOverviewRoute(
            navigateToChecklistEdit = navigateToCheckListEdit,
            viewModel = checklistViewModel
        )
    }
    composable(route = checklistEditRoute) {
            ChecklistEditRoute(onBackClick = onBackClick, viewModel = checklistViewModel)
    }
}