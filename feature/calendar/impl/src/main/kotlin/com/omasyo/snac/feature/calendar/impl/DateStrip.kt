package com.omasyo.snac.feature.calendar.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.annotation.FrequentlyChangingValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.theme.SnacTheme
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
        println("JJJJJJ $indices $dayDiff")

        if (index !in indices) {
            listState.animateScrollToDate(selectedDate)
        }
    }

    LazyRow(
        modifier = modifier,
        state = listState.listState,
    ) {
        items(Int.MAX_VALUE) {

            val date = Today.plusDays(it.toLong() - START)
            val selected = selectedDate == date
            Box(
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = null,
                    ) {
                        onDateTap(date)
                    }
                    .width(100.dp)
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = date.toString(),
                    color = if (selected) SnacColors.signal else SnacColors.ink,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val lazyListState = rememberDateStripListState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val targetDate = LocalDate.now().plusDays(20)
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