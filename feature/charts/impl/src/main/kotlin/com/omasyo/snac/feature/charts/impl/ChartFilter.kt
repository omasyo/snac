package com.omasyo.snac.feature.charts.impl

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.omasyo.snac.core.ui.components.BottomBarItem
import com.omasyo.snac.core.ui.components.ClapperboardStripes
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.random.Random

val MinWidth = 104.dp


enum class Chart(val label: String) {
    Trending("Trending"),
    Popular("Popular"),
    Anticipated("Anticipated"),
    Watched("Watched"),
    Played("Played"),
    Collected("Collected"),
    Favorited("Favorited"),
    BoxOffice("Box Office"),
}

@OptIn(ExperimentalGridApi::class)
@Composable
fun ChartFilter(
    selected: Chart,
    onSelectedChange: (Chart) -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    SharedTransitionLayout(modifier) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                (EnterTransition.None togetherWith ExitTransition.None) using null
            },
        ) {
            val grid: @Composable (@Composable () -> Unit) -> Unit = { content ->
                Grid(
                    config = {
                        repeat(3) {
                            column(GridTrackSize.MinMax(MinWidth, 1.fr))
                        }
                        gap(10.dp)
                    },
                    content = { content() }
                )
            }

            val stack: @Composable (@Composable () -> Unit) -> Unit = { content ->
                Box(
                    modifier = Modifier
                        .widthIn(MinWidth)
                        .width(IntrinsicSize.Max),
                    content = { content() }
                )
            }

            val layout = if (it) grid else stack

            layout {
                for (chart in Chart.entries) {
                    val isSelected = selected == chart
                    val pos = if (isSelected) Chart.entries.size else chart.ordinal
                    ChartFilterItem(
                        chart = chart,
                        selected = isSelected,
                        onClick = { onSelectedChange(chart) },
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(chart),
                                this@AnimatedContent,
                                zIndexInOverlay = pos.toFloat(),
                            )
                            .zIndex(pos.toFloat())
                            .fillMaxWidth(),
                    )
                }
            }
        }
    }
}


@Composable
fun ChartFilterItem(
    chart: Chart,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotation = Random(chart.hashCode()).nextInt(-6, 6).toFloat()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(IntrinsicSize.Max)
            .rotate(rotation)
            .clickable(
                indication = SilhouettePressIndication(
                    offset = if (selected) DpOffset(4.dp, 4.dp)
                    else DpOffset(2.dp, 2.dp)
                ),
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .border(4.dp, SnacColors.ink)
            .background(if (selected) SnacColors.accent else SnacColors.chip)
    ) {
        ClapperboardStripes(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        )
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(SnacColors.ink)
        )
        Text(
            text = chart.label,
            style = SnacTypography.tag,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun TestPreview() {
    var expanded by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf(Chart.Trending) }
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
        ) {
            ChartFilter(
                selected = selected,
                onSelectedChange = {
                    if (!expanded) {
                        expanded = true
                    } else {
                        selected = it
                        expanded = false
                    }
                },
                expanded = expanded,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun ChartFilterItemPreview() {
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            ChartFilterItem(
                chart = Chart.Trending,
                selected = true,
                onClick = {},
                modifier = Modifier
                    .padding(8.dp),
//                    .width(120.dp)
            )
        }
    }
}