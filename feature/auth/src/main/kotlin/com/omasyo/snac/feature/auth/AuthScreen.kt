package com.omasyo.snac.feature.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.omasyo.snac.core.ui.theme.SnacTheme

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        BasicText(text = "Auth")
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    SnacTheme {
        AuthScreen()
    }
}
