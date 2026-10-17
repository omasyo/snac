package com.omasyo.snac.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Stamp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

private val CapHeightLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.Both,
    mode = LineHeightStyle.Mode.Tight,
)

@Composable
internal fun TrendingSpotlight(
    title: String,
    watchingCount: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .film()
            .background(SnacColors.film)
            .heightIn(272.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .padding(horizontal = 16.dp)
    ) {
        Stamp("#1 trending", modifier = Modifier.rotate(-6f))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.align(
                Alignment.BottomStart
            )
        ) {
            val titleColor = SnacColors.onFilm
            Text(
                text = title,
                style = SnacTypography.display.copy(
                    lineHeightStyle = CapHeightLineHeightStyle,
                ),
                color = titleColor,
                modifier = Modifier.silhouette(SnacColors.signal)

            )
            val infoColor = SnacColors.accent
            Text(
                text = "$watchingCount watching now", //TODO should have comma seperator
                style = SnacTypography.hand,
                color = infoColor

            )
        }
    }
}

@Preview
@Composable
fun TrendingSpotlightPreview() {
    SnacTheme {
        TrendingSpotlight(
            title = "FRANKENSTEIN",
            watchingCount = 1204,
        )
    }
}