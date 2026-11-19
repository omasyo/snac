package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Episode
import com.omasyo.snac.core.model.media.EpisodePlayback
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.MoviePlayback
import com.omasyo.snac.core.model.media.PlaybackItem
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.ProgressBar
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.format.formatEpisodeCode
import com.omasyo.snac.core.ui.format.formatRelative
import com.omasyo.snac.core.ui.format.formatRuntime
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun ContinueWatchingSection(
    onMediaClick: (Media) -> Unit,
    onEpisodeClick: (Show, Episode) -> Unit,
    state: UiState<List<PlaybackItem>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "CONTINUE WATCHING",
                style = SnacTypography.title,
                color = SnacColors.ink,
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
                    .silhouette(SnacColors.accentShadow),
            )
            if (state is UiState.Success) {
                Text(
                    text = pluralStringResource(
                        R.plurals.paused_count,
                        state.content.size,
                        state.content.size,
                    ),
                    style = SnacTypography.hand,
                    color = SnacColors.signal,
                    modifier = Modifier.alignByBaseline(),
                )
            }
        }

        val placeholder =
            remember { movableContentOf<Boolean> { scrollable -> Placeholder(scrollable) } }

        when (state) {
            is UiState.Error -> {
                ErrorBox(
                    description = state.error.message,
                    onRetry = state.refresh,
                ) {
                    placeholder(false)
                }
            }

            UiState.Loading -> placeholder(true)

            is UiState.Success -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.content, key = { it.id }) { item ->
                        PlaybackCard(
                            item = item,
                            onClick = {
                                when (item) {
                                    is MoviePlayback -> onMediaClick(item.media)
                                    is EpisodePlayback -> onEpisodeClick(
                                        item.media,
                                        item.episode,
                                    )
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlaybackCard(
    item: PlaybackItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val kind = when (item) {
        is MoviePlayback -> "Movie"
        is EpisodePlayback -> formatEpisodeCode(item.episode.season, item.episode.number)
    }
    val timeLeft = item.runtime?.times(1.0 - item.progress)
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .width(CardWidth)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
    ) {
        Box(
            modifier = Modifier
                .indication(
                    interactionSource,
                    SilhouettePressIndication(offset = FrameShadowOffset),
                )
                .background(SnacColors.film)
                .fillMaxWidth()
                .aspectRatio(16f / 9f),
        ) {
            Image(
                imageUrl = item.backdropUrl,
                modifier = Modifier.matchParentSize(),
            )
            PlayBadge(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 22.dp),
            )
            timeLeft?.let {
                Text(
                    text = stringResource(R.string.time_left, it.formatRuntime()),
                    style = SnacTypography.hand,
                    color = SnacColors.onFilm,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 18.dp),
                )
            }
            ProgressBar(
                progress = item.progress,
                color = SnacColors.signal,
                trackColor = SnacColors.mutedOnFilm,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(4.dp)
                    .fillMaxWidth()
                    .height(6.dp),
            )
        }
        Text(
            text = item.media.title.uppercase(),
            style = SnacTypography.subtitle,
            color = SnacColors.ink,
            maxLines = 1,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = "$kind · ${stringResource(R.string.paused_ago, item.pausedAt.formatRelative())}",
            style = SnacTypography.label,
            color = SnacColors.muted,
            maxLines = 1,
        )
    }
}

@Composable
private fun PlayBadge(modifier: Modifier = Modifier) {
    val triangleColor = SnacColors.onAccent
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(SnacColors.accent)
            .border(2.dp, SnacColors.outline, CircleShape),
    ) {
        Canvas(
            Modifier
                .padding(start = 3.dp)
                .size(14.dp)
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, size.height / 2)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path, triangleColor)
        }
    }
}

@Composable
private fun Placeholder(scrollable: Boolean) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState(), scrollable)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(3) {
            Column(Modifier.width(CardWidth)) {
                Box(
                    modifier = Modifier
                        .silhouette(offset = FrameShadowOffset)
                        .background(imageGradient())
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                )
                Box(
                    Modifier
                        .padding(top = 12.dp, bottom = 4.dp)
                        .width(140.dp)
                        .height(18.dp)
                        .background(SnacColors.placeholder)
                )
                Box(
                    Modifier
                        .width(180.dp)
                        .height(14.dp)
                        .background(SnacColors.placeholder)
                )
            }
        }
    }
}

private val CardWidth = 248.dp
private val FrameShadowOffset = DpOffset(4.dp, 4.dp)

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            ContinueWatchingSection(
                onMediaClick = {},
                onEpisodeClick = { _, _ -> },
                state = UiState.Success(PlaybackItem.PreviewSample),
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<List<PlaybackItem>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            ContinueWatchingSection(
                onMediaClick = {},
                onEpisodeClick = { _, _ -> },
                state = state,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
    }
}
