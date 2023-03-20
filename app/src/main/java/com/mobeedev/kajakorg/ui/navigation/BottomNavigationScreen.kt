package com.mobeedev.kajakorg.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mobeedev.kajakorg.R

sealed class BottomNavigationScreen(val route: String, @StringRes val titleRes: Int,@DrawableRes val iconRes:Int ) {
    object PathOverviewList : BottomNavigationScreen(pathOverviewRoute, R.string.path_overview,R.drawable.conversion_path)
    object CheckList : BottomNavigationScreen(checklistRoute, R.string.check_list,R.drawable.outline_checklist_24)
    object MapOverview : BottomNavigationScreen(pathMapOverviewRoute, R.string.path_map,R.drawable.outline_map_24)
}