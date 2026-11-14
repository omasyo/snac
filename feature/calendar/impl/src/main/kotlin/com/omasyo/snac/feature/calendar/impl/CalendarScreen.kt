package com.omasyo.snac.feature.calendar.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.node.Ref
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.omasyo.snac.core.common.SnacException
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.CalendarDay
import com.omasyo.snac.core.model.media.CalendarItem
import com.omasyo.snac.core.model.media.Episode
import com.omasyo.snac.core.model.media.EpisodeAiring
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.MovieRelease
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.format.formatEpisodeCode
import com.omasyo.snac.core.ui.format.formatLocalTime
import com.omasyo.snac.core.ui.format.metaLine
import com.omasyo.snac.core.ui.models.snacMessage
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.bleedVertically
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
internal fun CalendarScreen(
    onMovieClick: (Movie) -> Unit,
    onEpisodeClick: (EpisodeAiring) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = metroViewModel(),
) {
    CalendarScreen(
        onMovieClick = onMovieClick,
        onEpisodeClick = onEpisodeClick,
        items = viewModel.items.collectAsLazyPagingItems(),
        onJumpToDate = viewModel::jumpToDate,
        modifier = modifier,
    )
}

@Composable
internal fun CalendarScreen(
    onMovieClick: (Movie) -> Unit,
    onEpisodeClick: (EpisodeAiring) -> Unit,
    items: LazyPagingItems<CalendarUiItem>,
    onJumpToDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val dateStripListState = rememberDateStripListState()
    val listState = rememberLazyListState()

    var tappedDate by remember { mutableStateOf<LocalDate?>(null) }
    val selectedDate by remember(items) {
        derivedStateOf {
            val topItem = items.itemSnapshotList.getOrNull(listState.firstVisibleItemIndex)
            tappedDate ?: topItem?.date ?: Today
        }
    }
    LaunchedEffect(listState) {
        listState.interactionSource.interactions
            .filterIsInstance<DragInteraction.Start>()
            .collect { tappedDate = null }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SnacColors.page),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "Calendar".uppercase(),
                style = SnacTypography.headline,
                modifier = Modifier
                    .rotate(-3f)
                    .silhouette(SnacColors.signalShadow),
            )
        }

        DateStrip(
            selectedDate = selectedDate,
            onDateTap = { date ->
                tappedDate = date
                val loadedIndex =
                    items.itemSnapshotList.items.indexOfFirst { it is CalendarUiItem.Header && it.date == date }
                coroutineScope.launch {
                    if (loadedIndex >= 0) {
                        listState.animateScrollToItem(loadedIndex)
                    } else {
                        onJumpToDate(date)
                        // TODO MAYBE don't scroll immediately or invalidate results
                        listState.scrollToItem(0)
                    }
                }
            },
            listState = dateStripListState,
            modifier = Modifier
                .zIndex(1f)
                .rotate(-1.5f),
        )

        CalendarList(
            onMovieClick = onMovieClick,
            onEpisodeClick = onEpisodeClick,
            items = items,
            listState = listState,
        )
    }
}

@Composable
private fun CalendarList(
    onMovieClick: (Movie) -> Unit,
    onEpisodeClick: (EpisodeAiring) -> Unit,
    items: LazyPagingItems<CalendarUiItem>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = listState,
        modifier = modifier.bleedVertically(32.dp),
    ) {
        when (val refreshState = items.loadState.refresh) {
            is LoadState.Loading -> {
                if (items.itemCount == 0) {
                    item("refresh-loading") { CalendarDayPlaceholder() }
                }
            }

            is LoadState.Error -> {
                if (items.itemCount == 0) {
                    item("refresh-error") {
                        ErrorBox(
                            description = refreshState.error.snacMessage,
                            onRetry = items::retry,
                            alignment = Alignment.TopCenter,
                            errorPadding = PaddingValues(top = 48.dp),
                        ) {
                            CalendarDayPlaceholder()
                        }
                    }
                }
            }

            is LoadState.NotLoading -> {}
        }

        when (items.loadState.prepend) {
            is LoadState.Loading -> item("prepend-loading") { CalendarCardPlaceholder() }
            is LoadState.Error -> item("prepend-error") { RetryButton(onRetry = items::retry) }
            is LoadState.NotLoading -> {}
        }

        items(
            count = items.itemCount,
            key = items.itemKey(CalendarUiItem::key),
            contentType = items.itemContentType {
                when (it) {
                    is CalendarUiItem.Header -> "header"
                    is CalendarUiItem.Entry -> "entry"
                }
            },
        ) { index ->
            when (val item = items[index]) {
                is CalendarUiItem.Header -> DateHeader(item.date)
                is CalendarUiItem.Entry -> CalendarCard(
                    title = item.item.title,
                    subtitle = item.item.subtitle,
                    caption = item.item.caption,
                    imageUrl = item.item.media.imageUrl,
                    onClick = {
                        when (item.item) {
                            is EpisodeAiring -> onEpisodeClick(item.item)
                            is MovieRelease -> onMovieClick(item.item.media)
                        }
                    },
                )

                null -> Unit
            }
        }
        when (items.loadState.append) {
            is LoadState.Loading -> item("append-loading") { CalendarCardPlaceholder() }
            is LoadState.Error -> item("append-error") { RetryButton(onRetry = items::retry) }
            is LoadState.NotLoading -> {}
        }

        item {
            Spacer(
                Modifier
                    .bottomBarPadding()
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
private fun CalendarCard(
    title: String,
    subtitle: String,
    caption: String?,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clickable(
                indication = remember { SilhouettePressIndication() },
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .border(2.dp, SnacColors.ink)
            .background(SnacColors.card)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            imageUrl = imageUrl,
            modifier = Modifier.aspectRatio(16f / 9f),
        )
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                maxLines = 1,
                style = SnacTypography.subtitle,
            )
            Text(
                text = subtitle,
                maxLines = 1,
                style = SnacTypography.caption,
            )
            Text(
                text = caption.orEmpty(),
                maxLines = 1,
                style = SnacTypography.caption,
                color = SnacColors.muted,
            )
        }
    }
}

@Composable
private fun RetryButton(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth(),
    ) {
        Button(text = "Retry", onClick = onRetry)
    }
}

@Composable
private fun DateHeader(
    date: LocalDate,
    modifier: Modifier = Modifier,
) {
    Text(
        text = date.formatted().uppercase(),
        style = SnacTypography.title,
        modifier = modifier.silhouette(SnacColors.accent),
    )
}

private val CalendarItem.title: String
    get() = media.title

private val CalendarItem.subtitle: String
    get() = when (this) {
        is EpisodeAiring -> if (episodes.size > 1) {
            metaLine(
                "${episodes.first().code()}–${episodes.last().code()}",
                "${episodes.size} episodes"
            )
        } else {
            metaLine(episode.code(), episode.title)
        }

        is MovieRelease -> "Movie"
    }

private val CalendarItem.caption: String?
    get() = when (this) {
        is EpisodeAiring -> firstAired.formatLocalTime()

        is MovieRelease -> null
    }

private fun Episode.code() = formatEpisodeCode(season, number)

private fun LocalDate.formatted(): String {
    val locale = Locale.getDefault()
    val today = LocalDate.now()

    fun fmt(pattern: String) = format(DateTimeFormatter.ofPattern(pattern, locale))
    fun LocalDate.weekStart() = with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    return when {
        this == today -> "Today ${fmt("EEE d")}"             // Today Fri 13
        year != today.year -> fmt("d MMM yyyy")              // 3 Mar 2027
        weekStart() == today.weekStart() -> fmt("EEE d")     // Wed 11
        else -> fmt("d MMM")                                 // 27 Nov
    }
}

@Preview
@Composable
private fun Preview() {
    val items = remember {
        val days = List(4) { dayOffset ->
            val date = Today.plusDays(dayOffset.toLong())
            CalendarDay(
                date = date,
                items = listOf(
                    MovieRelease(Movie("movie-$dayOffset", "Frankenstein", 2025, null), date),
                    EpisodeAiring(
                        media = Show("show-$dayOffset", "The Long Static", 2024, null),
                        episode = Episode(3, dayOffset + 1, "Carrier Wave"),
                        episodes = emptyList(),
                        firstAired = date.atTime(21, 0).atZone(ZoneId.systemDefault()).toInstant(),
                        date = date,
                    ),
                ),
            )
        }
        flowOf(PagingData.from(days).toUiItems())
    }.collectAsLazyPagingItems()

    SnacTheme {
        CalendarScreen(
            onMovieClick = {},
            onEpisodeClick = {},
            items = items,
            onJumpToDate = {},
        )
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    val loading =
        LoadStates(LoadState.Loading, LoadState.NotLoading(false), LoadState.NotLoading(false))
    val items =
        remember { flowOf(PagingData.empty<CalendarUiItem>(loading)) }.collectAsLazyPagingItems()
    SnacTheme {
        CalendarScreen(
            onMovieClick = {},
            onEpisodeClick = {},
            items = items,
            onJumpToDate = {},
        )
    }
}

@Preview
@Composable
private fun ErrorPreview() {
    val error = LoadStates(
        LoadState.Error(SnacException(SnacResult.Error.NoConnection)),
        LoadState.NotLoading(false),
        LoadState.NotLoading(false),
    )
    val items =
        remember { flowOf(PagingData.empty<CalendarUiItem>(error)) }.collectAsLazyPagingItems()
    SnacTheme {
        CalendarScreen(
            onMovieClick = {},
            onEpisodeClick = {},
            items = items,
            onJumpToDate = {},
        )
    }
}
