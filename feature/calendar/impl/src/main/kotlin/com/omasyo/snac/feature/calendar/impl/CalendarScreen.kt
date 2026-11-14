package com.omasyo.snac.feature.calendar.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.omasyo.snac.core.model.media.CalendarDay
import com.omasyo.snac.core.model.media.CalendarItem
import com.omasyo.snac.core.model.media.Episode
import com.omasyo.snac.core.model.media.EpisodeAiring
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.MovieRelease
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
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
import java.time.format.FormatStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
internal fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = metroViewModel(),
) {
    CalendarScreen(
        items = viewModel.items.collectAsLazyPagingItems(),
        onJumpToDate = viewModel::jumpToDate,
        modifier = modifier,
    )
}

@Composable
internal fun CalendarScreen(
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
                val loadedIndex = items.itemSnapshotList.items.indexOfDate(date)
                coroutineScope.launch {
                    if (loadedIndex != null) {
                        listState.animateScrollToItem(loadedIndex)
                    } else {
                        onJumpToDate(date)
                        // TODO MAYBE don't scroll immediately
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
            items = items,
            listState = listState,
        )
    }
}

@Composable
private fun CalendarList(
    items: LazyPagingItems<CalendarUiItem>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = listState,
        modifier = modifier.bleedVertically(36.dp),
    ) {
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
                is CalendarUiItem.Entry -> CalendarCard(item.item)
                null -> Unit
            }
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
    item: CalendarItem,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clickable(
                indication = remember { SilhouettePressIndication() },
                interactionSource = remember { MutableInteractionSource() },
            ) {}
            .border(2.dp, SnacColors.ink)
            .background(SnacColors.card)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            imageUrl = item.media.imageUrl,
            modifier = Modifier.aspectRatio(16f / 9f),
        )
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = item.media.title,
                style = SnacTypography.subtitle,
            )
            when (item) {
                is EpisodeAiring -> {
                    Text(
                        text = item.episodeLabel(),
                        style = SnacTypography.caption,
                    )
                    Text(
                        text = item.firstAired.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                        style = SnacTypography.caption,
                        color = SnacColors.muted,
                    )
                }

                is MovieRelease -> {
                    Text(
                        text = "Movie",
                        style = SnacTypography.caption,
                        color = SnacColors.muted,
                    )
                    // Empty third line, so movie cards are as tall as episode cards.
                    Text(
                        text = "",
                        style = SnacTypography.caption,
                        color = SnacColors.muted,
                    )
                }
            }
        }
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

private fun Episode.code() = "S%02dE%02d".format(season, number)

private fun EpisodeAiring.episodeLabel(): String =
    if (episodes.size > 1) {
        "${episodes.first().code()}–${episodes.last().code()} · ${episodes.size} episodes"
    } else {
        listOfNotNull(episode.code(), episode.title).joinToString(" · ")
    }

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
            items = items,
            onJumpToDate = {},
        )
    }
}
