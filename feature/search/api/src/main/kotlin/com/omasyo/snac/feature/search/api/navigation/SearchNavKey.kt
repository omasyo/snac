package com.omasyo.snac.feature.search.api.navigation

import com.omasyo.snac.core.navigation.TopLevelNavKey
import kotlinx.serialization.Serializable

@Serializable
data object SearchNavKey : TopLevelNavKey {
    override val label: String = "Search"
}
