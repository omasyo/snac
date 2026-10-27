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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
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
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random

enum class MediaTab(val label: String) {
    Movie("Movie"), Show("Show")
}

val ChartItem.info: String
    get() = when (this) {
        is AnticipatedItem -> "$listCount lists"
        is BoxOfficeItem -> "$revenue"
        is FavoritedItem -> "$userCount users"
        is PopularItem -> ""
        is TrendingItem -> "$watchers watchers"
        is CollectedItem -> "$collectedCount watchers"  //TODO shows have collectors, which seems more important
        is PlayedItem -> "$playCount watchers"
        is WatchedItem -> "$watcherCount watchers"
    }

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
                            onSelectedTabChange = {
                                onSelectedMediaChange(it, listState.firstVisibleCardPosition())
                            },
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
                            val anchorPosition = listState.firstVisibleCardPosition()
                            if (it == Chart.BoxOffice) onSelectedMediaChange(MediaTab.Movie, anchorPosition)
                            onSelectedChartChange(it, anchorPosition)
                        }
                    },
                    selectedPeriod = selectedPeriod,
                    onSelectedPeriodChange = {
                        if (!selectorExpanded) {
                            selectorExpanded = true
                        } else {
                            onSelectedPeriodChange(it, listState.firstVisibleCardPosition())
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
                    item("refresh-loading") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Loading...",
                                style = SnacTypography.subtitle,
                                color = SnacColors.faint,
                            )
                        }
                    }
                }
            }

            is LoadState.Error -> {
                if (results.itemCount == 0) {
                    item("refresh-error") {
                        ErrorBox(
                            description = refreshState.error.localizedMessage
                                ?: "Failed to load charts.",
                            onRetry = { results.retry() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(SnacColors.film)
                            )
                        }
                    }
                }
            }

            is LoadState.NotLoading -> {}
        }

        items(
            count = results.itemCount,
            key = results.itemKey { it.media.slug },
//            key = { position -> results.peek(position)?.media?.slug ?: position },
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
                    .animateItem()
                    .padding(horizontal = 16.dp)
                    .film(
                        side = Side.Start,
                        crossEdgeInset = 12.dp,
                    )
                    .background(SnacColors.film)
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Loading more...",
                            style = SnacTypography.caption,
                            color = SnacColors.faint,
                        )
                    }
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
            Spacer(Modifier.navigationBarsPadding())
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

@Composable
private fun TopMediaCardPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SnacColors.film)
            .padding(16.dp)
    ) {
        Box(
            Modifier
                .rotate(-3f)
                .background(SnacColors.muted)
                .size(128.dp, 192.dp)
        )
    }
}

@Composable
private fun TopMediaCard(
    item: ChartItem,
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
            imageUrl = item.media.imageUrl,
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
                text = item.media.title,
                style = SnacTypography.title,
                color = SnacColors.onFilm,
            )
            Text(
                text = item.media.year.toString(),
                style = SnacTypography.label,
                color = SnacColors.mutedOnFilm,
            )
            Text(
                text = item.info,
                style = SnacTypography.hand,
                color = SnacColors.accent,
            )
        }
    }
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

@Composable
private fun MediaCard(
    position: Int,
    item: ChartItem,
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
            imageUrl = item.media.imageUrl,
            modifier = Modifier
                .rotate(remember(item.media.slug) {
                    Random(item.media.slug.hashCode()).nextInt(
                        -3,
                        3
                    ).toFloat()
                })
                .border(2.dp, SnacColors.chip)
                .background(SnacColors.muted)
                .size(56.dp, 80.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = item.media.title,
                style = SnacTypography.subtitle,
                color = SnacColors.onFilm,
            )
            Text(
                text = item.media.year.toString(),
                style = SnacTypography.label,
                color = SnacColors.mutedOnFilm,
            )
            Text(
                text = item.info,
                style = SnacTypography.hand,
                color = SnacColors.accent,
            )
        }
    }
}

@Composable
private fun MediaCardPlaceholder(
    position: Int,
    modifier: Modifier = Modifier,
) {
    val borderColor = SnacColors.filmLine
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 32.dp, end = 8.dp)
            .drawBehind {
                drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height))
            }
            .padding(end = 8.dp)
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = "$position",
            style = SnacTypography.title.copy(textAlign = TextAlign.End),
            color = SnacColors.faint,
            modifier = Modifier.widthIn(32.dp)
        )
        Box(
            Modifier
                .background(SnacColors.muted)
                .size(56.dp, 80.dp)
        )
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

@Preview
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
