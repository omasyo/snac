package com.omasyo.snac.feature.home.impl

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
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
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

internal enum class TrendingTab(val label: String) {
    Movie("Movie"), Show("Show")
}

@Composable
internal fun TrendingSection(
    onMediaClick: (Media) -> Unit,
    moviesState: UiState<List<TrendingItem>>,
    showsState: UiState<List<TrendingItem>>,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableStateOf(TrendingTab.Movie) }
    val stateHolder = rememberSaveableStateHolder()

    CompositionLocalProvider(LocalSilhouetteColor provides SnacColors.signal) {
        Column(
            modifier = modifier
                .bleedHorizontally(8.dp)
                .film(
                    orientation = Orientation.Horizontal,
                    crossEdgeInset = 16.dp
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
                        .weight(1f)
                        .silhouette(),
                )
                MediaToggle(
                    selectedTab = selectedTab,
                    onTabClick = { selectedTab = it }
                )
            }

            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    val towards =
                        if (targetState > initialState) AnimatedContentTransitionScope.SlideDirection.Left
                        else AnimatedContentTransitionScope.SlideDirection.Right
                    slideIntoContainer(towards) togetherWith slideOutOfContainer(towards)
                },
                label = "trending",
            ) { tab ->

                val items = when (tab) {
                    TrendingTab.Movie -> moviesState
                    TrendingTab.Show -> showsState
                }


                val placeholder =
                    remember { movableContentOf<Boolean> { scrollable -> PlaceholderRow(scrollable) } }

                stateHolder.SaveableStateProvider(tab) {
                    when (items) {
                        is UiState.Error -> {
                            ErrorBox(
                                description = items.error.message,
                                onRetry = items.refresh,
                            ) {
                                placeholder(false)
                            }
                        }


                        UiState.Loading -> placeholder(true)

                        is UiState.Success -> {
                            Row(items.content, onMediaClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaToggle(
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
private fun Row(
    items: List<TrendingItem>,
    onMediaClick: (Media) -> Unit,
) {
    LazyRow {
        items(items, key = { it.media.slug }) { item ->
            val media = item.media
            val rotation = Random(media.slug.hashCode()).nextInt(-2, 3).toFloat()

            TrendingCard(
                title = media.title,
                watchingCount = item.watchers,
                imageUrl = media.imageUrl,
                onClick = { onMediaClick(media) },
                modifier = Modifier
                    .padding(16.dp)
                    .rotate(rotation)
            )
        }
    }
}

@Composable
private fun PlaceholderRow(scrollable: Boolean) {
    Row(Modifier.horizontalScroll(rememberScrollState(), scrollable)) {
        repeat(10) {
            val rotation = Random(it + 80).nextInt(-2, 3).toFloat()

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .rotate(rotation),
            ) {
                Box(
                    modifier = Modifier
                        .border(4.dp, SnacColors.chip)
                        .background(imageGradient())
                        .size(120.dp, 180.dp),
                )

                Box(
                    Modifier
                        .padding(vertical = 8.dp)
                        .width(96.dp)
                        .height(16.dp)
                        .background(SnacColors.faint)
                )
                Box(
                    Modifier
                        .width(104.dp)
                        .height(12.dp)
                        .background(SnacColors.faint)
                )
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
        modifier = modifier
            .width(120.dp)
            .clickable(
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
                .height(180.dp)
                .fillMaxWidth(),
        )
        Text(
            text = title,
            style = SnacTypography.subtitle,
            color = SnacColors.onFilm,
            maxLines = 1,

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
    SnacTheme {
        TrendingSection(
            onMediaClick = {},
            moviesState = UiState.Success(
                listOf(
                    TrendingItem(
                        Movie(
                            "movie-",
                            "Frankenstein Frankenstein",
                            2025,
                            null
                        ), watchers = 1204
                    )
                ) + TrendingItem.MoviePreviewSample
            ),
            showsState = UiState.Success(TrendingItem.ShowPreviewSample),
            modifier = Modifier
                .rotate(-1.5f),
        )
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var moviesState by remember { mutableStateOf<UiState<List<TrendingItem>>>(UiState.Loading) }
    var showsState by remember { mutableStateOf<UiState<List<TrendingItem>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(2.seconds)
        moviesState = UiState.Error(SnacResult.Error.Unexpected) {}
        delay(2.seconds)
        showsState = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        TrendingSection(
            onMediaClick = {},
            moviesState = moviesState,
            showsState = showsState,
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