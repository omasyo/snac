package com.omasyo.snac.feature.search.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.snacMessage
import com.omasyo.snac.core.ui.theme.SnacTheme
import kotlinx.coroutines.flow.flowOf

private const val ROWS_PER_BLOCK = 3

/** Enough cells for two full bento blocks while the first page loads. */
private const val PLACEHOLDER_ITEM_COUNT = ROWS_PER_BLOCK * 3 * 2

private sealed interface RowSpec {
    data class Feature(val start: Int, val bigOnLeft: Boolean) : RowSpec
    data class Singles(val start: Int, val count: Int) : RowSpec
}

private fun bentoRowCount(itemCount: Int): Int = (itemCount + 2) / 3

private fun bentoRowSpec(row: Int, itemCount: Int): RowSpec {
    val start = row * 3
    val count = minOf(3, itemCount - start)
    return if (row % ROWS_PER_BLOCK == 0 && count == 3) {
        RowSpec.Feature(start, bigOnLeft = (row / ROWS_PER_BLOCK) % 2 == 0)
    } else {
        RowSpec.Singles(start, count)
    }
}

@Composable
fun <T : Any> BentoGrid(
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues.Zero,
    placeholder: @Composable () -> Unit = { BentoPlaceholderCell() },
    errorPadding: PaddingValues = PaddingValues(top = 48.dp),
    itemContent: @Composable (T) -> Unit,
) {
    val count = items.itemCount

    val cell: @Composable (Int, Modifier) -> Unit = { index, cellModifier ->
        val item = items[index]
        Box(cellModifier) {
            if (item != null) itemContent(item) else placeholder()
        }
    }

    LazyColumn(modifier = modifier, contentPadding = contentPadding) {
        when (val refreshState = items.loadState.refresh) {
            is LoadState.Loading -> {
                if (count == 0) {
                    item("refresh-loading") { PlaceholderRows(PLACEHOLDER_ITEM_COUNT, placeholder) }
                }
            }

            is LoadState.Error -> {
                if (count == 0) {
                    item("refresh-error") {
                        ErrorBox(
                            description = refreshState.error.snacMessage,
                            onRetry = items::retry,
                            alignment = Alignment.TopCenter,
                            errorPadding = errorPadding,
                        ) {
                            PlaceholderRows(PLACEHOLDER_ITEM_COUNT, placeholder)
                        }
                    }
                }
            }

            is LoadState.NotLoading -> {}
        }

        items(
            count = bentoRowCount(count),
            contentType = { bentoRowSpec(it, count)::class },
        ) { rowIndex ->
            when (val spec = bentoRowSpec(rowIndex, count)) {
                is RowSpec.Feature -> FeatureRow(spec, cell)
                is RowSpec.Singles -> SinglesRow(spec, cell)
            }
        }

        when (items.loadState.append) {
            is LoadState.Loading -> item("append-loading") {
                SinglesRow(RowSpec.Singles(start = 0, count = 3)) { _, cellModifier ->
                    Box(cellModifier) { placeholder() }
                }
            }

            is LoadState.Error -> item("append-error") {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Button(text = "Retry", onClick = items::retry)
                }
            }

            is LoadState.NotLoading -> {}
        }
    }
}

/** Lays out [itemCount] placeholder cells in the same bento rows as real items. */
@Composable
private fun PlaceholderRows(itemCount: Int, placeholder: @Composable () -> Unit) {
    val cell: @Composable (Int, Modifier) -> Unit = { _, cellModifier ->
        Box(cellModifier) { placeholder() }
    }
    Column {
        repeat(bentoRowCount(itemCount)) { row ->
            when (val spec = bentoRowSpec(row, itemCount)) {
                is RowSpec.Feature -> FeatureRow(spec, cell)
                is RowSpec.Singles -> SinglesRow(spec, cell)
            }
        }
    }
}

/** Stands in for an image cell, using the same gradient as an image that hasn't loaded. */
@Composable
fun BentoPlaceholderCell(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .padding(1.dp)
            .background(imageGradient())
    )
}

@Composable
private fun FeatureRow(spec: RowSpec.Feature, cell: @Composable (Int, Modifier) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .aspectRatio(3f / 2f)
    ) {
        @Composable
        fun RowScope.Side() = Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            cell(
                spec.start + 1, Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            cell(
                spec.start + 2, Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }

        if (!spec.bigOnLeft) Side()
        cell(
            spec.start, Modifier
                .weight(2f)
                .fillMaxHeight()
        )
        if (spec.bigOnLeft) Side()
    }
}

@Composable
private fun SinglesRow(spec: RowSpec.Singles, cell: @Composable (Int, Modifier) -> Unit) {
    Row(Modifier.fillMaxWidth()) {
        repeat(spec.count) { i ->
            cell(
                spec.start + i, Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }
        repeat(3 - spec.count) { Spacer(Modifier.weight(1f)) }
    }
}


@Preview
@Composable
private fun Preview() {
    SnacTheme {
        BentoGrid(
            items = remember { flowOf(PagingData.from(List(100) { it })) }.collectAsLazyPagingItems(),
        ) {
            Box(
                Modifier
                    .padding(1.dp)
                    .clickable {}
                    .fillMaxSize()
                    .background(Color.Cyan),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = it.toString())
            }
        }
    }
}