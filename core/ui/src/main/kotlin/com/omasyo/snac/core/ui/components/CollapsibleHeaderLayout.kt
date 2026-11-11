package com.omasyo.snac.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollapsibleHeaderLayout(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    content: @Composable (isExpanded: Boolean) -> Unit,
) {

    var headerHeightPx by remember { mutableFloatStateOf(0f) }

    val maxPadding = headerHeightPx

    var topPadding by remember(maxPadding) { mutableFloatStateOf(maxPadding) }

    val isBodyScrollableAtTop = topPadding < headerHeightPx

    val nestedScrollConnection = remember(isBodyScrollableAtTop, headerHeightPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

                val prevPadding = topPadding

                val consumed = when {
                    delta < 0 -> {
                        val newPadding = prevPadding + delta
                        max(0f, newPadding) - prevPadding
                    }

                    else -> 0f
                }
                topPadding += consumed
                return Offset(0f, consumed)
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y

                val consumedScroll = when {
                    delta > 0 && isBodyScrollableAtTop -> {
                        val prevPadding = topPadding
                        val newPadding = topPadding + delta
                        min(maxPadding, newPadding) - prevPadding
                    }

                    else -> 0f
                }
                topPadding += consumedScroll
                return Offset(0f, consumedScroll)
            }

//            override suspend fun onPreFling(available: Velocity): Velocity { //TODO experiment without this
//                // Fling upward while header is still partially visible → snap it closed
//                return if (available.y < 0 && topPadding > 0f) {
//                    val animatable = Animatable(topPadding)
//                    animatable.animateTo(
//                        targetValue = 0f,
//                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
//                    ) { topPadding = value }
//                    // Consume the vertical component; hand x back untouched
//                    Velocity(0f, available.y)
//                } else {
//                    Velocity.Zero
//                }
//            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                // Fling downward while body is scrolled to its top → snap header open
                return if (available.y > 0 && isBodyScrollableAtTop) {
                    val animatable = Animatable(topPadding)
                    animatable.animateTo(
                        targetValue = maxPadding,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                    ) { topPadding = value }
                    Velocity(0f, available.y)
                } else {
                    Velocity.Zero
                }
            }
        }
    }
    SubcomposeLayout(
        modifier = modifier.nestedScroll(nestedScrollConnection)
    ) { constraints ->

        val headerPlaceable = subcompose("header") {
            header()
        }.first().measure(constraints.copy(minHeight = 0))

        headerHeightPx = headerPlaceable.height.toFloat()

        val bodyPlaceable = subcompose("body") {
            content(topPadding.compareTo(0f) == 0)
        }.first().measure(constraints)

        layout(bodyPlaceable.width, bodyPlaceable.height) {
            headerPlaceable.placeRelative(0, (topPadding - maxPadding).roundToInt())

            bodyPlaceable.placeRelative(0, topPadding.roundToInt())
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        CollapsibleHeaderLayout(
            header = {
                Column(
                    Modifier
                        .height(300.dp)
                ) {
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
            }
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
