package com.omasyo.snac.feature.charts.api.navigation

import com.omasyo.snac.core.navigation.TopLevelNavKey
import kotlinx.serialization.Serializable

@Serializable
data object ChartsNavKey : TopLevelNavKey {
    override val label: String = "Charts"
}
