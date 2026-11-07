package com.omasyo.snac.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.random.Random

@Composable
fun SelectableText(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val mutableInteractionSource = remember { MutableInteractionSource() }
    val indicationColor = SnacColors.onFilm
    val selectionColor = SnacColors.signal

    val selectProgress = remember { Animatable(if (isSelected) 1f else 0f) }
    val unSelectProgress = remember { Animatable(if (isSelected) 0f else 1f) }

    val randomOffsets =
        rememberSaveable {
            List(10) { Random.nextDouble(-10.0, 7.0).toFloat() }
        }

    LaunchedEffect(isSelected) {
        if (isSelected) {
            if (unSelectProgress.isRunning) {
                selectProgress.snapTo(0f)
            }
            unSelectProgress.stop()
            unSelectProgress.snapTo(0f)
            selectProgress.animateTo(1f)
        } else {
            unSelectProgress.snapTo(0.5f)
            unSelectProgress.animateTo(1f)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = mutableInteractionSource,
                indication = remember {
                    TintIndication(
                        contentOnly = true,
                        focusOpacity = 0.2f,
                        pressOpacity = 0.5f
                    ) { indicationColor }
                },
                onClick = onClick,
            )
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val pad = strokeWidth / 2f

                    //Center
                    val cx = size.width / 2f
                    val cy = size.height / 2f

                    // Bezier handle offsets
                    val kx = (size.width - strokeWidth) * 0.25f
                    val ky = (size.height - strokeWidth) * 0.25f

                    val topX = cx + randomOffsets[0]
                    val topY = pad + randomOffsets[1]
                    val leftX = pad + randomOffsets[2]
                    val leftY = cy + randomOffsets[3]
                    val bottomX = cx + randomOffsets[4]
                    val bottomY = size.height - pad + randomOffsets[5]
                    val rightX = size.width - pad + randomOffsets[6]
                    val rightY = cy + randomOffsets[7]

                    val endX = topX + randomOffsets[8]
                    val endY = topY + randomOffsets[9]

                    val path = Path().apply {
                        moveTo(topX, topY)
                        cubicTo(topX - kx, topY, leftX, leftY - ky, leftX, leftY)
                        cubicTo(leftX, leftY + ky, bottomX - kx, bottomY, bottomX, bottomY)
                        cubicTo(bottomX + kx, bottomY, rightX, rightY + ky, rightX, rightY)
                        cubicTo(rightX, rightY - ky, endX + kx, endY, endX, endY)
                    }

                    val pathMeasure = PathMeasure().apply {
                        setPath(path, false)
                    }
                    val extractedPath = Path()

                    pathMeasure.getSegment(
                        startDistance = 0f,
                        stopDistance = pathMeasure.length * selectProgress.value,
                        destination = extractedPath,
                        startWithMoveTo = true
                    )

                    drawPath(
                        path = extractedPath,
                        color = selectionColor.copy(1 - unSelectProgress.value),
                        style = Stroke(width = strokeWidth),
                    )
                }
        ) {
            Text(
                text = label,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {

    var selected by remember { mutableStateOf(true) }
    SnacTheme {
        SelectableText(
            label = "Home",
            isSelected = selected,
            onClick = { selected = !selected }
        )
    }
}