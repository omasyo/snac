package com.omasyo.snac.feature.charts.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.media.movie.Movie
import com.omasyo.snac.core.model.media.show.Show
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.LocalBottomBarPadding
import com.omasyo.snac.core.ui.components.TabRow
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.modifiers.Side
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.random.Random

enum class Media(val label: String) {
    Movie("Movie"), Show("Show")
}

@Composable
fun ChartsScreen(
    onMovieClick: (Movie) -> Unit,
    onShowClick: (Show) -> Unit,
    selectedMedia: Media,
    onSelectedMediaChange: (Media) -> Unit,
    selectedChart: Chart,
    onSelectedChartChange: (Chart) -> Unit,
    selectedPeriod: TimePeriod,
    onSelectedPeriodChange: (TimePeriod) -> Unit,
    selectorExpanded: Boolean,
//    results: List<>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SnacColors.page)
//            .bottomBarPadding() TODO
//            .statusBarsPadding()
//            .navigationBarsPadding()
        ,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Box(Modifier.weight(1f)) {
                Text(
                    text = "CHARTS",
                    style = SnacTypography.headline,
                    modifier = Modifier
                        .rotate(-3f)
                        .silhouette(SnacColors.signal),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                TabRow(
                    selectedTab = selectedMedia,
                    onSelectedTabChange = onSelectedMediaChange,
                    name = { it.label },
                )
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                ) {}
            }
        }
        ChartSelector(
            selectedChart = selectedChart,
            onSelectedChartChange = onSelectedChartChange,
            selectedPeriod = selectedPeriod,
            onSelectedPeriodChange = onSelectedPeriodChange,
            expanded = selectorExpanded,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        TopMediaCard(
            title = "FRANKENSTEIN",
            imageUrl = null,
            year = 2025,
            info = "61,000 plays",
            onClick = {},
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        MediaList()
        Spacer(Modifier.height(LocalBottomBarPadding.current.calculateBottomPadding()))
    }
}

@Composable
private fun TopMediaCard(
    title: String,
    imageUrl: String?,
    year: Int,
    info: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val indicationColor = SnacColors.signal
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .clickable(
                indication = SilhouettePressIndication({ indicationColor }, DpOffset(6.dp, 6.dp)),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .fillMaxWidth()
            .background(SnacColors.film)
            .padding(16.dp)
    ) {
        Image(
            imageUrl = imageUrl,
            modifier = Modifier
                .rotate(-3f)
                .silhouette(SnacColors.accent)
                .border(4.dp, SnacColors.chip)
                .background(SnacColors.muted)
                .size(128.dp, 192.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                style = SnacTypography.title,
                color = SnacColors.onFilm,
            )
            Text(
                text = year.toString(),
                style = SnacTypography.label,
                color = SnacColors.mutedOnFilm,
            )
            Text(
                text = info,
                style = SnacTypography.hand,
                color = SnacColors.accent,
            )
        }
    }
}

@Composable
private fun MediaList(
//    media: List<Media>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .film(
                side = Side.Start,
                crossEdgeInset = 12.dp,
                edgeInset = 12.dp,
            )
            .background(SnacColors.film),
    ) {
        items(100) {
            MediaCard(
                position = it + 2,
                title = "FRANKENSTEIN",
                imageUrl = null,
                year = 2025,
                info = "61,000 plays",
                onClick = {},
            )
        }
    }
}

@Composable
private fun MediaCard(
    position: Int,
    title: String,
    imageUrl: String?,
    year: Int,
    info: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = SnacColors.filmLine
    val indicationColor = SnacColors.accent
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 32.dp, end = 8.dp)
            .drawBehind {
                drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height))
            }
            .clickable(
                onClick = onClick,
                indication = remember { TintIndication { indicationColor } },
                interactionSource = null,
            )
            .padding(end = 8.dp)
            .padding(vertical = 8.dp),
    ) {
        val positionColor = when (position) {
            1 -> SnacColors.signal
            2, 3 -> SnacColors.onFilm
            else -> SnacColors.faint
        }
        Text(
            text = "$position",
            style = SnacTypography.title.copy(textAlign = TextAlign.End),
            color = positionColor,
            modifier = Modifier.widthIn(32.dp)
        )

        Image(
            imageUrl = imageUrl,
            modifier = Modifier
                .rotate(remember { Random.nextInt(-3, 3).toFloat() }) //TODO
                .border(2.dp, SnacColors.chip)
                .background(SnacColors.muted)
                .size(56.dp, 80.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = SnacTypography.subtitle,
                color = SnacColors.onFilm,
            )
            Text(
                text = year.toString(),
                style = SnacTypography.label,
                color = SnacColors.mutedOnFilm,
            )
            Text(
                text = info,
                style = SnacTypography.hand,
                color = SnacColors.accent,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var media by remember { mutableStateOf(Media.Movie) }
    var expanded by remember { mutableStateOf(true) }
    var selectedChart by remember { mutableStateOf(Chart.Trending) }
    var selectedPeriod by remember { mutableStateOf(TimePeriod.Weekly) }
    SnacTheme {
        ChartsScreen(
            onMovieClick = {},
            onShowClick = {},
            selectedMedia = media,
            onSelectedMediaChange = { media = it },
            selectedChart = selectedChart,
            onSelectedChartChange = {
                if (!expanded) {
                    expanded = true
                } else {
                    selectedChart = it
                    expanded = false
                }
            },
            selectedPeriod = selectedPeriod,
            onSelectedPeriodChange = {
                if (!expanded) {
                    expanded = true
                } else {
                    selectedPeriod = it
                    expanded = false
                }
            },
            selectorExpanded = expanded,
        )
    }
}