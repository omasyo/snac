package com.omasyo.snac.feature.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.feature.settings.api.navigation.SettingsNavKey
import com.omasyo.snac.feature.settings.impl.SettingsScreen

fun EntryProviderScope<NavKey>.settingsEntry() {
    entry<SettingsNavKey> {
        SettingsScreen()
    }
}
