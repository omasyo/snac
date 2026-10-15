package com.omasyo.snac.feature.auth

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.util.Consumer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.serialization.Serializable

@Serializable
data object Auth : NavKey

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = metroViewModel(),
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.authorizeUrls.collect { url ->
            CustomTabsIntent.Builder().build().launchUrl(context, url.toUri())
        }
    }

    AuthRedirectEffect(viewModel::onRedirect)

    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BasicText("Auth state: $authState")

        when (authState) {
            AuthState.Idle -> Button("Sign in", viewModel::signIn)

            is AuthState.Error -> {
                Button("Sign in", viewModel::signIn)
            }

            AuthState.Authorizing, AuthState.Exchanging -> BasicText("Waiting…")
        }
    }
}

@Composable
private fun AuthRedirectEffect(onRedirect: (String) -> Unit) {
    val activity = LocalActivity.current as? ComponentActivity ?: return

    DisposableEffect(activity) {
        fun handle(intent: Intent) {
            val uri = intent.data ?: return
            if (uri.scheme != "snac" || uri.host != "redirect") return //TODO redirect uri hardcoded here

            onRedirect(uri.toString())
        }

        handle(activity.intent)
        val listener = Consumer<Intent> { handle(it) }

        activity.addOnNewIntentListener(listener)
        onDispose { activity.removeOnNewIntentListener(listener) }
    }
}

@Composable
private fun Button(text: String, onClick: () -> Unit) {
    Box(Modifier.clickable(onClick = onClick).padding(16.dp)) {
        BasicText(text)
    }
}
