package com.omasyo.snac.feature.calendar

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.ui.components.navigation.TopLevelRoute
import kotlinx.serialization.Serializable

@Serializable
data object CalendarRoute : TopLevelRoute {

    override val label: String = "Calendar"
}


fun EntryProviderScope<NavKey>.calendarNavigation() {
    entry<CalendarRoute> {
        CalendarScreen()
    }
}