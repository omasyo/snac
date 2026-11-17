package com.omasyo.snac.feature.auth.api.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.omasyo.snac.feature.auth.api.LocalIsSignedIn

@Composable
fun SignInGate(
    modifier: Modifier = Modifier,
    signedOutContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(modifier) { if (LocalIsSignedIn.current) content() else signedOutContent() }
}
