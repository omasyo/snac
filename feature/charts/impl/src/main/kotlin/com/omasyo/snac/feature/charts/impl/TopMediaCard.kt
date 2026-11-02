package com.omasyo.snac.feature.charts.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors


@Composable
internal fun TopMediaCard(
    item: ChartItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val indicationColor = SnacColors.signalShadow
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .clickable(
                indication = SilhouettePressIndication({ indicationColor }, DpOffset(6.dp, 6.dp)),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .fillMaxWidth()
            .background(SnacColors.film)
            .padding(16.dp)
    ) {
        Image(
            imageUrl = item.media.imageUrl,
            modifier = Modifier
                .rotate(-3f)
                .silhouette(SnacColors.accentShadow)
                .border(4.dp, SnacColors.chip)
                .size(128.dp, 192.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = item.media.title,
                style = SnacTypography.title,
                color = SnacColors.onFilm,
            )
            Text(
                text = item.media.year.toString(),
                style = SnacTypography.label,
                color = SnacColors.mutedOnFilm,
            )
            Text(
                text = item.info(),
                style = SnacTypography.hand,
                color = SnacColors.accent,
            )
        }
    }
}

@Composable
internal fun TopMediaCardPlaceholder(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .silhouette(SnacColors.signalShadow)
            .fillMaxWidth()
            .background(SnacColors.film)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .rotate(-3f)
                .silhouette(SnacColors.accentShadow)
                .border(4.dp, SnacColors.chip)
                .background(imageGradient())
                .size(128.dp, 192.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Box(
                Modifier
                    .padding(vertical = 4.dp)
                    .width(160.dp)
                    .height(24.dp)
                    .background(SnacColors.placeholder)
            )
            Box(
                Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(12.dp)
                    .background(SnacColors.placeholder)
            )
            Box(
                Modifier
                    .width(104.dp)
                    .height(16.dp)
                    .background(SnacColors.placeholder)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        TopMediaCard(
            item = TrendingItem.MoviePreviewSample.first(),
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PlaceholderPreview() {
    SnacTheme {
        TopMediaCardPlaceholder()
    }
}