package com.omasyo.snac.feature.auth.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

val LocalIsSignedIn = compositionLocalOf { false }


@Composable
fun ProvideSignInState(isSignedIn: Boolean, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalIsSignedIn provides isSignedIn, content = content)
}