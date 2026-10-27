package com.omasyo.snac.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.launch

@Composable
fun ErrorBox(
    description: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    error: String = "Didn't load",
    alignment: Alignment = Alignment.Center,
    errorPadding: PaddingValues = PaddingValues.Zero,
    content: @Composable () -> Unit,
) {
    val stampScale = remember { Animatable(1.8f) }
    val stampRotation = remember { Animatable(-20f) }
    val stampAlpha = remember { Animatable(0f) }
    val detailsAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { stampAlpha.animateTo(1f, tween(200)) }
        launch { stampRotation.animateTo(-7f, tween(250, easing = FastOutLinearInEasing)) }

        stampScale.animateTo(1f, tween(220, easing = FastOutLinearInEasing))

        launch { detailsAlpha.animateTo(1f, tween(200)) }
        stampScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.35f, stiffness = Spring.StiffnessMedium),
            initialVelocity = -3f,
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = alignment,
    ) {
        Box(
            Modifier.graphicsLayer {
                val squash = 1f - 0.25f * (1f - stampScale.value).coerceAtLeast(0f)
                scaleX = squash
                scaleY = squash
            }
        ) {
            content()
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(errorPadding),
        ) {
            val color = SnacColors.signal
            Text(
                text = error,
                style = SnacTypography.hand,
                color = color,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = stampAlpha.value
                        scaleX = stampScale.value
                        scaleY = stampScale.value
                        rotationZ = stampRotation.value
                    }
                    .border(2.dp, color)
                    .padding(4.dp)
                    .border(4.dp, color)
                    .background(SnacColors.chip)
                    .padding(horizontal = 20.dp, vertical = 4.dp),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer { alpha = detailsAlpha.value },
            ) {
                Text(
                    text = description,
                    style = SnacTypography.caption,
                    color = SnacColors.onFilm,
                    modifier = Modifier
                        .background(SnacColors.film)
                        .padding(4.dp),
                )
                Button("Retry", onRetry, contentPadding = PaddingValues(16.dp, 4.dp))
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    var toggle by remember { mutableStateOf(false) }
    SnacTheme {
        Column {
            if (toggle) {
                ErrorBox(
                    description = "Couldn’t get trending films from Trakt.",
                    onRetry = {},
                ) {
                    Box(
                        modifier = Modifier
                            .background(SnacColors.page)
                            .padding(12.dp)
                            .background(SnacColors.film)
                            .padding(12.dp)
                            .background(SnacColors.page)
                            .aspectRatio(1f)
                    )
                }
            } else {
                ErrorBox(
                    description = "Couldn’t get trending films from Trakt.",
                    onRetry = {},
                ) {
                    Box(
                        modifier = Modifier
                            .background(SnacColors.page)
                            .padding(12.dp)
                            .background(SnacColors.film)
                            .padding(12.dp)
                            .background(SnacColors.page)
                            .aspectRatio(1f)
                    )
                }
            }
            Button(text = "Test", onClick = { toggle = !toggle })
        }
    }
}