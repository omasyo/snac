package com.omasyo.snac.feature.home.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.navigation.Navigator
import com.omasyo.snac.feature.auth.api.rememberAuthenticator
import com.omasyo.snac.feature.home.api.navigation.HomeNavKey
import com.omasyo.snac.feature.home.impl.HomeScreen
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey
import com.omasyo.snac.feature.settings.api.navigation.SettingsNavKey

fun EntryProviderScope<NavKey>.homeEntry(navigator: Navigator) {
    entry<HomeNavKey> {
        val authenticator = rememberAuthenticator()

        HomeScreen(
            onSignInClick = { authenticator.signIn() },
            onSettingsClick = { navigator.navigate(SettingsNavKey) },
            onMediaClick = {
                when (it) {
                    is Movie -> navigator.navigate(MovieNavKey(it.slug))
                    is Show -> navigator.navigate(ShowNavKey(it.slug))
                }
            },
            onAllChartsClick = {},
        )
    }
}
