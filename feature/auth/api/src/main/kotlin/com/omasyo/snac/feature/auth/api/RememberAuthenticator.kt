package com.omasyo.snac.feature.auth.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metrox.android.MetroApplication

@Composable
fun rememberAuthenticator(): Authenticator {
    val application = LocalContext.current.applicationContext as MetroApplication
    return remember(application) {
        (application.appComponentProviders as AuthenticatorProvider).authenticator
    }
}

@ContributesTo(AppScope::class)
interface AuthenticatorProvider {
    val authenticator: Authenticator
}
