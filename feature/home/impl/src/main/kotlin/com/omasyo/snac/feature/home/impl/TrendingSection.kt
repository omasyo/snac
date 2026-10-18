package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.LocalSilhouetteColor
import com.omasyo.snac.core.ui.modifiers.Orientation
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.bleedHorizontally
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.random.Random

@Composable
internal fun TrendingSection(
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalSilhouetteColor provides SnacColors.signal
    ) {
        val background = SnacColors.film
        Column(
            modifier = modifier
                .bleedHorizontally(8.dp)
                .film(
                    orientation = Orientation.Horizontal,
                    edgeInset = 16.dp
                )
                .background(SnacColors.film)
                .padding(vertical = 40.dp, horizontal = 8.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "TRENDING",
                    style = SnacTypography.headline,
                    color = SnacColors.onFilm,
                    modifier = Modifier
                        .rotate(-3f)
                        .silhouette(),
                )
            }

            LazyRow {
                items(10) {

                    val rotation = remember { Random.nextInt(-2, 3).toFloat() }

                    TrendingCard(
                        title = "FRANKENSTEIN",
                        watchingCount = 1204,
                        imageUrl = null,
                        onClick = {},
                        modifier = Modifier
                            .padding(16.dp)
                            .rotate(rotation)
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendingCard(
    title: String,
    watchingCount: Int,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick,
        )
    ) {
        Image(
            imageUrl = imageUrl,
            modifier = Modifier
                .indication(interactionSource, SilhouettePressIndication())
                .border(4.dp, SnacColors.chip)
                .background(SnacColors.muted)
                .size(120.dp, 180.dp),
        )
        Text(
            text = title,
            style = SnacTypography.subtitle,
            color = SnacColors.onFilm,
        )
        Text(
            text = "$watchingCount watching",
            style = SnacTypography.hand,
            color = SnacColors.accent,
        )
    }
}

@Preview
@Composable
private fun TrendingCardPreview() {
    SnacTheme {
        CompositionLocalProvider(
            LocalSilhouetteColor provides SnacColors.signal
        ) {
            TrendingCard(
                title = "FRANKENSTEIN",
                watchingCount = 1204,
                imageUrl = null,
                onClick = {},
                modifier = Modifier
                    .padding(16.dp)
                    .rotate(-2f)
            )
        }
    }
}