package com.omasyo.snac.feature.auth.api.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ClapperboardStripes
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.auth.api.ProvideSignInState
import com.omasyo.snac.feature.auth.api.rememberAuthenticator

@Composable
fun SignInPrompt(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    signedInContent: @Composable () -> Unit = {}, //TODO remove this default
) {
    val authenticator = rememberAuthenticator()
    SignInGate(
        modifier = modifier,
        signedOutContent = {
            Column(
                modifier = Modifier
                    .rotate(-1f)
                    .silhouette(SnacColors.signalShadow, offset = DpOffset(6.dp, 6.dp))
                    .border(4.dp, SnacColors.outline)
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
                        text = title.uppercase(),
                        style = SnacTypography.title,
                    )
                    Text(
                        text = message,
                        style = SnacTypography.body,
                    )

                    Button(
                        text = "SIGN IN WITH TRAKT",
                        onClick = { authenticator.signIn() },
                    )
                }
            }
        },
        content = signedInContent,
    )
}

@Preview
@Composable
private fun Preview() {
    ProvideSignInState(true) {
        SnacTheme {
            SignInPrompt(
                title = "TRACK WHAT YOU WATCH",
                message = "Sign in to get Up Next, check-ins, paused playback and recommendations from your Trakt account.",
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(SnacColors.accent)
                )
            }
        }
    }
}