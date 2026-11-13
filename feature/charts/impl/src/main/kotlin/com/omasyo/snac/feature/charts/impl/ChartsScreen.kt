package com.omasyo.snac.feature.charts.impl

import androidx.annotation.PluralsRes
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.CollectedItem
import com.omasyo.snac.core.model.media.FavoritedItem
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.PlayedItem
import com.omasyo.snac.core.model.media.PopularItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.model.media.WatchedItem
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.IconButton
import com.omasyo.snac.core.ui.components.TabRow
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.format.formatCount
import com.omasyo.snac.core.ui.format.formatRevenue
import com.omasyo.snac.core.ui.modifiers.Side
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import com.omasyo.snac.core.ui.R as UiR

enum class MediaTab(val label: String) {
    Movie("Movie"), Show("Show")
}

@Composable
fun ChartItem.info(): String = when (this) {
    is AnticipatedItem -> countLabel(R.plurals.chart_lists, listCount)
    is BoxOfficeItem -> revenue.formatRevenue()
    is FavoritedItem -> countLabel(R.plurals.chart_users, userCount)
    is PopularItem -> ""
    is TrendingItem -> countLabel(R.plurals.chart_watchers, watchers)
    is CollectedItem -> collectorCount?.let { countLabel(R.plurals.chart_collectors, it) }
        ?: countLabel(R.plurals.chart_collected, collectedCount)

    is PlayedItem -> countLabel(R.plurals.chart_plays, playCount)
    is WatchedItem -> countLabel(R.plurals.chart_watchers, watcherCount)
}

@Composable
private fun countLabel(@PluralsRes id: Int, count: Int): String =
    pluralStringResource(id, count, count.formatCount())

@Composable
fun ChartsScreen(
    onMediaClick: (Media) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChartViewModel = metroViewModel()
) {
    ChartsScreen(
        onMediaClick = onMediaClick,
        selectedMediaTab = viewModel.mediaTab.collectAsStateWithLifecycle().value,
        onSelectedMediaChange = viewModel::changeMediaTab,
        selectedChart = viewModel.selectedChart.collectAsStateWithLifecycle().value,
        onSelectedChartChange = viewModel::changeChart,
        selectedPeriod = viewModel.selectedPeriod.collectAsStateWithLifecycle().value,
        onSelectedPeriodChange = viewModel::changePeriod,
        results = viewModel.results.collectAsLazyPagingItems(),
        modifier = modifier,
    )
}

@Composable
fun ChartsScreen(
    onMediaClick: (Media) -> Unit,
    selectedMediaTab: MediaTab,
    onSelectedMediaChange: (MediaTab, anchorPosition: Int) -> Unit,
    selectedChart: Chart,
    onSelectedChartChange: (Chart, anchorPosition: Int) -> Unit,
    selectedPeriod: TimePeriod,
    onSelectedPeriodChange: (TimePeriod, anchorPosition: Int) -> Unit,
    results: LazyPagingItems<ChartItem>,
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


    val cardModifier = Modifier
        .padding(horizontal = 16.dp)
        .film(
            side = Side.Start,
            crossEdgeInset = 12.dp,
        )
        .background(SnacColors.film)

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(collapseConnection)
            .background(SnacColors.page)
    ) {
        stickyHeader(HeaderKey) {
            val background = SnacColors.page
            val border = SnacColors.rule

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .pointerInput(Unit) { }
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
                            text = "Charts".uppercase(),
                            style = SnacTypography.headline,
                            modifier = Modifier
                                .rotate(-3f)
                                .silhouette(SnacColors.signalShadow),
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    ) {
                        TabRow(
                            selectedTab = selectedMediaTab,
                            onSelectedTabChange = {
                                onSelectedMediaChange(it, listState.firstVisibleCardPosition())
                            },
                            name = { it.label },
                        )
                        IconButton(
                            icon = UiR.drawable.funnel_simple_bold,
                            contentDescription = stringResource(R.string.filters),
                            onClick = { selectorExpanded = !selectorExpanded },
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f),
                        )
                    }
                }

                ChartSelector(
                    selectedChart = selectedChart,
                    onSelectedChartChange = {
                        if (!selectorExpanded) {
                            selectorExpanded = true
                        } else {
                            val anchorPosition = listState.firstVisibleCardPosition()
                            if (it == Chart.BoxOffice) {
                                onSelectedMediaChange(MediaTab.Movie, anchorPosition)
                            }
                            onSelectedChartChange(it, anchorPosition)
                            selectorExpanded = it.hasTimePeriod
                        }
                    },
                    selectedPeriod = selectedPeriod,
                    onSelectedPeriodChange = {
                        if (!selectorExpanded) {
                            selectorExpanded = true
                        } else {
                            onSelectedPeriodChange(it, listState.firstVisibleCardPosition())
                            selectorExpanded = false
                        }
                    },
                    expanded = selectorExpanded,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        item { Spacer(Modifier.height(16.dp)) }

        when (val refreshState = results.loadState.refresh) {
            is LoadState.Loading -> {
                if (results.itemCount == 0) {
                    item("refresh-loading") { Placeholder() }
                }
            }

            is LoadState.Error -> {
                if (results.itemCount == 0) {
                    item("refresh-error") {
                        ErrorBox(
                            description = refreshState.error.localizedMessage
                                ?: "Failed to load charts.",
                            onRetry = { results.retry() },
                            alignment = Alignment.TopCenter,
                            errorPadding = PaddingValues(top = 120.dp)
                        ) { Placeholder() }
                    }
                }
            }

            is LoadState.NotLoading -> {}
        }

        items(
            count = results.itemCount,
            key = results.itemKey { it.media.slug },
            contentType = { position -> if (position == 0) "top-media-card" else "media-card" },
        ) { position ->
            val item = results[position]
            if (position == 0) {
                val cardModifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                if (item != null) {
                    TopMediaCard(
                        item = item,
                        onClick = { onMediaClick(item.media) },
                        modifier = cardModifier,
                    )
                } else {
                    TopMediaCardPlaceholder(cardModifier)
                }
            } else {
                val cardModifier = Modifier
                    .animateItem(
                        //TODO
                        fadeInSpec = null,
                        fadeOutSpec = null,
                    )
                    .then(cardModifier)
                if (item != null) {
                    MediaCard(
                        position = position + 1,
                        item = item,
                        onClick = { onMediaClick(item.media) },
                        modifier = cardModifier,
                    )
                } else {
                    MediaCardPlaceholder(
                        position = position + 1,
                        modifier = cardModifier,
                    )
                }
            }
        }

        when (val appendState = results.loadState.append) {
            is LoadState.Loading -> {
                item("append-loading") {
                    MediaCardPlaceholder(
                        position = results.itemCount,
                        modifier = cardModifier,
                    )
                }
            }

            is LoadState.Error -> {
                item("append-error") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Error loading more: ${appendState.error.localizedMessage ?: "Unknown error"}",
                            style = SnacTypography.caption,
                            color = SnacColors.signal,
                            modifier = Modifier.weight(1f),
                        )
                        Button(
                            text = "Retry",
                            onClick = { results.retry() },
                        )
                    }
                }
            }

            is LoadState.NotLoading -> {}
        }

        item("bottom-padding") {
            Spacer(Modifier.bottomBarPadding())
        }
    }
}

@Composable
private fun Placeholder() {
    Column {
        TopMediaCardPlaceholder(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
        )
        repeat(20) {
            MediaCardPlaceholder(
                position = it + 2,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .film(
                        side = Side.Start,
                        crossEdgeInset = 12.dp,
                    )
                    .background(SnacColors.film),
            )
        }
    }
}

private const val HeaderKey = "header"

private const val ChartItemsStart = 2

private fun LazyListState.firstVisibleCardPosition(): Int {
    val visibleItems = layoutInfo.visibleItemsInfo
    val headerBottom = visibleItems.firstOrNull { it.key == HeaderKey }
        ?.let { it.offset + it.size }
        ?: layoutInfo.viewportStartOffset
    val first = visibleItems.firstOrNull {
        it.index >= ChartItemsStart && it.offset + it.size > headerBottom
    } ?: return 0
    return first.index - ChartItemsStart
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

@Preview
@Composable
private fun PreviewContent() {
    var mediaTab by remember { mutableStateOf(MediaTab.Movie) }
    var selectedChart by remember { mutableStateOf(Chart.Trending) }
    var selectedPeriod by remember { mutableStateOf(TimePeriod.Weekly) }
    val results = remember {
        flowOf(PagingData.from<ChartItem>(TrendingItem.MoviePreviewSample))
    }.collectAsLazyPagingItems()

    SnacTheme {
        ChartsScreen(
            onMediaClick = {},
            selectedMediaTab = mediaTab,
            onSelectedMediaChange = { value, _ -> mediaTab = value },
            selectedChart = selectedChart,
            onSelectedChartChange = { value, _ -> selectedChart = value },
            selectedPeriod = selectedPeriod,
            onSelectedPeriodChange = { value, _ -> selectedPeriod = value },
            results = results,
        )
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    var mediaTab by remember { mutableStateOf(MediaTab.Movie) }
    var selectedChart by remember { mutableStateOf(Chart.Trending) }
    var selectedPeriod by remember { mutableStateOf(TimePeriod.Weekly) }
    val results = remember {
        flowOf(
            PagingData.empty<ChartItem>(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )
    }.collectAsLazyPagingItems()

    SnacTheme {
        ChartsScreen(
            onMediaClick = {},
            selectedMediaTab = mediaTab,
            onSelectedMediaChange = { value, _ -> mediaTab = value },
            selectedChart = selectedChart,
            onSelectedChartChange = { value, _ -> selectedChart = value },
            selectedPeriod = selectedPeriod,
            onSelectedPeriodChange = { value, _ -> selectedPeriod = value },
            results = results,
        )
    }
}

@Preview(device = "spec:width=1080px,height=1800px,dpi=440")
@Composable
private fun PreviewError() {
    var mediaTab by remember { mutableStateOf(MediaTab.Movie) }
    var selectedChart by remember { mutableStateOf(Chart.Trending) }
    var selectedPeriod by remember { mutableStateOf(TimePeriod.Weekly) }
    val results = remember {
        flowOf(
            PagingData.empty<ChartItem>(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Error(Exception("Couldn't get trending films")),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )
    }.collectAsLazyPagingItems()

    SnacTheme {
        ChartsScreen(
            onMediaClick = {},
            selectedMediaTab = mediaTab,
            onSelectedMediaChange = { value, _ -> mediaTab = value },
            selectedChart = selectedChart,
            onSelectedChartChange = { value, _ -> selectedChart = value },
            selectedPeriod = selectedPeriod,
            onSelectedPeriodChange = { value, _ -> selectedPeriod = value },
            results = results,
        )
    }
}
