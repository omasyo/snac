package com.omasyo.snac.feature.calendar.api.navigation

import com.omasyo.snac.core.navigation.TopLevelNavKey
import kotlinx.serialization.Serializable

@Serializable
data object CalendarNavKey : TopLevelNavKey {
    override val label: String = "Calendar"
}
