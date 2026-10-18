package com.omasyo.snac.feature.charts

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.ui.components.navigation.TopLevelRoute
import kotlinx.serialization.Serializable

@Serializable
data object ChartsRoute : TopLevelRoute {

    override val label: String = "Charts"
}


fun EntryProviderScope<NavKey>.chartsNavigation() {
    entry<ChartsRoute> {
        ChartsScreen()
    }
}