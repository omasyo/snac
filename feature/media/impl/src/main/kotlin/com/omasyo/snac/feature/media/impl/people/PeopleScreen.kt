package com.omasyo.snac.feature.media.impl.people

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.PeopleList
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.common.MediaDetailsLayout

private enum class Tab {
    Cast, Crew,
}

private val Tab.label
    get() = when (this) {
        Tab.Cast -> "Cast"
        Tab.Crew -> "Crew"
    }


internal data class HeroContent(
    val slug: String,
    val title: String,
    val posterUrl: String?,
    val secondaryPosterUrl: String?,
    val extra: String,
)

@Composable
internal fun PeopleScreen(
    hero: HeroContent,
    onBackClick: () -> Unit,
    onPersonClick: () -> Unit,
    peopleList: UiState<PeopleList>,
    modifier: Modifier,
) {
    var selectedTab by rememberSaveable { mutableStateOf(Tab.Cast) }

    MediaDetailsLayout(
        onBackClick = onBackClick,
        title = hero.title,
        hero = {
            Column {
                Hero(hero)
            }
        }
    ) { contentPadding ->
        val bottomPadding = contentPadding.calculateBottomPadding()
        Column(Modifier.padding(top = contentPadding.calculateTopPadding())) {
            CastCrewTab(
                selectedTab = selectedTab,
                onSelectedTabChange = { selectedTab = it },
                modifier = Modifier
                    .zIndex(1f)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )
            val contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 16.dp + bottomPadding,
            )
                when (selectedTab) {
                Tab.Cast -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 160.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = contentPadding,
                        modifier = Modifier
                            .fillMaxWidth(),
                    ) {
                        items(20) {
                            Box(
                                Modifier
                                    .aspectRatio(1f)
                                    .background(Color.Red)
                            )
                        }
                    }
                }

                Tab.Crew -> {
                    LazyColumn(
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(20) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                                    .background(Color.Red)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Hero(
    hero: HeroContent,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .film()
            .background(SnacColors.film)
    ) {
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
                        .size(74.dp, 108.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Cast + Crew",
                    style = SnacTypography.overline,
                    color = SnacColors.onAccent,
                    modifier = Modifier
                        .background(SnacColors.accent)
                        .padding(horizontal = 6.dp)
                )
                Text(
                    text = hero.title,
                    style = SnacTypography.headline.copy(
                        lineHeightStyle = CapHeightLineHeightStyle,
                        hyphens = Hyphens.Auto,
                    ),
                    color = SnacColors.onFilm,
                    modifier = Modifier
                        .silhouette(SnacColors.signalShadow)
                )
                Text(
                    text = hero.extra,
                    style = SnacTypography.caption,
                    color = SnacColors.mutedOnFilm
                )
            }
        }
    }
}

@Composable
private fun CastCrewTab(
    selectedTab: Tab,
    onSelectedTabChange: (Tab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .silhouette()
            .border(2.dp, SnacColors.outline)
            .selectableGroup()
    ) {
        for (tab in Tab.entries) {
            val selected = tab == selectedTab
            val indicationColor = SnacColors.accent
            Text(
                text = tab.label,
                color = if (selected) SnacColors.accent else SnacColors.ink,
                style = SnacTypography.subtitle.copy(textAlign = TextAlign.Center),
                modifier = Modifier
                    .weight(1f)
                    .background(if (selected) SnacColors.film else SnacColors.chip)
                    .selectable(
                        selected = selected,
                        interactionSource = null,
                        indication = TintIndication { indicationColor },
                        role = Role.Tab,
                        onClick = { onSelectedTabChange(tab) }
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

private val PreviewHero = HeroContent(
    slug = "slug",
    title = "Frankenstein",
    posterUrl = "",
    secondaryPosterUrl = "",
    extra = "2025 · Guillermo del Toro"
)

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        PeopleScreen(
            hero = PreviewHero,
            onBackClick = {},
            onPersonClick = {},
            peopleList = UiState.Success(
                PeopleList(
                    cast = Cast.PreviewSample,
                    crew = emptyMap(),
                )
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    SnacTheme {
        PeopleScreen(
            hero = PreviewHero,
            onBackClick = {},
            onPersonClick = {},
            peopleList = UiState.Loading,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    val error = UiState.Error(SnacResult.Error.Unexpected) {}
    SnacTheme {
        PeopleScreen(
            hero = PreviewHero,
            onBackClick = {},
            onPersonClick = {},
            peopleList = error,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
