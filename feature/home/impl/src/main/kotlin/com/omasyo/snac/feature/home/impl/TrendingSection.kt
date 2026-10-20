package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.media.movie.TrendingMovie
import com.omasyo.snac.core.model.media.show.TrendingShow
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.LocalSilhouetteColor
import com.omasyo.snac.core.ui.modifiers.Orientation
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.bleedHorizontally
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.random.Random

internal enum class TrendingTab(val label: String) {
    Movie("Movie"), Show("Show")
}

@Composable
internal fun TrendingSection(
    onMovieClick: (TrendingMovie) -> Unit,
    onShowClick: (TrendingShow) -> Unit,
    selectedTab: TrendingTab,
    onTabChange: (TrendingTab) -> Unit,
    movies: List<TrendingMovie>,
    shows: List<TrendingShow>,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalSilhouetteColor provides SnacColors.signal
    ) {
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
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "TRENDING",
                    style = SnacTypography.title,
                    color = SnacColors.onFilm,
                    modifier = Modifier
                        .silhouette(),
                )
                Spacer(Modifier.weight(1f))
                Toggle(selectedTab, onTabChange)
            }

            when (selectedTab) {
                TrendingTab.Movie -> Row(movies, slug = { it.slug }) { movie, rotation ->
                    TrendingCard(
                        title = movie.title,
                        watchingCount = movie.watchingCount,
                        imageUrl = movie.imageUrl,
                        onClick = { onMovieClick(movie) },
                        modifier = Modifier
                            .padding(16.dp)
                            .rotate(rotation)
                    )
                }

                TrendingTab.Show -> Row(shows, slug = { it.slug }) { show, rotation ->
                    TrendingCard(
                        title = show.title,
                        watchingCount = show.watchingCount,
                        imageUrl = show.imageUrl,
                        onClick = { onShowClick(show) },
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
private fun Toggle(
    selectedTab: TrendingTab,
    onTabClick: (TrendingTab) -> Unit,
) {
    Row(
        modifier = Modifier.border(2.dp, SnacColors.onFilm)
    ) {
        for (tab in TrendingTab.entries) {
            val selected = tab == selectedTab
            val indicationColor = SnacColors.accent
            Text(
                text = tab.label,
                color = if (selected) SnacColors.ink else SnacColors.onFilm,
                modifier = Modifier
                    .background(if (selected) SnacColors.accent else Color.Transparent)
                    .clickable(
                        interactionSource = null,
                        indication = TintIndication { indicationColor },
                        onClick = { onTabClick(tab) }
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun <T> Row(
    items: List<T>,
    slug: (T) -> String,
    content: @Composable (T, Float) -> Unit,
) {
    LazyRow {
        items(items, key = slug) { item ->
            val rotation = Random(slug(item).hashCode()).nextInt(-2, 3).toFloat()

            content(item, rotation)
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
private fun Preview() {
    var trendingTab by remember { mutableStateOf(TrendingTab.Movie) }
    SnacTheme {
        TrendingSection(
            onMovieClick = {},
            onShowClick = {},
            selectedTab = trendingTab,
            onTabChange = { trendingTab = it },
            movies = List(10) {
                TrendingMovie(
                    slug = "movie-$it",
                    title = "Frankenstein",
                    watchingCount = 1204,
                    imageUrl = null,
                )
            },
            shows = List(10) {
                TrendingShow(
                    slug = "show-$it",
                    title = "Dark",
                    watchingCount = 300,
                    imageUrl = null,
                )
            },
            modifier = Modifier
                .rotate(-1.5f),
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