package com.omasyo.snac.feature.media.impl.show

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.ui.R as UiR
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Icon
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.format.MetaSeparator
import com.omasyo.snac.core.ui.format.formatFullDate
import com.omasyo.snac.core.ui.format.formatRating
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.Side
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun EpisodesSection(
    onEpisodeClick: (EpisodeDetails) -> Unit,
    state: UiState<List<EpisodeDetails>>,
    modifier: Modifier = Modifier,
) {


    val placeholder = remember { movableContentOf { Placeholder() } }
    Box(
        modifier = modifier
            .film(
                side = Side.Start,
                crossEdgeInset = 12.dp,
            )
            .background(SnacColors.film)
    ) {
        when (state) {
            is UiState.Error -> {
                ErrorBox(
                    description = state.error.message,
                    onRetry = state.refresh,
                ) {
                    placeholder()
                }
            }


            UiState.Loading -> {
                placeholder()
            }

            is UiState.Success -> {
                Column {
                    for (episode in state.content) {
                        EpisodeRow(
                            episode = episode,
                            onClick = { onEpisodeClick(episode) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeRow(
    episode: EpisodeDetails,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = SnacColors.filmLine
    val indicationColor = SnacColors.accent
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .padding(start = 32.dp, end = 8.dp)
            .drawBehind {
                drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height))
            }
            .padding(vertical = 8.dp)
            .clickable(
                onClick = onClick,
                indication = remember { TintIndication { indicationColor } },
                interactionSource = null,
            )
    ) {
        Box(
            modifier = Modifier
                .size(92.dp, 54.dp)
        ) {
            Image(
                imageUrl = episode.screenshotUrl,
                placeholder = ColorPainter(SnacColors.muted.copy(0.4f)),
                modifier = Modifier.matchParentSize(),
            )
            Text(
                text = episode.number.toString(),
                style = SnacTypography.tag,
                modifier = Modifier
                    .background(SnacColors.accent)
                    .padding(horizontal = 4.dp),
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = episode.displayTitle(),
                style = SnacTypography.tag,
                color = SnacColors.onFilm,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                episode.firstAired?.let {
                    Text(
                        text = "${it.formatFullDate()} $MetaSeparator",
                        style = SnacTypography.hand,
                        color = SnacColors.mutedOnFilm,
                    )
                }
                Icon(
                    icon = UiR.drawable.star_fill,
                    contentDescription = null,
                    tint = SnacColors.mutedOnFilm,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = episode.rating.formatRating(),
                    style = SnacTypography.hand,
                    color = SnacColors.mutedOnFilm,
                )
            }
        }
    }
}

@Composable
internal fun EpisodeDetails.displayTitle(): String =
    title ?: stringResource(R.string.episode_fallback_title, number)

@Composable
private fun Placeholder() {
    val borderColor = SnacColors.filmLine
    Column {
        repeat(8) {
            val position = it + 1
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(start = 32.dp, end = 8.dp)
                    .drawBehind {
                        drawLine(
                            borderColor,
                            Offset(0f, size.height),
                            Offset(size.width, size.height)
                        )
                    }
                    .padding(vertical = 8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(92.dp, 54.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(SnacColors.muted.copy(0.4f)),
                    )
                    Text(
                        text = position.toString(),
                        style = SnacTypography.tag,
                        modifier = Modifier
                            .background(SnacColors.muted)
                            .padding(horizontal = 4.dp),
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Box(
                        Modifier
                            .padding(vertical = 8.dp)
                            .width(64.dp)
                            .height(12.dp)
                            .background(SnacColors.placeholder)
                    )
                    Box(
                        Modifier
                            .padding(vertical = 8.dp)
                            .width(120.dp)
                            .height(12.dp)
                            .background(SnacColors.placeholder)
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            EpisodesSection(
                onEpisodeClick = { },
                state = UiState.Success(EpisodeDetails.PreviewSample.take(8)),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<List<EpisodeDetails>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            EpisodesSection(
                onEpisodeClick = { },
                state = state,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
