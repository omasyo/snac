package com.omasyo.snac.feature.search.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.components.BackButton
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.TopBar
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.search.impl.components.MediaResultCard
import com.omasyo.snac.feature.search.impl.components.ResultItemPlaceholder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun <T : Any> SearchResultSectionScreen(
    title: String,
    onBackClick: () -> Unit,
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    initialIndex: Int,
    key: ((index: Int) -> Any)? = null,
    content: @Composable (T) -> Unit,
) {
    Column(modifier) {
        TopBar(
            modifier = Modifier.zIndex(1f),
            navigationIcon = {
                BackButton(
                    onClick = onBackClick,
                )
            }
        ) {
            Text(
                text = title,
                style = SnacTypography.title,
                color = SnacColors.ink,
                modifier = Modifier
                    .silhouette(SnacColors.accentShadow),
            )
        }

        LazyVerticalGrid(
            state = rememberLazyGridState(initialFirstVisibleItemIndex = initialIndex),
            columns = GridCells.Adaptive(96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
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


@Preview
@Composable
private fun Preview() {
    val items = remember {
        flowOf(PagingData.from(Movie.PreviewSample + Movie.PreviewSample.map {
            it.copy(
                slug = it.toString()
            )
        }))
    }.collectAsLazyPagingItems()
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            SearchResultSectionScreen(
                title = "Movie",
                onBackClick = {},
                items = items,
                initialIndex = 0,
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
            SearchResultSectionScreen(
                title = "Movie",
                onBackClick = {},
                items = items,
                initialIndex = 0,
            ) {
                MediaResultCard(
                    media = it,
                    onClick = {},
                )
            }
        }
    }
}
