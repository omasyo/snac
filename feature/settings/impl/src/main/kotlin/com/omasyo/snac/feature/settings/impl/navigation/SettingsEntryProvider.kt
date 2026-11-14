package com.omasyo.snac.feature.settings.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.ui.navigation.Navigator
import com.omasyo.snac.feature.auth.api.rememberAuthenticator
import com.omasyo.snac.feature.settings.api.navigation.SettingsNavKey
import com.omasyo.snac.feature.settings.impl.SettingsScreen

fun EntryProviderScope<NavKey>.settingsEntry(navigator: Navigator) {
    entry<SettingsNavKey> {
        val authenticator = rememberAuthenticator()

        SettingsScreen(
            onBackClick = navigator::goBack,
            onSignInClick = { authenticator.signIn() },
        )
    }
}
