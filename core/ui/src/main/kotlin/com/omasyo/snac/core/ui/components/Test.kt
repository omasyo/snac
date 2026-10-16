package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.node.requireGraphicsContext
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omasyo.snac.core.ui.theme.SnacTheme
import kotlin.math.ceil

/**
 * Draws a solid-color silhouette of the content, offset by [offset], behind the content.
 *
 * The tint needs an offscreen buffer, which would normally be clipped to the composable's
 * bounds. [overflow] pads that buffer on every side, so content that draws up to [overflow]
 * outside its bounds (shadows, glows, rotated children, etc.) is still captured.
 * Keep it as small as your content needs: the buffer grows with it.
 */
fun Modifier.silhouette(
    color: Color,
    offset: DpOffset = DpOffset(4.dp, 4.dp),
    overflow: Dp = 0.dp, //TODO needed?
): Modifier = this then SilhouetteElement(color, offset, overflow)

private data class SilhouetteElement(
    val color: Color,
    val offset: DpOffset,
    val overflow: Dp,
) : ModifierNodeElement<SilhouetteNode>() {

    override fun create() = SilhouetteNode(color, offset, overflow)

    override fun update(node: SilhouetteNode) {
        node.update(color, offset, overflow)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "silhouette"
        properties["color"] = color
        properties["offset"] = offset
        properties["overflow"] = overflow
    }
}

private class SilhouetteNode(
    private var color: Color,
    private var offset: DpOffset,
    private var overflow: Dp,
) : Modifier.Node(), DrawModifierNode {

    private var colorFilter = ColorFilter.tint(color, BlendMode.SrcIn)

    private var contentLayer: GraphicsLayer? = null
    private var tintedLayer: GraphicsLayer? = null

    fun update(color: Color, offset: DpOffset, overflow: Dp) {
        if (this.color != color) {
            this.color = color
            colorFilter = ColorFilter.tint(color, BlendMode.SrcIn)
            tintedLayer?.colorFilter = colorFilter
            invalidateDraw()
        }
        if (this.offset != offset || this.overflow != overflow) {
            this.offset = offset
            this.overflow = overflow
            invalidateDraw()
        }
    }

    override fun onAttach() {
        val graphicsContext = requireGraphicsContext()
        contentLayer = graphicsContext.createGraphicsLayer()
        tintedLayer = graphicsContext.createGraphicsLayer().also {
            it.colorFilter = colorFilter
        }
    }

    override fun onDetach() {
        val graphicsContext = requireGraphicsContext()
        contentLayer?.let(graphicsContext::releaseGraphicsLayer)
        tintedLayer?.let(graphicsContext::releaseGraphicsLayer)
        contentLayer = null
        tintedLayer = null
    }

    override fun ContentDrawScope.draw() {
        val content = checkNotNull(contentLayer)
        val tinted = checkNotNull(tintedLayer)

        val m = ceil(overflow.toPx()).coerceAtLeast(0f)
        val margin = m.toInt()

        val paddedSize = IntSize(
            ceil(size.width).toInt() + margin * 2,
            ceil(size.height).toInt() + margin * 2,
        )
        val origin = IntOffset(-margin, -margin)

        content.topLeft = origin
        content.record(paddedSize) {
            translate(m, m) { this@draw.drawContent() }
        }

        tinted.topLeft = origin
        tinted.record(paddedSize) {
            translate(m, m) { drawLayer(content) }
        }

        translate(offset.x.toPx(), offset.y.toPx()) {
            drawLayer(tinted)
        }
        drawLayer(content)
    }
}

@Composable
fun Test(modifier: Modifier = Modifier) {
    Box(
        modifier
//            .fastSilhouette(Color.Black, overflow = 10.dp)
    ) {
        BasicText(
            "Hello new world",
            style = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Black, color = Color.Red),
            modifier = Modifier
                .rotate(-90f)
                .background(Color.Yellow)
                .silhouette(Color.Black, overflow = 10.dp)
        )
        Box(
            Modifier
                .size(100.dp).rotate(-10f)
                .clickable(
                    indication = ScaleIndication,
                    interactionSource = null
                ) {}
                .background(Color.Magenta)
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SnacTheme {

        Test(
            modifier = Modifier.padding(20.dp)
        )
    }
}
