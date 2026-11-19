package com.omasyo.snac.feature.calendar.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
    CalendarCardLayout(
        modifier = modifier,
        image = {
            Box(
                Modifier
                    .matchParentSize()
                    .background(imageGradient())
            )
        },
        details = {
            Bar(
                Modifier
                    .padding(vertical = 2.dp)
                    .size(140.dp, 18.dp)
            )
            Bar(
                Modifier
                    .padding(vertical = 2.dp)
                    .size(100.dp, 12.dp)
            )
            Bar(
                Modifier
                    .padding(vertical = 2.dp)
                    .size(56.dp, 12.dp)
            )
        },
    )
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
