package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun ClapperboardStripes(
    modifier: Modifier = Modifier,
    stripeThickness: Dp = 16.dp,
    stripeGap: Dp = 16.dp,
    stripeShift: Dp = 12.dp,
    stripeColor: Color = SnacColors.outline,
    backgroundColor: Color = SnacColors.chip,
) {
    Canvas(modifier) {
        val thickness = stripeThickness.toPx()
        val gap = stripeGap.toPx()
        val shift = stripeShift.toPx()
        val run = thickness + gap

        clipRect {
            drawRect(backgroundColor)

            val centerX = size.width / 2f
            val baseOffset = centerX - (thickness + shift) / 2f

            val count = ((size.width + thickness + shift) / run).toInt()
            val n = count / 2

            for (i in -n..n) {
                val offset = baseOffset + i * run
                val path = Path().apply {
                    moveTo(offset, size.height)
                    lineTo(offset + thickness, size.height)
                    lineTo(offset + thickness + shift, 0f)
                    lineTo(offset + shift, 0f)
                    close()
                }
                drawPath(path, stripeColor)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Column(Modifier.width(IntrinsicSize.Min).background(SnacColors.chip)) {
            ClapperboardStripes(
               modifier =  Modifier
                    .height(20.dp)
                    .fillMaxWidth()
            )
            Box(
                Modifier
                    .height(120.dp)
                    .width(200.dp)
                    .background(SnacColors.chip)
            )
        }
    }
}