package com.omasyo.snac.core.ui.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme

enum class Orientation {
    Vertical,

    Horizontal,
}

fun Modifier.film(
    orientation: Orientation = Orientation.Vertical,
    holeLength: Dp = 12.dp,
    holeDepth: Dp = 12.dp,
    holeGap: Dp = 12.dp,
    edgeInset: Dp = 4.dp,
): Modifier = graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()

        val vertical = orientation == Orientation.Vertical
        val mainSize = if (vertical) size.height else size.width
        val crossSize = if (vertical) size.width else size.height

        val length = holeLength.toPx()
        val depth = holeDepth.toPx()
        val gap = holeGap.toPx()
        val inset = edgeInset.toPx()
        val pitch = length + gap

        val count = ((mainSize + gap) / pitch).toInt()
        if (count <= 0) return@drawWithContent


        val start = (mainSize - (count * pitch - gap)) / 2f

        val holeSize = if (vertical) Size(depth, length) else Size(length, depth)

        fun topLeft(main: Float, cross: Float) =
            if (vertical) Offset(cross, main) else Offset(main, cross)

        repeat(count) { i ->
            val main = start + i * pitch
            listOf(inset, crossSize - inset - depth).forEach { cross ->
                drawRect(
                    color = Color.Black, // ignored by BlendMode.Clear
                    topLeft = topLeft(main, cross),
                    size = holeSize,
                    blendMode = BlendMode.Clear,
                )
            }
        }
    }


@Preview(showBackground = true)
@Composable
private fun Preview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .film(),
//            orientation = Orientation.Horizontal,
        ) {

            Box(
                Modifier
                    .size(360.dp)
                    .background(Color.Blue)
            )
        }
    }
}
