package com.omasyo.snac.feature.search.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.components.ArrowLink
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun <T : Any> SearchResultSection(
    title: String,
    onSeeAllClick: () -> Unit,
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    key: ((index: Int) -> Any)? = null,
    content: @Composable (T) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionHeader(
            title = title.uppercase(),
            onSeeAllClick = onSeeAllClick,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        val placeholder =
            remember { movableContentOf<Boolean> { scrollable -> Placeholder(scrollable = scrollable) } }


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
                                modifier = Modifier.size(112.dp, 172.dp),
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

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onSeeAllClick: (() -> Unit)? = null,
) {
    Row(modifier = modifier) {
        Text(
            text = title,
            style = SnacTypography.title,
            color = SnacColors.ink,
            modifier = Modifier
                .weight(1f)
                .alignByBaseline()
                .silhouette(SnacColors.accentShadow),
        )
        if (onSeeAllClick != null) {
            ArrowLink(
                text = "SEE ALL",
                onClick = onSeeAllClick,
                modifier = Modifier.alignByBaseline(),
            )
        }
    }
}

@Composable
internal fun Placeholder(scrollable: Boolean) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState(), scrollable)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16f.dp),
    ) {
        repeat(10) { ResultItemPlaceholder(it) }
    }
}


@Composable
internal fun ResultItemPlaceholder(seed: Int, modifier: Modifier = Modifier) {
    val rotation = remember(seed) { Random(seed + 80).nextInt(-3, 3).toFloat() }

    Column(
        modifier
            .rotate(rotation)
            .width(112.dp)
    ) {
        Box(
            modifier = Modifier
                .silhouette()
                .border(2.dp, SnacColors.outline)
                .background(SnacColors.card)
                .padding(6.dp)
                .fillMaxWidth()
                .height(160.dp)
                .border(2.dp, SnacColors.outline)
                .background(imageGradient())
        )
        Box(
            Modifier
                .padding(top = 8.dp, bottom = 2.dp)
                .width(96.dp)
                .height(14.dp)
                .background(SnacColors.placeholder)
        )
        Box(
            Modifier
                .padding(bottom = 8.dp)
                .width(72.dp)
                .height(14.dp)
                .background(SnacColors.placeholder)
        )
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
