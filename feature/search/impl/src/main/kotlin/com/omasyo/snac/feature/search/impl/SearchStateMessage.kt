package com.omasyo.snac.feature.search.impl

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.launch

@Composable
internal fun SearchStateMessage(
    stamp: String,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FrameRotations.forEach { rotation ->
                    EmptyFrame(
                        modifier = Modifier
                            .weight(1f)
                            .rotate(rotation),
                    )
                }
            }
            Stamp(text = stamp.uppercase())
        }
        Text(
            text = title.uppercase(),
            style = SnacTypography.headline,
            color = SnacColors.ink,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = body,
            style = SnacTypography.hand,
            color = SnacColors.ink,
        )
    }
}

private val FrameRotations = listOf(-3f, 1.5f, -1f)

@Composable
private fun EmptyFrame(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(0.72f)
            .silhouette()
            .border(2.dp, SnacColors.outline)
            .background(SnacColors.card)
            .padding(8.dp)
            .fillMaxSize()
            .background(SnacColors.placeholder),
    )
}

@Composable
private fun Stamp(text: String, modifier: Modifier = Modifier) {
    val scale = remember(text) { Animatable(1.8f) }
    val alpha = remember(text) { Animatable(0f) }

    LaunchedEffect(text) {
        launch { alpha.animateTo(1f, tween(200)) }
        scale.animateTo(1f, tween(220, easing = FastOutLinearInEasing))
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.35f, stiffness = Spring.StiffnessMedium),
            initialVelocity = -3f,
        )
    }

    val color = SnacColors.signal
    Text(
        text = text,
        style = SnacTypography.headline,
        color = color,
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha.value
                scaleX = scale.value
                scaleY = scale.value
            }
            .border(2.dp, color)
            .padding(4.dp)
            .border(4.dp, color)
            .background(SnacColors.chip)
            .padding(horizontal = 20.dp, vertical = 4.dp),
    )
}

@Preview
@Composable
private fun NoResultsPreview() {
    SnacTheme {
        SearchStateMessage(
            stamp = "No match",
            title = "Nothing found",
            body = "No films, series or people match “zzxyq”.",
            modifier = Modifier
                .background(SnacColors.page)
                .padding(vertical = 24.dp),
        )
    }
}

@Preview
@Composable
private fun PromptPreview() {
    SnacTheme {
        SearchStateMessage(
            stamp = "Take one",
            title = "Find your next watch",
            body = "Search for films, series and people.",
            modifier = Modifier
                .background(SnacColors.page)
                .padding(vertical = 24.dp),
        )
    }
}
