package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Stamp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
internal fun TrendingSpotlight(
    state: UiState<TrendingItem>,
    onClick: (Media) -> Unit,
    modifier: Modifier = Modifier,
) {
    val indicationColor = SnacColors.accent
    Box(
        modifier = modifier
            .film()
            .clickable(
                onClick = {
                    if (state is UiState.Success) {
                        onClick(state.content.media)
                    }
                },
                indication = remember { TintIndication { indicationColor } },
                interactionSource = null,
                enabled = state is UiState.Success,
            )
            .background(SnacColors.film)
            .heightIn(272.dp)
            .fillMaxWidth(),
    ) {
        when (state) {
            is UiState.Error -> {

                ErrorBox(
                    description = state.error.message,
                    onRetry = state.refresh,
                    modifier = Modifier.matchParentSize()
                ) {
                }
            }

            UiState.Loading -> {}
            is UiState.Success -> {
                val item = state.content
                Image(
                    imageUrl = item.media.imageUrl,
                    placeholder = ColorPainter(Color.Transparent),
                    modifier = Modifier.matchParentSize(),
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(16.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Stamp("#1 trending", modifier = Modifier.rotate(-6f))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.align(
                            Alignment.BottomStart
                        )
                    ) {
                        Text(
                            text = item.media.title,
                            style = SnacTypography.display.copy(
                                lineHeightStyle = CapHeightLineHeightStyle,
                            ),
                            color = SnacColors.onFilm,
                            modifier = Modifier.silhouette(SnacColors.signal)

                        )
                        val infoColor = SnacColors.accent
                        Text(
                            text = "${item.watchers} watching now", //TODO should have comma seperator
                            style = SnacTypography.hand,
                            color = infoColor

                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        TrendingSpotlight(
            state = UiState.Success(TrendingItem.MoviePreviewSample.first()),
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    SnacTheme {
        TrendingSpotlight(
            state = UiState.Error(SnacResult.Error.Unexpected) {},
            onClick = {}
        )
    }
}