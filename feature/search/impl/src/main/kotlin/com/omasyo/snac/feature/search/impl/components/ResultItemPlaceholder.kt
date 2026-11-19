package com.omasyo.snac.feature.search.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.PosterAspectRatio
import com.omasyo.snac.core.ui.components.PosterCardLayout
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.random.Random


@Composable
internal fun ResultItemPlaceholder(seed: Int, modifier: Modifier = Modifier) {
    val rotation = remember(seed) { Random(seed + 80).nextInt(-3, 3).toFloat() }

    PosterCardLayout(
        interactionSource = null,
        poster = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(PosterAspectRatio)
                    .border(2.dp, SnacColors.outline)
                    .background(imageGradient())
            )
        },
        details = {
            Box(
                Modifier
                    .padding(top = 8.dp, bottom = 2.dp)
                    .width(96.dp)
                    .height(14.dp)
                    .background(SnacColors.placeholder)
            )
            Box(
                Modifier
                    .padding(bottom = 8.dp)
                    .width(72.dp)
                    .height(14.dp)
                    .background(SnacColors.placeholder)
            )
        },
        modifier = modifier
            .rotate(rotation)
            .width(112.dp),
    )
}


@Preview
@Composable
private fun Preview() {
    SnacTheme {
        ResultItemPlaceholder(1)
    }
}