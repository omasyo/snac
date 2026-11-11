package com.omasyo.snac.feature.media.impl.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.data.models.Genre
import com.omasyo.snac.core.ui.R
import com.omasyo.snac.core.ui.components.ClapperInfo
import com.omasyo.snac.core.ui.components.CollapsibleHeaderLayout
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.IconButton
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.InfoClapper
import com.omasyo.snac.core.ui.components.InfoClapperPlaceholder
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.components.GenreChip
import com.omasyo.snac.feature.media.impl.components.GenreChipPlaceholder
import kotlin.random.Random

internal data class MediaHeroContent(
    val title: String,
    val posterUrl: String?,
    val secondaryPosterUrl: String?,
)

@Composable
internal fun MediaDetailsLayout(
    onBackClick: () -> Unit,
    title: String,
    hero: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier) {
        CollapsibleHeaderLayout(
            hero = hero,
            topBar = {
                TopBar(
                    title = title,
                    onBackClick = onBackClick,
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(SnacColors.page),
        ) {
            content()
        }
        IconButton(
            icon = R.drawable.caret_left_bold,
            contentDescription = stringResource(R.string.back),
            onClick = onBackClick,
            backgroundColor = SnacColors.chip,
            contentColor = SnacColors.ink,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 32.dp, top = 16.dp)
                .size(48.dp),
        )
    }


}

@Composable
private fun TopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(SnacColors.page)
            .statusBarsPadding()
            .padding(16.dp),
    ) {

        IconButton(
            icon = R.drawable.caret_left_bold,
            contentDescription = stringResource(R.string.back),
            onClick = onBackClick,
            backgroundColor = SnacColors.chip,
            contentColor = SnacColors.ink,
            modifier = Modifier
                .size(48.dp),
        )
        Text(
            text = title,
            style = SnacTypography.title,
            maxLines = 1,
        )
    }
}

@Composable
internal fun MediaHero(
    state: UiState<MediaHeroContent>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(13f / 18f)
            .film()
            .background(SnacColors.film)
    ) {
        when (state) {
            is UiState.Error -> {
                ErrorBox(
                    description = state.error.message,
                    onRetry = state.refresh,
                    modifier = Modifier.matchParentSize(),
                ) {
                }
            }

            UiState.Loading -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .rotate(-4f)
                        .padding(horizontal = 32.dp, vertical = 32.dp)
                        .align(Alignment.BottomStart),
                ) {
                    Box(
                        modifier = Modifier
                            .silhouette(SnacColors.signal)
                            .border(4.dp, SnacColors.chip)
                            .size(112.dp, 168.dp)
                            .background(imageGradient())
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            Modifier
                                .size(280.dp, 40.dp)
                                .background(SnacColors.faint)
                        )
                        Box(
                            Modifier
                                .size(200.dp, 40.dp)
                                .background(SnacColors.faint)
                        )
                    }
                }
            }

            is UiState.Success -> {
                val hero = state.content

                val brush = Brush.linearGradient(
                    listOf(
                        Color.Transparent,
                        Color.Transparent,
                        SnacColors.film.copy(0.5f),
                    ), end = Offset(0f, Float.POSITIVE_INFINITY)
                )
                Image(
                    imageUrl = hero.secondaryPosterUrl ?: hero.posterUrl,
                    modifier = Modifier
                        .matchParentSize()
                        .drawWithContent {
                            drawContent()
                            drawRect(brush)
                        },
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .rotate(if (hero.secondaryPosterUrl != null) -4f else 0f)
                        .padding(horizontal = 32.dp, vertical = 32.dp)
                        .align(Alignment.BottomStart),
                ) {
                    if (hero.secondaryPosterUrl != null) {
                        Image(
                            imageUrl = hero.posterUrl,
                            modifier = Modifier
                                .silhouette(SnacColors.signalShadow)
                                .border(4.dp, SnacColors.chip)
                                .size(112.dp, 168.dp),
                        )
                    }
                    Text(
                        text = hero.title,
                        style = SnacTypography.display.copy(
                            lineHeightStyle = CapHeightLineHeightStyle,
                            hyphens = Hyphens.Auto,
                        ),
                        color = SnacColors.onFilm,
                        modifier = Modifier
                            .weight(1f)
                            .silhouette(SnacColors.signalShadow)
                    )
                }
            }
        }
    }
}

@Composable
internal fun MediaTagline(
    state: UiState<String>,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is UiState.Success -> {
            Text(
                text = state.content,
                style = SnacTypography.hand,
                color = SnacColors.signal,
                modifier = modifier,
            )
        }

        else -> {
            Box(
                modifier
                    .size(200.dp, 16.dp)
                    .background(SnacColors.signal.copy(0.4f))
            )
        }
    }
}

@Composable
internal fun MediaInfo(
    state: UiState<List<Pair<String, String>>>,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is UiState.Success -> InfoClapper(
            info = state.content.map { (label, value) -> ClapperInfo(label, value) },
            modifier = modifier,
        )

        else -> InfoClapperPlaceholder(modifier = modifier)
    }
}

@Composable
internal fun MediaGenres(
    state: UiState<List<Genre>>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        when (state) {
            is UiState.Success -> state.content.forEach {
                val rotation = remember(it) { Random(it.hashCode()).nextInt(-3, 3).toFloat() }
                GenreChip(label = it.name, modifier = Modifier.rotate(rotation))
            }

            else -> repeat(3) {
                val rotation = remember(it) { Random(it + 80).nextInt(-3, 3).toFloat() }
                GenreChipPlaceholder(Modifier.rotate(rotation))
            }
        }
    }
}

@Composable
internal fun MediaOverview(
    state: UiState<String>,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is UiState.Success -> {
            Text(
                text = state.content,
                style = SnacTypography.body,
                modifier = modifier,
            )
        }

        else -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier,
            ) {
                repeat(4) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(14.dp)
                            .background(SnacColors.placeholder)
                    )
                }
                Box(
                    Modifier
                        .width(160.dp)
                        .height(14.dp)
                        .background(SnacColors.placeholder)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TopBarPreview() {
    SnacTheme {
        TopBar(
            title = "Maria Says Hello!",
            onBackClick = {},
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        MediaDetailsLayout(
            onBackClick = {},
            title = "Maria Says Hello!",
            hero = {
                Column(Modifier.height(500.dp)) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.Blue)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.Green)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.Yellow)
                    )
                }
            },
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(20) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}