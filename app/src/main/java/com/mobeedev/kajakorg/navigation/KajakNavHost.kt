/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobeedev.kajakorg.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import com.mobeedev.kajakorg.ui.checklist.ChecklistViewModel
import com.mobeedev.kajakorg.ui.navigation.aboutGraph
import com.mobeedev.kajakorg.ui.navigation.checklistGraph
import com.mobeedev.kajakorg.ui.navigation.navigateToChecklistEdit
import com.mobeedev.kajakorg.ui.navigation.navigateToPathDetails
import com.mobeedev.kajakorg.ui.navigation.navigateToPathDetailsMap
import com.mobeedev.kajakorg.ui.navigation.navigateToPathLoading
import com.mobeedev.kajakorg.ui.navigation.navigateToPathMap
import com.mobeedev.kajakorg.ui.navigation.navigateToPathOverview
import com.mobeedev.kajakorg.ui.navigation.pathGraph
import com.mobeedev.kajakorg.ui.navigation.pathLoadingRoute
import com.mobeedev.kajakorg.ui.navigation.pathOverviewRoute
import org.koin.androidx.compose.getViewModel


/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun KajakNavHost(
    navController: NavHostController,
    onNavigateToDestination: (KajakNavigationDestination, String) -> Unit,
    onBackClick: () -> Unit,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    startDestination: String = pathOverviewRoute
) {
    val checkListViewModel: ChecklistViewModel = getViewModel()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        checklistGraph(checkListViewModel,
            navigateToCheckListEdit = { navController.navigateToChecklistEdit() },
            onBackClick = { navController.popBackStack() })

        aboutGraph(navigateToExplain = {
            //todo
        }, onBackClick = { navController.popBackStack() })

        pathGraph(navigateToPathLoading = {
            navController.navigateToPathLoading(
                navOptions = NavOptions.Builder().setPopUpTo(
                        route = pathOverviewRoute, inclusive = true
                    ).build()
            )
        }, navigateToPathOverviewFromLoading = {
            navController.navigateToPathOverview(
                navOptions = NavOptions.Builder().setPopUpTo(
                        route = pathLoadingRoute, inclusive = true
                    ).build()
            )
        }, navigateToPathOverview = {
            navController.navigateToPathOverview()
        }, navigateToPathDetails = {
            navController.navigateToPathDetails(it)
        }, navigateToPathMap = {
            navController.navigateToPathMap()
        }, navigateToPathDetailsMap = {
            navController.navigateToPathDetailsMap(it)
        }, onBackClick = { navController.popBackStack() })
    }
}
