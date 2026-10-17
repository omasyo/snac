package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun SignInPrompt(
    title: String,
    message: String,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .rotate(-1f)
            .silhouette(SnacColors.signal, offset = DpOffset(6.dp, 6.dp))
            .border(4.dp, SnacColors.ink)
            .background(SnacColors.chip)
    ) {
        ClapperboardStripes(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 16.dp)
        ) {
            Text(
                text = title,
                style = SnacTypography.title,
            )
            Text(
                text = message,
                style = SnacTypography.body,
            )

            Button(
                text = "SIGN IN WITH TRAKT",
                onClick = onSignInClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SnacTheme {
        SignInPrompt(
            title = "TRACK WHAT YOU WATCH",
            message = "Sign in to get Up Next, check-ins, paused playback and recommendations from your Trakt account.",
            onSignInClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}