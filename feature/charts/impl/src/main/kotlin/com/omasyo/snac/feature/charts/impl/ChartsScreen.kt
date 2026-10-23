package com.omasyo.snac.feature.charts.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.Image
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.random.Random

enum class MediaTab(val label: String) {
    Movie("Movie"), Show("Show")
}

private class CollapseOnScrollDownConnection(
    private val thresholdPx: Float,
    private val onThresholdReached: () -> Unit,
) : NestedScrollConnection {
    private var accumulated = 0f

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        val dy = consumed.y
        when {
            dy < 0f -> {
                accumulated += -dy
                if (accumulated >= thresholdPx) {
                    onThresholdReached()
                    accumulated = 0f
                }
            }

            dy > 0f -> {
                accumulated = 0f
            }
        }
        return Offset.Zero
    }

    fun reset() {
        accumulated = 0f
    }
}


val ChartItem.info: String get() = "61,000 plays"

@Composable
fun ChartsScreen(
    onMediaClick: (Media) -> Unit,
    selectedMediaTab: MediaTab,
    onSelectedMediaChange: (MediaTab) -> Unit,
    selectedChart: Chart,
    onSelectedChartChange: (Chart) -> Unit,
    selectedPeriod: TimePeriod,
    onSelectedPeriodChange: (TimePeriod) -> Unit,
    results: List<ChartItem>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    var selectorExpanded by rememberSaveable { mutableStateOf(true) }

    val collapseThresholdPx = with(LocalDensity.current) { 56.dp.toPx() }
    val collapseConnection = remember(collapseThresholdPx) {
        CollapseOnScrollDownConnection(collapseThresholdPx) {
            selectorExpanded = false
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { isScrolling ->
                if (!isScrolling) collapseConnection.reset()
            }
    }


    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(collapseConnection)
            .background(SnacColors.page)
    ) {
        stickyHeader("header") {
            val background = SnacColors.page
            val border = SnacColors.rule


            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .drawBehind {
                        drawRect(background)

                        val dividerAlpha = if (listState.firstVisibleItemIndex > 0) {
                            1f
                        } else {
                            listState.firstVisibleItemScrollOffset / 40.dp.toPx()
                        }.coerceIn(0f, 1f)

                        drawLine(
                            color = border.copy(minOf(dividerAlpha, 1f)),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2.dp.toPx(),
                        )
                    }
                    .statusBarsPadding()
                    .padding(vertical = 16.dp),
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
                            selectedTab = selectedMediaTab,
                            onSelectedTabChange = onSelectedMediaChange,
                            name = { it.label },
                        )
                        Button(
                            onClick = { selectorExpanded = !selectorExpanded },
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f),
                        ) {

                        }
                    }
                }

                ChartSelector(
                    selectedChart = selectedChart,
                    onSelectedChartChange = {
                        if (!selectorExpanded) {
                            selectorExpanded = true
                        } else {
                            onSelectedChartChange(it)
                        }
                    },
                    selectedPeriod = selectedPeriod,
                    onSelectedPeriodChange = {
                        if (!selectorExpanded) {
                            selectorExpanded = true
                        } else {
                            onSelectedPeriodChange(it)
                        }
                    },
                    expanded = selectorExpanded,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
        if (results.isNotEmpty()) {
            item("top-media-card") {
                val item = results.first()
                TopMediaCard(
                    title = item.media.title,
                    imageUrl = item.media.imageUrl,
                    year = item.media.year,
                    info = item.info,
                    onClick = { onMediaClick(item.media) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
            itemsIndexed(results.drop(1), key = { _, item -> item.media.slug }) { index, item ->
                MediaCard(
                    position = index + 2,
                    title = item.media.title,
                    imageUrl = item.media.imageUrl,
                    year = item.media.year,
                    info = item.info,
                    onClick = { onMediaClick(item.media) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .film(
                            side = Side.Start,
                            crossEdgeInset = 12.dp,
                        )
                        .background(SnacColors.film)
                )
            }
        }
        item("bottom-padding") {
            Spacer(Modifier.bottomBarPadding())
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}

@Composable
private fun TopMediaCard(
    title: String,
    imageUrl: String?,
    year: Int,
    info: String?,
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
            if (info != null) {
                Text(
                    text = info,
                    style = SnacTypography.hand,
                    color = SnacColors.accent,
                )
            }
        }
    }
}

@Composable
private fun MediaCard(
    position: Int,
    title: String,
    imageUrl: String?,
    year: Int,
    info: String?,
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
            if (info != null) {
                Text(
                    text = info,
                    style = SnacTypography.hand,
                    color = SnacColors.accent,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var mediaTab by remember { mutableStateOf(MediaTab.Movie) }
    var selectedChart by remember { mutableStateOf(Chart.Trending) }
    var selectedPeriod by remember { mutableStateOf(TimePeriod.Weekly) }
    SnacTheme {
        ChartsScreen(
            onMediaClick = {},
            selectedMediaTab = mediaTab,
            onSelectedMediaChange = { mediaTab = it },
            selectedChart = selectedChart,
            onSelectedChartChange = { selectedChart = it },
            selectedPeriod = selectedPeriod,
            onSelectedPeriodChange = { selectedPeriod = it },
            results = TrendingItem.MoviePreviewSample,
        )
    }
}