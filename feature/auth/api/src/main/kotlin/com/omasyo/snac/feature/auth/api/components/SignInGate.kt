package com.omasyo.snac.feature.auth.api.components

import androidx.compose.runtime.Composable
import com.omasyo.snac.feature.auth.api.LocalIsSignedIn

@Composable
fun SignInGate(
    signedOutContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    if (LocalIsSignedIn.current) content() else signedOutContent()
}
