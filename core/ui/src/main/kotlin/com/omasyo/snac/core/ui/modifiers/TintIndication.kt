package com.omasyo.snac.core.ui.modifiers

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.launch

data class TintIndication(
    private val contentOnly: Boolean = false,
    private val focusOpacity: Float = 0.1f,
    private val pressOpacity: Float = 0.3f,
    private val color: ColorProducer = { Color.Unspecified },
) : IndicationNodeFactory {

    override fun create(interactionSource: InteractionSource): DelegatableNode =
        TintIndicationInstance(
            interactionSource = interactionSource,
            contentOnly = contentOnly,
            focusOpacity = focusOpacity,
            pressOpacity = pressOpacity,
            color = color
        )

    private class TintIndicationInstance(
        private val interactionSource: InteractionSource,
        private val contentOnly: Boolean,
        private val focusOpacity: Float,
        private val pressOpacity: Float,
        private val color: ColorProducer,
    ) : Modifier.Node(), DrawModifierNode {
        private var alpha = 0f

        override fun onAttach() {
            coroutineScope.launch {
                var pressCount = 0
                var hoverCount = 0
                var focusCount = 0
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> pressCount++
                        is PressInteraction.Release -> pressCount--
                        is PressInteraction.Cancel -> pressCount--
                        is HoverInteraction.Enter -> hoverCount++
                        is HoverInteraction.Exit -> hoverCount--
                        is FocusInteraction.Focus -> focusCount++
                        is FocusInteraction.Unfocus -> focusCount--
                    }
                    val newAlpha = when {
                        pressCount > 0 -> pressOpacity
                        hoverCount > 0 || focusCount > 0 -> focusOpacity
                        else -> 0f
                    }
                    if (newAlpha != alpha) {
                        alpha = newAlpha
                        invalidateDraw()
                    }
                }
            }
        }

        override fun ContentDrawScope.draw() {
            val color = color()
            if (alpha == 0f || !color.isSpecified) {
                drawContent()
                return
            }

            val canvas = drawContext.canvas
            if (contentOnly) canvas.saveLayer(size.toRect(), Paint())
            drawContent()
            drawRect(
                color = color.copy(alpha = alpha),
                blendMode = if (contentOnly) BlendMode.SrcAtop else BlendMode.SrcOver,
            )
            if (contentOnly) canvas.restore()
        }
    }
}

@Preview
@Composable
private fun Preview() {
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
                        indication = TintIndication(true) { Color.Blue },
                        interactionSource = remember { MutableInteractionSource() }
                    ) {}
//                    .background(Color.Magenta),
            ) {
                Text(
                    "Hello",
                    style = SnacTypography.title,
                    modifier = Modifier
                )
            }
        }
    }
}