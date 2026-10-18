package com.omasyo.snac.navigation

import com.omasyo.snac.feature.calendar.api.navigation.CalendarNavKey
import com.omasyo.snac.feature.charts.api.navigation.ChartsNavKey
import com.omasyo.snac.feature.home.api.navigation.HomeNavKey
import com.omasyo.snac.feature.search.api.navigation.SearchNavKey

data class TopLevelNavItem(
    val label: String,
)

val HOME = TopLevelNavItem(label = "Home")

val CHARTS = TopLevelNavItem(label = "Charts")

val SEARCH = TopLevelNavItem(label = "Search")

val CALENDAR = TopLevelNavItem(label = "Calendar")

val TOP_LEVEL_NAV_ITEMS = mapOf(
    HomeNavKey to HOME,
    ChartsNavKey to CHARTS,
    SearchNavKey to SEARCH,
    CalendarNavKey to CALENDAR,
)
