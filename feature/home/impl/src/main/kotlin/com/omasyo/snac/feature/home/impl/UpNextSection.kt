package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Episode
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.UpNextItem
import com.omasyo.snac.core.ui.components.BackdropAspectRatio
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.ProgressBar
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.format.formatEpisodeCode
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
internal fun UpNextSection(
    onEpisodeClick: (Show, Episode) -> Unit,
    state: UiState<List<UpNextItem>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "UP NEXT",
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
                        R.plurals.up_next_shows,
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
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.content, key = { it.show.slug }) { item ->
                        UpNextCard(
                            item = item,
                            onClick = { onEpisodeClick(item.show, item.nextEpisode) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UpNextCard(
    item: UpNextItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val episode = item.nextEpisode
    UpNextCardLayout(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = SilhouettePressIndication(offset = CardShadowOffset),
            onClick = onClick,
        ),
        image = {
            Image(
                imageUrl = item.backdropUrl,
                modifier = Modifier.matchParentSize(),
            )
            Text(
                text = formatEpisodeCode(episode.season, episode.number),
                style = SnacTypography.tag,
                color = SnacColors.onAccent,
                modifier = Modifier
                    .background(SnacColors.accent)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            )
        },
        details = {
            Text(
                text = item.show.title.uppercase(),
                style = SnacTypography.subtitle,
                color = SnacColors.ink,
                maxLines = 1,
            )
            episode.title?.let {
                Text(
                    text = it,
                    style = SnacTypography.label,
                    color = SnacColors.ink,
                    maxLines = 1,
                )
            }
        },
        progress = {
            ProgressBar(
                progress = if (item.aired > 0) item.completed.toFloat() / item.aired else 0f,
                color = SnacColors.signal,
                trackColor = SnacColors.card,
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, SnacColors.outline)
                    .height(10.dp),
            )
            Text(
                text = "${item.completed}/${item.aired}",
                style = SnacTypography.hand,
                color = SnacColors.ink,
            )
        },
    )
}

@Composable
private fun UpNextCardLayout(
    image: @Composable BoxScope.() -> Unit,
    details: @Composable ColumnScope.() -> Unit,
    progress: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .width(CardWidth)
            .border(2.dp, SnacColors.outline)
            .background(SnacColors.card)
            .padding(8.dp),
    ) {
        Box(
            modifier = Modifier
                .border(2.dp, SnacColors.outline)
                .fillMaxWidth()
                .aspectRatio(BackdropAspectRatio),
            content = image,
        )
        Column(
            modifier = Modifier.padding(horizontal = 4.dp),
            content = details,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 4.dp),
            content = progress,
        )
    }
}

@Composable
private fun Placeholder(scrollable: Boolean) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState(), scrollable)
            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(3) {
            UpNextCardLayout(
                modifier = Modifier.silhouette(offset = CardShadowOffset),
                image = {
                    Box(
                        Modifier
                            .matchParentSize()
                            .background(imageGradient())
                    )
                },
                details = {
                    Box(
                        Modifier
                            .padding(vertical = 4.dp)
                            .width(120.dp)
                            .height(18.dp)
                            .background(SnacColors.placeholder)
                    )
                    Box(
                        Modifier
                            .width(80.dp)
                            .height(14.dp)
                            .background(SnacColors.placeholder)
                    )
                },
                progress = {
                    Box(
                        Modifier
                            .weight(1f)
                            .height(10.dp)
                            .background(SnacColors.placeholder)
                    )
                },
            )
        }
    }
}

private val CardWidth: Dp = 248.dp
private val CardShadowOffset = DpOffset(4.dp, 4.dp)

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            UpNextSection(
                onEpisodeClick = { _, _ -> },
                state = UiState.Success(UpNextItem.PreviewSample),
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<List<UpNextItem>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            UpNextSection(
                onEpisodeClick = { _, _ -> },
                state = state,
                modifier = Modifier.padding(vertical = 16.dp),
            )
        }
    }
}
