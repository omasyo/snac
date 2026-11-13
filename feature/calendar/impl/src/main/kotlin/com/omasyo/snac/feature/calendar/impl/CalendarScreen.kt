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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.bleedVertically
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

sealed interface CalendarUiItem {
    data class Date(val date: LocalDate) : CalendarUiItem

    data class Content(val text: String) : CalendarUiItem

    companion object {

        val MockData: List<CalendarUiItem> = listOf(
            Date(LocalDate.of(2026, 10, 27)),
            Content("Quarterly review prep"),
            Content("Haircut at 6 PM"),

            Date(LocalDate.of(2026, 10, 28)),
            Content("Quarterly review"),
            Content("Team dinner"),

            Date(LocalDate.of(2026, 10, 29)),
            Content("Follow-up notes from review"),

            Date(LocalDate.of(2026, 11, 2)),
            Content("Team standup"),
            Content("Review Q4 roadmap draft"),
            Content("Dentist appointment at 4:30 PM"),

            Date(LocalDate.of(2026, 11, 3)),
            Content("Lunch with Amaka"),
            Content("Pay electricity bill"),

            Date(LocalDate.of(2026, 11, 4)),
            Content("Design sync with Tunde"),

            Date(LocalDate.of(2026, 11, 5)),
            Content("1:1 with manager"),
            Content("Gym — leg day"),
            Content("Read 30 pages"),

            Date(LocalDate.of(2026, 11, 7)),
            Content("Grocery run"),
            Content("Call the landlord about the leaking tap"),

            Date(LocalDate.of(2026, 11, 8)),
            Content("Church"),
            Content("Meal prep for the week"),

            Date(LocalDate.of(2026, 11, 12)),
            Content("Code freeze"),
            Content("Fix release blockers"),

            Date(LocalDate.of(2026, 11, 13)),
            Content("Sprint demo"),
            Content("Submit expense report"),
            Content("Pick up dry cleaning"),
            Content("Movie night"),

            Date(LocalDate.of(2026, 11, 14)),
            Content("Morning run, 5km"),
            Content("Visit Chioma's new place"),

            Date(LocalDate.of(2026, 11, 15)),
            Content("Laundry"),

            Date(LocalDate.of(2026, 11, 18)),
            Content("Pack for Abuja trip"),
            Content("Confirm hotel booking"),

            Date(LocalDate.of(2026, 11, 19)),
            Content("Print conference badge"),

            Date(LocalDate.of(2026, 11, 20)),
            Content("Flight to Abuja, 9:15 AM"),
            Content("Check in at hotel"),
            Content("Networking dinner"),

            Date(LocalDate.of(2026, 11, 21)),
            Content("Conference day 1"),
            Content("Give talk at 2 PM"),

            Date(LocalDate.of(2026, 11, 22)),
            Content("Conference day 2"),
            Content("Flight back to Lagos, 7:40 PM"),

            Date(LocalDate.of(2026, 11, 25)),
            Content("Write conference recap"),

            Date(LocalDate.of(2026, 11, 28)),
            Content("Mum's birthday 🎂"),
            Content("Book restaurant for dinner"),

            Date(LocalDate.of(2026, 11, 29)),
            Content("Family lunch"),

            Date(LocalDate.of(2026, 12, 3)),
            Content("Car service"),
            Content("Team retro"),

            Date(LocalDate.of(2026, 12, 4)),
            Content("Interview candidate — Android role"),

            Date(LocalDate.of(2026, 12, 5)),
            Content("Year-end planning workshop"),
            Content("Renew car insurance"),
            Content("Send holiday cards"),

            Date(LocalDate.of(2026, 12, 11)),
            Content("Office end-of-year party"),

            Date(LocalDate.of(2026, 12, 18)),
            Content("Last working day"),
            Content("Set out-of-office reply"),

            Date(LocalDate.of(2026, 12, 23)),
            Content("Christmas shopping"),
            Content("Wrap gifts"),

            Date(LocalDate.of(2026, 12, 24)),
            Content("Christmas Eve service"),

            Date(LocalDate.of(2026, 12, 25)),
            Content("Christmas lunch with family 🎄"),
            Content("Call grandma"),

            Date(LocalDate.of(2026, 12, 26)),
            Content("Boxing Day — rest"),

            Date(LocalDate.of(2026, 12, 31)),
            Content("New Year's Eve party"),
            Content("Write 2027 goals"),
        )
    }
}

@Composable
internal fun CalendarScreen(
    modifier: Modifier = Modifier,
) {
    CalendarScreen(
        items = CalendarUiItem.MockData
    )
}

@Composable
internal fun CalendarScreen(
    items: List<CalendarUiItem>,
    modifier: Modifier = Modifier,
) {

    val coroutineScope = rememberCoroutineScope()

    val dateStripListState = rememberDateStripListState()

    val listState = rememberLazyListState(30)

    val sectionDates = remember(items) {
        var current: LocalDate? = null
        items.map { item ->
            if (item is CalendarUiItem.Date) item.date.also { current = it } else current
        }
    }

    var tappedDate by remember { mutableStateOf<LocalDate?>(null) }

    val selectedDate by remember(sectionDates) {
        derivedStateOf {
            tappedDate ?: sectionDates.getOrNull(listState.firstVisibleItemIndex) ?: Today
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

//                val dateIndex =
//                    items.indexOfFirst { item -> item is CalendarUiItem.Date && item.date == date }
//                if (dateIndex >= 0) {
//                    coroutineScope.launch { listState.animateScrollToItem(dateIndex) }
//                }

                // Will this work with paging?
                val dateIndex = items
                    .indexOfFirst { item -> item is CalendarUiItem.Date && !item.date.isBefore(date) }

                if (dateIndex >= 0) {
                    coroutineScope.launch { listState.animateScrollToItem(dateIndex) }
                }
            },
            listState = dateStripListState,
            modifier = Modifier
                .zIndex(1f)
                .rotate(-1.5f),
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            modifier = Modifier.bleedVertically(36.dp)
        ) {
            items(
                items = items,
//                key = TODO
                contentType = {
                    when (it) {
                        is CalendarUiItem.Content -> "content"
                        is CalendarUiItem.Date -> "date"
                    }
                }
            ) {
                when (it) {
                    is CalendarUiItem.Content -> {
                        Row(
                            modifier = Modifier
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
                                imageUrl = null,
                                modifier = Modifier.aspectRatio(16f / 9f),
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(
                                    text = "The Long Static",
                                    style = SnacTypography.subtitle,
                                )
                                Text(
                                    text = "S03E05 · Carrier Wave",
                                    style = SnacTypography.caption,
                                )
                                Text(
                                    text = "Coastline TV · 48 min",
                                    style = SnacTypography.caption,
                                    color = SnacColors.muted,
                                )
                            }
                        }
                    }

                    is CalendarUiItem.Date -> {

                        Text(
                            text = it.date.formatted().uppercase(),
                            style = SnacTypography.title,
                            modifier = Modifier.silhouette(SnacColors.accent),
                        )
                    }
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
    SnacTheme {
        CalendarScreen(
            items = CalendarUiItem.MockData
        )
    }
}