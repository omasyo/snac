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
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Icon
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.launch

@Composable
internal fun SearchPromptPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            EmptyFrame(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(104.dp, 144.dp)
                    .rotate(1.5f),
            )
            Icon(
                icon = com.omasyo.snac.core.ui.R.drawable.magnifying_glass_bold,
                contentDescription = null,
                tint = SnacColors.page,
                modifier = Modifier.size(36.dp),
            )
        }
        Text(
            text = "What are we looking for?".uppercase(),
            style = SnacTypography.title,
            color = SnacColors.ink,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = "Films, series, episodes",
            style = SnacTypography.hand,
            color = SnacColors.ink,
        )
    }
}

@Composable
internal fun NoResultsPlaceholder(
    query: String,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
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
                listOf(-3f, 1.5f, -1f).forEach { rotation ->
                    EmptyFrame(
                        modifier = Modifier
                            .size(104.dp, 144.dp)
                            .rotate(rotation),
                    )
                }
            }
            Stamp(text = stringResource(R.string.search_no_results_stamp).uppercase())
        }
        Text(
            text = stringResource(R.string.search_no_results_title).uppercase(),
            style = SnacTypography.title,
            color = SnacColors.ink,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = stringResource(R.string.search_no_results_body, query),
            style = SnacTypography.hand,
            color = SnacColors.ink,
        )
    }
}

@Composable
private fun EmptyFrame(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(9f / 13f)
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
        style = SnacTypography.title,
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
private fun SearchPromptPreview() {
    SnacTheme {
        SearchPromptPlaceholder(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(vertical = 24.dp),
        )
    }
}


@Preview
@Composable
private fun NoResultsPreview() {
    SnacTheme {
        NoResultsPlaceholder(
            query = "“zzxyq”",
            modifier = Modifier
                .background(SnacColors.page)
                .padding(vertical = 24.dp),
        )
    }
}

