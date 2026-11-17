package com.omasyo.snac.feature.auth.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

val LocalIsSignedIn = compositionLocalOf { false }


@Composable
fun ProvideSignInState(content: @Composable () -> Unit) {
    val isSignedIn by rememberAuthenticator().isSignedIn.collectAsStateWithLifecycle()
    CompositionLocalProvider(LocalIsSignedIn provides isSignedIn, content = content)
}