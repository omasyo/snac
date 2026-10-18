package com.omasyo.snac.feature.search

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.ui.components.navigation.TopLevelRoute
import kotlinx.serialization.Serializable

@Serializable
data object SearchRoute : TopLevelRoute {

    override val label: String = "Search"
}


fun EntryProviderScope<NavKey>.searchNavigation() {
    entry<SearchRoute> {
        SearchScreen()
    }
}