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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val animationSpec = spring<Float>()

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
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> progress.animateTo(1f, animationSpec)
                    is PressInteraction.Release,
                    is PressInteraction.Cancel -> progress.animateTo(0f, animationSpec)
                }
            }
        }
    }

}

data class SilhouettePressIndication(
    private val color: ColorProducer = { Color.Black },
    private val offset: DpOffset = DefaultOffset,
    private val overflow: Dp = DefaultOverflow,
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

@Composable
fun Clickable(modifier: Modifier = Modifier) {
    Box(
        modifier
//            .fastSilhouette(Color.Black, overflow = 10.dp)
    ) {
        val color = Color.Blue
        Box(
            Modifier
                .size(100.dp)
//                .rotate(-10f)

                .clickable(
                    indication = remember(color) { SilhouettePressIndication({ color }) },
                    interactionSource = null
                ) {}
//                .silhouette(Color.Black, overflow = 20.dp)
                .background(Color.Magenta)
        )
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
