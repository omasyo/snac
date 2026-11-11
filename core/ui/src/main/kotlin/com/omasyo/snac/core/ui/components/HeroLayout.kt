package com.omasyo.snac.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.theme.SnacTheme
import kotlin.math.roundToInt

@Composable
fun rememberHeroLayoutState(): CollapsibleHeaderState =
    rememberSaveable(saver = CollapsibleHeaderState.Saver) { CollapsibleHeaderState() }

@Composable
fun HeroLayout(
    modifier: Modifier = Modifier,
    state: CollapsibleHeaderState = rememberHeroLayoutState(),
    hero: @Composable () -> Unit,
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    SubcomposeLayout(
        modifier = modifier
            .clipToBounds()
            .nestedScroll(state.nestedScrollConnection),
    ) { constraints ->
        val looseConstraints = constraints.copy(minHeight = 0)

        val heroPlaceables = subcompose("hero", hero).map { it.measure(looseConstraints) }
        val heroHeight = heroPlaceables.maxOfOrNull { it.height } ?: 0

        val topBarPlaceables = subcompose("topBar", topBar).map { it.measure(looseConstraints) }
        val topBarHeight = topBarPlaceables.maxOfOrNull { it.height } ?: 0

        state.onMeasured(heroHeight = heroHeight.toFloat(), topBarHeight = topBarHeight.toFloat())

        val topPadding = topBarHeight.toDp()
        val bodyPlaceables = subcompose("body") {
            val bottomBarPadding = LocalBottomBarPadding.current.calculateBottomPadding()
            val navigationBarPadding =
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            content(
                PaddingValues(
                    top = topPadding,
                    bottom = bottomBarPadding + navigationBarPadding,
                )
            )
        }.map { it.measure(constraints) }

        val width = bodyPlaceables.maxOfOrNull { it.width } ?: constraints.minWidth
        val height = constraints.maxHeight

        layout(width, height) {
            val heroY = (-state.collapseOffset).roundToInt()
            val heroBottom = heroY + heroHeight

            val topBarY = (topBarHeight - heroBottom).coerceIn(-topBarHeight, 0)

            bodyPlaceables.forEach { it.placeRelative(0, heroBottom - topBarHeight) }
            heroPlaceables.forEach { it.placeRelative(0, heroY) }
            topBarPlaceables.forEach { it.placeRelative(0, topBarY) }
        }
    }
}

@Stable
class CollapsibleHeaderState(initialCollapseOffset: Float = 0f) {

    private var heroHeight = 0f

    var topBarHeight = 0f
        private set

    internal var collapseOffset by mutableFloatStateOf(initialCollapseOffset)
        private set

    private val maxCollapseOffset: Float
        get() = (heroHeight - topBarHeight).coerceAtLeast(0f)

    private val isHeroPartlyHidden: Boolean
        get() = collapseOffset > 0f

    val topBarProgress: Float
        get() {
            if (topBarHeight <= 0f) return 0f
            val shownPx = collapseOffset - (maxCollapseOffset - topBarHeight)
            return (shownPx / topBarHeight).coerceIn(0f, 1f)
        }

    internal fun onMeasured(heroHeight: Float, topBarHeight: Float) {
        if (heroHeight != this.heroHeight || topBarHeight != this.topBarHeight) {
            this.heroHeight = heroHeight
            this.topBarHeight = topBarHeight
        }
    }

    private fun scrollHero(delta: Float): Float {
        val newOffset = (collapseOffset - delta).coerceIn(0f, maxCollapseOffset)
        val consumed = collapseOffset - newOffset
        collapseOffset = newOffset
        return consumed
    }

    companion object {
        val Saver: Saver<CollapsibleHeaderState, Float> = Saver(
            save = { it.collapseOffset },
            restore = { CollapsibleHeaderState(it) }
        )
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
        HeroLayout(
            hero = {
                Column(Modifier.height(300.dp)) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.Blue)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.Green)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color.Yellow)
                    )
                }
            },
            topBar = {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(Color.DarkGray)
                    )
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(Color.Magenta)
                    )
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(Color.Yellow)
                    )
                }
            },
        ) { contentPadding ->
            LazyColumn(
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(20) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}
