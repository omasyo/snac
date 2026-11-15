package com.omasyo.snac.feature.home.api.navigation

import com.omasyo.snac.core.navigation.TopLevelNavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeNavKey : TopLevelNavKey {
    override val label: String = "Home"
}
