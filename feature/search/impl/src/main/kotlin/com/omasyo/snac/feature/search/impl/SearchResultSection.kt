package com.omasyo.snac.feature.search.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.SectionHeader
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.search.impl.components.MediaResultCard
import com.omasyo.snac.feature.search.impl.components.ResultItemPlaceholder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun <T : Any> SearchResultSection(
    title: String,
    onSeeAllClick: (startIndex: Int) -> Unit,
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    key: ((index: Int) -> Any)? = null,
    content: @Composable (T) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val listState = rememberLazyListState()

        SectionHeader(
            title = title.uppercase(),
            onSeeAllClick = { onSeeAllClick(listState.mostlyVisibleItemIndex()) },
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        val placeholder =
            remember {
                movableContentOf<Boolean> { scrollable ->
                    SearchResultSectionPlaceholder(
                        scrollable = scrollable
                    )
                }
            }


        when (val refresh = items.loadState.refresh) {
            is LoadState.Error -> {
                ErrorBox(
                    description = refresh.error.message ?: SnacResult.Error.Unexpected.message,
                    onRetry = items::retry,
                ) {
                    placeholder(false)
                }
            }

            is LoadState.Loading if items.itemCount == 0 -> placeholder(true)
            else -> {
                LazyRow(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        count = items.itemCount,
                        key = key,
                    ) { index ->
                        val item = items[index]
                        if (item != null) {
                            content(item)
                        } else {
                            ResultItemPlaceholder(index)
                        }
                    }
                    when (items.loadState.append) {
                        is LoadState.Loading -> item("append-loading") {
                            ResultItemPlaceholder(items.itemCount)
                        }

                        is LoadState.Error -> item("append-error") {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(112.dp, 160.dp),
                            ) {
                                Button(text = "Retry", onClick = items::retry)
                            }
                        }

                        is LoadState.NotLoading -> {}
                    }
                }
            }
        }
    }
}

private fun LazyListState.mostlyVisibleItemIndex(): Int {
    val first = layoutInfo.visibleItemsInfo.firstOrNull() ?: return 0
    val index =
        if (firstVisibleItemScrollOffset > first.size / 2) first.index + 1 else first.index
    return index
}

@Composable
internal fun SearchResultSectionPlaceholder(scrollable: Boolean) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState(), scrollable)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16f.dp),
    ) {
        repeat(10) { ResultItemPlaceholder(it) }
    }
}

@Preview
@Composable
private fun Preview() {
    val items = remember { flowOf(PagingData.from(Movie.PreviewSample)) }.collectAsLazyPagingItems()
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            SearchResultSection(
                title = "Movie",
                onSeeAllClick = { },
                items = items,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                MediaResultCard(
                    media = it,
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    val flow = remember {
        MutableStateFlow(
            PagingData.empty<Movie>(
                LoadStates(
                    LoadState.Loading,
                    LoadState.Loading,
                    LoadState.Loading
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        val error = LoadState.Error(Exception(SnacResult.Error.Unexpected.message))
        flow.value = PagingData.empty(LoadStates(error, error, error))
    }

    val items = flow.collectAsLazyPagingItems()
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            SearchResultSection(
                title = "Movie",
                onSeeAllClick = { },
                items = items,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                MediaResultCard(
                    media = it,
                    onClick = {},
                )
            }
        }
    }
}
