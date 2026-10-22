package com.omasyo.snac.feature.charts.impl

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ClapperboardStripes
import com.omasyo.snac.core.ui.components.SelectableText
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.math.max
import kotlin.random.Random

val ChartMinWidth = 104.dp
private val ItemGap = 12.dp
private val GroupGap = 16.dp

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

enum class TimePeriod(val label: String) {
    Daily("Daily"),
    Weekly("Weekly"),
    Monthly("Monthly"),
    Yearly("Yearly"),
    AllTime("All Time"),
}

@Composable
fun ChartSelector(
    selectedChart: Chart,
    onSelectedChartChange: (Chart) -> Unit,
    selectedPeriod: TimePeriod,
    onSelectedPeriodChange: (TimePeriod) -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val progress = animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "chartFilter",
    )

    Layout(
        contents = listOf(
            {
                Chart.entries.forEach { chart ->
                    ChartFilterItem(
                        chart = chart,
                        selected = chart == selectedChart,
                        onClick = { onSelectedChartChange(chart) },
                    )
                }
            },
            {
                TimePeriod.entries.forEach { period ->
                    SelectableText(
                        label = period.label,
                        isSelected = period == selectedPeriod,
                        onClick = { onSelectedPeriodChange(period) },
                    )
                }
            },
        ),
        modifier = modifier,
    ) { (chartMeasurables, periodMeasurables), constraints ->
        val p = progress.value
        val gap = ItemGap.roundToPx()
        val groupGap = GroupGap.roundToPx()
        val chartMinWidth = ChartMinWidth.roundToPx()

        require(constraints.hasBoundedWidth) { "ChartFilter needs a bounded width" }
        val available = constraints.maxWidth

        val chartColumns = max(1, (available + gap) / (chartMinWidth + gap))
        val chartCellWidth = (available - gap * (chartColumns - 1)) / chartColumns
        val charts = chartMeasurables.map { it.measure(Constraints.fixedWidth(chartCellWidth)) }

        val periodConstraints = Constraints(maxWidth = available)
        val periods = periodMeasurables.map { it.measure(periodConstraints) }

        val chartStackHeight = charts.maxOf { it.height }
        val periodStackHeight = periods.maxOf { it.height }
        val rowHeight = max(chartStackHeight, periodStackHeight)
        val chartCollapsed = IntOffset(0, (rowHeight - chartStackHeight) / 2)
        val periodCollapsed =
            IntOffset(chartCellWidth + groupGap, (rowHeight - periodStackHeight) / 2)
        val selectedPeriodWidth = periods[selectedPeriod.ordinal].width
        val collapsedWidth = chartCellWidth + groupGap + selectedPeriodWidth

        fun gridOffsets(
            ps: List<Placeable>,
            top: Int,
            columns: Int,
            cellWidth: Int,
        ): Pair<List<IntOffset>, Int> {
            val rowHeights = ps.chunked(columns) { row -> row.maxOf { it.height } }
            val offsets = ps.indices.map { i ->
                val row = i / columns
                IntOffset(
                    x = (i % columns) * (cellWidth + gap),
                    y = top + rowHeights.take(row).sum() + gap * row,
                )
            }
            return offsets to rowHeights.sum() + gap * (rowHeights.size - 1)
        }

        fun flowOffsets(ps: List<Placeable>, top: Int): Pair<List<IntOffset>, Int> {
            val offsets = ArrayList<IntOffset>(ps.size)
            var x = 0
            var y = top
            var lineHeight = 0
            for (placeable in ps) {
                if (x > 0 && x + placeable.width > available) {
                    x = 0
                    y += lineHeight + gap
                    lineHeight = 0
                }
                offsets += IntOffset(x, y)
                x += placeable.width + gap
                lineHeight = max(lineHeight, placeable.height)
            }
            return offsets to (y - top + lineHeight)
        }

        val (chartGrid, chartGridHeight) = gridOffsets(
            charts,
            top = 0,
            columns = chartColumns,
            cellWidth = chartCellWidth,
        )
        val (periodFlow, periodFlowHeight) = flowOffsets(
            periods,
            top = chartGridHeight + groupGap,
        )
        val expandedWidth = chartColumns * chartCellWidth + gap * (chartColumns - 1)
        val expandedHeight = chartGridHeight + groupGap + periodFlowHeight

        val width = constraints.constrainWidth(lerp(collapsedWidth, expandedWidth, p))
        val height = constraints.constrainHeight(lerp(rowHeight, expandedHeight, p))

        layout(width, height) {
            charts.forEachIndexed { i, placeable ->
                val selected = Chart.entries[i] == selectedChart
                placeable.place(
                    position = lerp(chartCollapsed, chartGrid[i], p),
                    zIndex = if (selected) 1f else 0f,
                )
            }
            periods.forEachIndexed { i, placeable ->
                val selected = TimePeriod.entries[i] == selectedPeriod
                val position = lerp(periodCollapsed, periodFlow[i], p)
                if (selected) {
                    placeable.place(position, zIndex = 1f)
                } else {
                    placeable.placeWithLayer(position) { alpha = p.coerceIn(0f, 1f) }
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
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    var expanded by remember { mutableStateOf(true) }
    var selectedChart by remember { mutableStateOf(Chart.Trending) }
    var selectedPeriod by remember { mutableStateOf(TimePeriod.Weekly) }
    SnacTheme {
        Column (
            modifier = Modifier
                .background(SnacColors.page)
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            ChartSelector(
                selectedChart = selectedChart,
                onSelectedChartChange = {
                    if (!expanded) {
                        expanded = true
                    } else {
                        selectedChart = it
                    }
                },
                selectedPeriod = selectedPeriod,
                onSelectedPeriodChange = {
                    if (!expanded) {
                        expanded = true
                    } else {
                        selectedPeriod = it
                    }
                },
                expanded = expanded,
            )

            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f),
            ) {

            }
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
                    .padding(8.dp)
                    .width(120.dp),
            )
        }
    }
}