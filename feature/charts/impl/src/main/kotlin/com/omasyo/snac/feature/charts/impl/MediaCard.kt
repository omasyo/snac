package com.omasyo.snac.feature.charts.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.random.Random

@Composable
internal fun MediaCard(
    position: Int,
    item: ChartItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = SnacColors.filmLine
    val indicationColor = SnacColors.accent
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 32.dp, end = 8.dp)
            .drawBehind {
                drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height))
            }
            .clickable(
                onClick = onClick,
                indication = remember { TintIndication { indicationColor } },
                interactionSource = null,
            )
            .padding(end = 8.dp)
            .padding(vertical = 8.dp),
    ) {
        val positionColor = if (position <= 3) SnacColors.accent else SnacColors.faint
        Text(
            text = "$position",
            style = SnacTypography.title.copy(textAlign = TextAlign.End),
            color = positionColor,
            modifier = Modifier.widthIn(32.dp)
        )

        Image(
            imageUrl = item.media.imageUrl,
            modifier = Modifier
                .rotate(remember(item.media.slug) {
                    Random(item.media.slug.hashCode()).nextInt(
                        -3, 3
                    ).toFloat()
                })
                .border(2.dp, SnacColors.chip)
                .background(SnacColors.muted)
                .size(56.dp, 80.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = item.media.title,
                style = SnacTypography.subtitle,
                color = SnacColors.onFilm,
            )
            Text(
                text = item.media.year.toString(),
                style = SnacTypography.label,
                color = SnacColors.mutedOnFilm,
            )
            Text(
                text = item.info,
                style = SnacTypography.hand,
                color = SnacColors.accent,
            )
        }
    }
}

@Composable
internal fun MediaCardPlaceholder(
    position: Int,
    modifier: Modifier = Modifier,
) {

    val borderColor = SnacColors.filmLine
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 32.dp, end = 8.dp)
            .drawBehind {
                drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height))
            }
            .padding(end = 8.dp)
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = "$position",
            style = SnacTypography.title.copy(textAlign = TextAlign.End),
            color = SnacColors.faint,
            modifier = Modifier.widthIn(32.dp)
        )

        Box(
            modifier = Modifier
                .rotate(-2f)
                .border(2.dp, SnacColors.chip)
                .background(imageGradient())
                .size(56.dp, 80.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Box(
                Modifier
                    .padding(vertical = 4.dp)
                    .width(160.dp)
                    .height(18.dp)
                    .background(SnacColors.faint)
            )
            Box(
                Modifier
                    .padding(vertical = 8.dp)
                    .width(40.dp)
                    .height(12.dp)
                    .background(SnacColors.faint)
            )
            Box(
                Modifier
                    .width(104.dp)
                    .height(14.dp)
                    .background(SnacColors.faint)
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    SnacTheme {
//        Box(Modifier.background(SnacColors.page)) {
        MediaCard(
            position = 3,
            item = TrendingItem.MoviePreviewSample.first(),
            onClick = {},
        )
//        }
    }
}

@Preview
@Composable
private fun PlaceholderPreview() {
    SnacTheme {
        MediaCardPlaceholder(position = 3)
    }
}