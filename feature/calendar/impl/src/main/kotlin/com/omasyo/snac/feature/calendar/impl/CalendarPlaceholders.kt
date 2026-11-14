package com.omasyo.snac.feature.calendar.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors


@Composable
internal fun CalendarDayPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
    ) {
        DateHeaderPlaceholder()
        repeat(3) { CalendarCardPlaceholder() }
        DateHeaderPlaceholder()
        repeat(4) { CalendarCardPlaceholder() }
    }
}

@Composable
private fun DateHeaderPlaceholder(modifier: Modifier = Modifier) {
    Bar(
        modifier
            .padding(vertical = 4.dp)
            .size(120.dp, 28.dp)
    )
}

@Composable
internal fun CalendarCardPlaceholder(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .height(IntrinsicSize.Min)
            .border(2.dp, SnacColors.ink)
            .background(SnacColors.card)
            .padding(8.dp),
    ) {
        Box(
            Modifier
                .aspectRatio(16f / 9f)
                .background(imageGradient())
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp),
        ) {
            Bar(Modifier.size(140.dp, 18.dp))
            Bar(Modifier.size(100.dp, 12.dp))
            Bar(Modifier.size(56.dp, 12.dp))
        }
    }
}

@Composable
private fun Bar(modifier: Modifier) {
    Box(modifier.background(SnacColors.placeholder))
}

@Preview
@Composable
private fun CalendarDayPlaceholderPreview() {
    SnacTheme {
        CalendarDayPlaceholder(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun CalendarCardPlaceholderPreview() {
    SnacTheme {
        CalendarCardPlaceholder(
            Modifier
                .background(SnacColors.page)
                .padding(16.dp)
                .width(360.dp)
        )
    }
}
