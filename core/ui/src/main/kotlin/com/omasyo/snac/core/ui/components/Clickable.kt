package com.omasyo.snac.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.requireGraphicsContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.ceil

private class ScaleNode(private val interactionSource: InteractionSource) :
    Modifier.Node(), DrawModifierNode {
    private var overflow: Dp = 0.dp

    val offset: DpOffset = DpOffset(4.dp, 4.dp)


    private var colorFilter = ColorFilter.tint(Color.Black, BlendMode.SrcIn)

    private var contentLayer: GraphicsLayer? = null
    private var tintedLayer: GraphicsLayer? = null
    val progress = Animatable(0f)

    private suspend fun animateToPressed() {
        progress.animateTo(1f, spring())
    }

    private suspend fun animateToResting() {
        progress.animateTo(0f, spring())
    }

    override fun onAttach() {
        val graphicsContext = requireGraphicsContext()
        contentLayer = graphicsContext.createGraphicsLayer()
        tintedLayer = graphicsContext.createGraphicsLayer().also {
            it.colorFilter = colorFilter
        }

        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animateToPressed()
                    is PressInteraction.Release -> animateToResting()
                    is PressInteraction.Cancel -> animateToResting()
                }
            }
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

        translate(offset.x.toPx() * progress.value, offset.y.toPx()* progress.value) {
            drawLayer(content)
        }
    }
}

object ScaleIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleNode(interactionSource)
    }

    override fun equals(other: Any?): Boolean = other === ScaleIndication
    override fun hashCode() = 100
}

@Composable
fun Clickable(modifier: Modifier = Modifier) {
    Box(
        modifier
//            .fastSilhouette(Color.Black, overflow = 10.dp)
    ) {
        Box(
            Modifier
                .size(100.dp)
                .rotate(-10f)

                .clickable(
                    indication = ScaleIndication,
                    interactionSource = null
                ) {}
//                .silhouette(Color.Black, overflow = 20.dp)
                .background(Color.Magenta)
        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    SnacTheme {

        Clickable(
            modifier = Modifier.padding(20.dp)
        )
    }
}
