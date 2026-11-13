package com.omasyo.snac.core.ui.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme

enum class Orientation { Vertical, Horizontal }

enum class Side { Both, Start, End }

fun Modifier.film(
    orientation: Orientation = Orientation.Vertical,
    side: Side = Side.Both,
    holeLength: Dp = 12.dp,
    holeDepth: Dp = 12.dp,
    holeGap: Dp = 12.dp,
    crossEdgeInset: Dp = 4.dp,
    edgeInset: Dp = 0.dp,
): Modifier = drawWithCache {
    val vertical = orientation == Orientation.Vertical
    val mainSize = if (vertical) size.height else size.width
    val crossSize = if (vertical) size.width else size.height

    val length = holeLength.toPx()
    val depth = holeDepth.toPx()
    val gap = holeGap.toPx()
    val crossInset = crossEdgeInset.toPx()
    val inset = edgeInset.toPx()
    val pitch = length + gap

    val count = ((mainSize + gap - inset) / pitch).toInt()
    val start = (mainSize - (count * pitch - gap)) / 2f

    val crossPositions = buildList {
        if (side != Side.End) add(crossInset)
        if (side != Side.Start) add(crossSize - crossInset - depth)
    }

    val holes = Path().apply {
        repeat(count.coerceAtLeast(0)) { i ->
            val main = start + i * pitch
            crossPositions.forEach { cross ->
                val rect = if (vertical) {
                    Rect(Offset(cross, main), Size(depth, length))
                } else {
                    Rect(Offset(main, cross), Size(length, depth))
                }
                addRect(rect)
            }
        }
    }

    onDrawWithContent {
        clipPath(holes, clipOp = ClipOp.Difference) {
            this@onDrawWithContent.drawContent()
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
