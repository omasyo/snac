package com.omasyo.snac.core.ui.components.navigation

import androidx.navigation3.runtime.NavKey

interface NavRoute : NavKey

interface TopLevelRoute : NavRoute {
    val label: String
}