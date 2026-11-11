package com.omasyo.snac.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme
import kotlin.math.roundToInt

@Composable
fun CollapsibleHeaderLayout(
    modifier: Modifier = Modifier,
    hero: @Composable () -> Unit,
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val state = remember { CollapsibleHeaderState() } // TODO make saveable

    Layout(
        contents = listOf(hero, topBar, content),
        modifier = modifier
            .clipToBounds()
            .nestedScroll(state.nestedScrollConnection),
    ) { (heroMeasurables, topBarMeasurables, bodyMeasurables), constraints ->
        val looseConstraints = constraints.copy(minHeight = 0)
        val heroPlaceables = heroMeasurables.map { it.measure(looseConstraints) }
        val topBarPlaceables = topBarMeasurables.map { it.measure(looseConstraints) }
        val bodyPlaceables = bodyMeasurables.map { it.measure(constraints) }

        val heroHeight = heroPlaceables.maxOfOrNull { it.height } ?: 0
        val topBarHeight = topBarPlaceables.maxOfOrNull { it.height } ?: 0
        val width = bodyPlaceables.maxOfOrNull { it.width } ?: constraints.minWidth
        val height = bodyPlaceables.maxOfOrNull { it.height } ?: constraints.minHeight

        state.onHeroMeasured(heroHeight.toFloat())

        layout(width, height) {
            val collapseOffset = state.collapseOffset

            val heroY = (-collapseOffset).roundToInt()
            val heroBottom = heroY + heroHeight

            val topBarY = (-heroBottom).coerceAtLeast(-topBarHeight)

            heroPlaceables.forEach { it.placeRelative(0, heroY) }
            bodyPlaceables.forEach { it.placeRelative(0, heroBottom) }
            topBarPlaceables.forEach { it.placeRelative(0, topBarY) }
        }
    }
}

@Stable
private class CollapsibleHeaderState {

    var heroHeight = 0f
        private set

    var collapseOffset by mutableFloatStateOf(0f)
        private set

    private val isHeroPartlyHidden: Boolean
        get() = collapseOffset > 0f

    fun onHeroMeasured(height: Float) {
        if (height != heroHeight) {
            heroHeight = height
            collapseOffset = 0f
        }
    }

    private fun scrollHero(delta: Float): Float {
        val newOffset = (collapseOffset - delta).coerceIn(0f, heroHeight)
        val consumed = collapseOffset - newOffset
        collapseOffset = newOffset
        return consumed
    }

    val nestedScrollConnection = object : NestedScrollConnection {

        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            return if (delta < 0f) Offset(0f, scrollHero(delta)) else Offset.Zero
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            val delta = available.y
            return if (delta > 0f) Offset(0f, scrollHero(delta)) else Offset.Zero
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            if (available.y <= 0f || !isHeroPartlyHidden) return Velocity.Zero

            Animatable(collapseOffset).animateTo(
                targetValue = 0f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
            ) { collapseOffset = value }

            return Velocity(0f, available.y)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        CollapsibleHeaderLayout(
            hero = {
                Column(Modifier.height(300.dp)) {
                    Box(Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Blue))
                    Box(Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Green))
                    Box(Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color.Yellow))
                }
            },
            topBar = {
                Row(Modifier
                    .fillMaxWidth()
                    .height(80.dp)) {
                    Box(Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(Color.DarkGray))
                    Box(Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(Color.Magenta))
                    Box(Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(Color.Yellow))
                }
            },
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(20) {
                    Box(Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color.Red))
                }
            }
        }
    }
}