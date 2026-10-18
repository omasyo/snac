package com.omasyo.snac.feature.calendar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.theme.SnacTypography

@Composable
fun CalendarScreen() {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text("Calendar", style = SnacTypography.title)
    }
}