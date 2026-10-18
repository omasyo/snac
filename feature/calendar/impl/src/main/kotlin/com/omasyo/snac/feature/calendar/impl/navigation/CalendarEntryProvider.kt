package com.omasyo.snac.feature.calendar.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.feature.calendar.api.navigation.CalendarNavKey
import com.omasyo.snac.feature.calendar.impl.CalendarScreen

fun EntryProviderScope<NavKey>.calendarEntry() {
    entry<CalendarNavKey> {
        CalendarScreen()
    }
}
