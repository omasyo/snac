package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = SnacColors.accent,
    trackColor: Color = SnacColors.placeholder,
) {
    Box(modifier.background(trackColor)) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .background(color)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(
            Modifier
                .background(SnacColors.page)
                .padding(16.dp)
        ) {
            ProgressBar(
                progress = 0.42f,
                modifier = Modifier
                    .width(160.dp)
                    .height(6.dp),
            )
        }
    }
}
