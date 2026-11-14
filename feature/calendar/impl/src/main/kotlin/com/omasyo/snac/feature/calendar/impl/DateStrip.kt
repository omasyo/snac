package com.omasyo.snac.feature.calendar.impl

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.annotation.FrequentlyChangingValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.Orientation
import com.omasyo.snac.core.ui.modifiers.bleedHorizontally
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.LocalContentColor
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import java.time.LocalDate
import java.time.temporal.ChronoUnit

const val START = Int.MAX_VALUE / 2

val Today: LocalDate = LocalDate.now()

@Composable
fun rememberDateStripListState(): DateStripListState {
    val listState = rememberLazyListState(START)
    return remember { DateStripListState(listState) }
}

class DateStripListState(internal val listState: LazyListState) : ScrollableState by listState {
    val layoutInfo: LazyListLayoutInfo
        @FrequentlyChangingValue get() = listState.layoutInfo

    suspend fun animateScrollToDate(date: LocalDate) {
        val dayDiff = ChronoUnit.DAYS.between(Today, date).toInt()
        listState.animateScrollToItem(START + dayDiff)
    }
}

@Composable
fun DateStrip(
    selectedDate: LocalDate,
    onDateTap: (LocalDate) -> Unit,
    listState: DateStripListState,
    modifier: Modifier = Modifier,
) {

    val itemsInfo by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo } }

    LaunchedEffect(selectedDate) {

        val indices = itemsInfo.map { it.index }


        val dayDiff = ChronoUnit.DAYS.between(Today, selectedDate).toInt()

        val index = START + dayDiff

        if (index !in indices) {
            listState.animateScrollToDate(selectedDate)
        }
    }

    LazyRow(
        state = listState.listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
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
        items(Int.MAX_VALUE) {
            val date = Today.plusDays(it.toLong() - START)
            DateCard(
                date = date,
                selected = selectedDate == date,
                onClick = { onDateTap(date) }
            )
        }
    }
}

@Composable
private fun DateCard(
    date: LocalDate,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(if (selected) SnacColors.accent else SnacColors.filmLine)
    val contentColor by animateColorAsState(if (selected) SnacColors.ink else SnacColors.chip)
    val rotation by animateFloatAsState(if (selected) -6f else 0f)

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .rotate(rotation)
                .clickable(
                    onClick = onClick,
                )
                .background(backgroundColor)
                .padding(12.dp),
        ) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = date.dayOfWeek.name.take(3),
                style = SnacTypography.overline,
            )
            Text(
                text = date.dayOfMonth.toString(),
                style = SnacTypography.title.copy(
                    lineHeightStyle = CapHeightLineHeightStyle,
                ),
            )

            Text(
                text = date.takeIf { it.monthValue != Today.monthValue }?.month?.name?.take(3)
                    ?: "",
                style = SnacTypography.overline,
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val lazyListState = rememberDateStripListState()
    var selectedDate by remember { mutableStateOf(Today) }
    val targetDate = LocalDate.now().plusDays(30)
    SnacTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .background(SnacColors.page)
                .padding(vertical = 16.dp),
        ) {
            DateStrip(
                selectedDate = selectedDate,
                onDateTap = { selectedDate = it },
                listState = lazyListState,
            )
            Button(
                onClick = { selectedDate = targetDate },
                modifier = Modifier.size(40.dp)
            ) {

            }
        }
    }
}