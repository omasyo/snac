package com.omasyo.snac.feature.media.impl.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.data.models.Genre
import com.omasyo.snac.core.ui.R as UiR
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.IconButton
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R
import com.omasyo.snac.feature.media.impl.components.GenreChip
import com.omasyo.snac.feature.media.impl.components.GenreChipPlaceholder
import com.omasyo.snac.feature.media.impl.components.MediaInfoClapper
import com.omasyo.snac.feature.media.impl.components.MediaInfoClapperPlaceholder
import kotlin.random.Random

internal data class MediaHeroContent(
    val title: String,
    val posterUrl: String?,
    val secondaryPosterUrl: String?,
)

@Composable
internal fun MediaDetailsLayout(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(SnacColors.page)
                .bottomBarPadding()
                .navigationBarsPadding(),
            content = content,
        )
        IconButton(
            icon = UiR.drawable.caret_left_bold,
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
//                        .rotate(-4f)
                        .padding(horizontal = 32.dp, vertical = 32.dp)
                        .align(Alignment.BottomStart),
                ) {
//                    Box(
//                        modifier = Modifier
//                            .silhouette(SnacColors.signal)
//                            .border(4.dp, SnacColors.chip)
//                            .size(112.dp, 168.dp)
//                            .background(imageGradient())
//                    )
//                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                        Box(
//                            Modifier
//                                .size(280.dp, 40.dp)
//                                .background(SnacColors.faint)
//                        )
//                        Box(
//                            Modifier
//                                .size(200.dp, 40.dp)
//                                .background(SnacColors.faint)
//                        )
//                    }

                    Box(
                        Modifier
                            .size(240.dp, 40.dp)
                            .background(SnacColors.faint)
                    )
                }
            }

            is UiState.Success -> {
                val hero = state.content
                Image(
                    imageUrl = hero.secondaryPosterUrl ?: hero.posterUrl,
                    modifier = Modifier.matchParentSize(),
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
                                .silhouette(SnacColors.signal)
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
                            .silhouette(SnacColors.signal)
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
        is UiState.Success -> MediaInfoClapper(info = state.content, modifier = modifier)
        else -> MediaInfoClapperPlaceholder(modifier = modifier)
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
                            .background(SnacColors.faint)
                    )
                }
                Box(
                    Modifier
                        .width(160.dp)
                        .height(14.dp)
                        .background(SnacColors.faint)
                )
            }
        }
    }
}
