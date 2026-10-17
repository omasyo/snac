package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun Stamp(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SnacColors.chip,
) {
    val contentColor = SnacColors.signal
    Text(
        text = text,
        style = SnacTypography.hand,
        color = contentColor,
        modifier = modifier
            .border(4.dp, contentColor)
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 2.dp)
    )
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Stamp("#1 trending")
    }
}