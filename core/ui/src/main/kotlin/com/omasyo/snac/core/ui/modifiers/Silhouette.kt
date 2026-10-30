package com.omasyo.snac.core.ui.modifiers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
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
import com.omasyo.snac.core.ui.theme.colors.LocalColors
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.ceil


private object SilhouetteDefaults {
    val Offset = DpOffset(3.dp, 3.dp)
    val Overflow = 0.dp
}

val LocalSilhouetteColor =
    compositionLocalWithComputedDefaultOf { LocalColors.currentValue.shadow }

fun Modifier.silhouette(
    color: Color = Color.Unspecified,
    offset: DpOffset = SilhouetteDefaults.Offset,
    overflow: Dp = SilhouetteDefaults.Overflow,
): Modifier = this then SilhouetteElement(color, offset, overflow)

private data class SilhouetteElement(
    val color: Color,
    val offset: DpOffset,
    val overflow: Dp,
) : ModifierNodeElement<SilhouetteNode>() {

    override fun create() = SilhouetteNode({ color }, offset, overflow)

    override fun update(node: SilhouetteNode) {
        node.update({ color }, offset, overflow)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "silhouette"
        properties["color"] = color
        properties["offset"] = offset
        properties["overflow"] = overflow
    }
}

internal class SilhouetteNode(
    private var color: ColorProducer,
    private var offset: DpOffset,
    private var overflow: Dp,
    private val contentShift: () -> Float = { 0f },
) : Modifier.Node(), CompositionLocalConsumerModifierNode, DrawModifierNode {

    private var contentLayer: GraphicsLayer? = null
    private var tintedLayer: GraphicsLayer? = null

    fun update(color: ColorProducer, offset: DpOffset, overflow: Dp) {
        if (this.color() != color() || this.offset != offset || this.overflow != overflow) {
            this.color = color
            this.offset = offset
            this.overflow = overflow
            invalidateDraw()
        }
    }

    override fun onAttach() {
        val graphicsContext = requireGraphicsContext()
        contentLayer = graphicsContext.createGraphicsLayer()
        tintedLayer = graphicsContext.createGraphicsLayer()
    }

    override fun onDetach() {
        val graphicsContext = requireGraphicsContext()
        contentLayer?.let(graphicsContext::releaseGraphicsLayer)
        tintedLayer?.let(graphicsContext::releaseGraphicsLayer)
        contentLayer = null
        tintedLayer = null
    }

    override fun ContentDrawScope.draw() {
        val resolved = color().takeOrElse { currentValueOf(LocalSilhouetteColor) }

        val content = checkNotNull(contentLayer)
        val tinted = checkNotNull(tintedLayer).also {
            it.colorFilter = ColorFilter.tint(resolved, BlendMode.SrcIn)
        }


        // The 2f is padding for sampling, reducing aliasing
        val m = ceil(overflow.toPx()).coerceAtLeast(0f) + 2f
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

        val dx = offset.x.toPx()
        val dy = offset.y.toPx()
        translate(dx, dy) { drawLayer(tinted) }

        val shift = contentShift()
        translate(dx * shift, dy * shift) { drawLayer(content) }
    }
}

private val animationSpec = spring<Float>()


data class SilhouettePressIndication(
    private val color: ColorProducer = { Color.Unspecified },
    private val offset: DpOffset = SilhouetteDefaults.Offset,
    private val overflow: Dp = SilhouetteDefaults.Overflow,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return SilhouettePressNode(
            interactionSource,
            color = color,
            offset = offset,
            overflow = overflow
        )
    }
}

private class SilhouettePressNode(
    private val interactionSource: InteractionSource,
    color: ColorProducer,
    offset: DpOffset,
    overflow: Dp,
) : DelegatingNode() {
    private val progress = Animatable(0f)


    init {
        delegate(
            SilhouetteNode(
                color = color,
                offset = offset,
                overflow = overflow,
                contentShift = { progress.value },
            )
        )
    }

    override fun onAttach() {
        coroutineScope.launch {
            progress.snapTo(0f)

            val presses = mutableListOf<PressInteraction.Press>()
            var pressJob: Job? = null
            var releaseJob: Job? = null

            fun onPressEnded(press: PressInteraction.Press, waitForPress: Boolean) {
                presses -= press
                if (presses.isEmpty()) {
                    releaseJob = launch {
                        if (waitForPress) pressJob?.join()
                        progress.animateTo(0f, animationSpec)
                    }
                }
            }

            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        presses += interaction
                        releaseJob?.cancel()
                        pressJob = this@launch.launch { progress.animateTo(1f, animationSpec) }
                    }

                    is PressInteraction.Release -> onPressEnded(
                        interaction.press,
                        waitForPress = true
                    )

                    is PressInteraction.Cancel -> onPressEnded(
                        interaction.press,
                        waitForPress = false
                    )

                }
            }
        }
    }
}

@Preview
@Composable
private fun SilhouettePreview() {
    SnacTheme {
        Box(
            Modifier
                .background(SnacColors.page)
                .padding(20.dp)
        ) {
            BasicText(
                "Hello new world",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Red
                ),
                modifier = Modifier
                    .rotate(-90f)
                    .background(Color.Yellow)
                    .silhouette(Color.Black, overflow = 10.dp)
            )
             Box(
                Modifier
                    .size(100.dp)
                    .rotate(-10f)
                    .clickable(
                        indication = SilhouettePressIndication(),
                        interactionSource = null
                    ) {}
                    .background(Color.Magenta)
            ) {

            }
        }
    }
}

@Preview
@Composable
private fun ClickablePreview() {
    SnacTheme {
        Row(
            Modifier
                .background(SnacColors.page)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                Modifier
                    .size(200.dp)
                    .clickable(
                        indication = SilhouettePressIndication(),
                        interactionSource = remember { MutableInteractionSource() }
                    ) {}
                    .background(Color.Magenta)
            )
            Box(
                Modifier
                    .size(200.dp)
                    .clickable(
                        indication = SilhouettePressIndication(),
                        interactionSource = remember { MutableInteractionSource() }
                    ) {}
                    .background(Color.Magenta)
            )
        }
    }
}
