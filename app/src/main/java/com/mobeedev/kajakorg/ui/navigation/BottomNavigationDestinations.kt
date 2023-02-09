package com.mobeedev.kajakorg.ui.navigation

import androidx.annotation.StringRes
import com.mobeedev.kajakorg.R

sealed class BottomNavigationDestinations(val route: String, @StringRes val resourceId: Int) {
    object PathList : BottomNavigationDestinations("path_overview_route", R.string.path_list)
    object PathMap : BottomNavigationDestinations("path_map_route", R.string.path_map)
}