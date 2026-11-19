package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.pressSilhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun PosterCardLayout(
    interactionSource: InteractionSource?,
    poster: @Composable BoxScope.() -> Unit,
    details: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Box(
            modifier = Modifier
                .pressSilhouette(interactionSource)
                .border(2.dp, SnacColors.outline)
                .background(SnacColors.card)
                .padding(6.dp),
            content = poster,
        )
        details()
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        PosterCardLayout(
            interactionSource = null,
            poster = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(PosterAspectRatio)
                        .background(imageGradient())
                )
            },
            details = {
                Text(text = "Frankenstein")
            },
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp)
                .width(112.dp),
        )
    }
}
