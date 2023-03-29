package com.mobeedev.kajakorg.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mobeedev.kajakorg.designsystem.theme.KajakTheme
import com.mobeedev.kajakorg.navigation.KajakNavHost
import com.mobeedev.kajakorg.ui.navigation.BottomNavigationScreen

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
    val bottomNavItems = listOf(
        BottomNavigationScreen.PathOverviewList,
        BottomNavigationScreen.CheckList,
        BottomNavigationScreen.MapOverview,
        BottomNavigationScreen.About
    )
    val backStackEntry = appState.navController.currentBackStackEntryAsState()
    val isBottomNavVisible = appState.navController
        .currentBackStackEntryAsState().value?.destination?.route in bottomNavItems.map { it.route }

    KajakTheme {
        Surface(
            color = Color.Transparent,
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                bottomBar = {//todo animate visibility
                    AnimatedVisibility(
                        visible = isBottomNavVisible,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it })
                    ) {
                        NavigationBar(
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                        ) {
                            bottomNavItems.forEachIndexed { index, item ->
                                val selected =
                                    item.route == backStackEntry.value?.destination?.route
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            painterResource(id = item.iconRes),
                                            contentDescription = stringResource(id = item.titleRes),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    },
//                                label = { Text(item) }, //todo figure out if i want text for items
                                    selected = selected,
                                    onClick = {
                                        if (selected.not()) {
                                            appState.navController.navigate(
                                                route = item.route,
                                                navOptions = NavOptions.Builder()
                                                    .setPopUpTo(
                                                        route = backStackEntry.value?.destination?.route,
                                                        inclusive = true
                                                    ).build()
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
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

