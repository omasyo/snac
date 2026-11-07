package com.omasyo.snac.feature.media.impl.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.paging.compose.itemKey
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.components.SectionHeader
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun RelatedSection(
    onMediaClick: (Media) -> Unit,
    onSeeAllClick: () -> Unit,
    items: LazyPagingItems<out Media>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionHeader(
            title = "RELATED",
            onSeeAllClick = onSeeAllClick,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        val placeholder =
            remember { movableContentOf<Boolean> { scrollable -> Placeholder(scrollable) } }


        when (val refresh = items.loadState.refresh) {
            is LoadState.Error -> {
                ErrorBox(
                    description = refresh.error.message ?: SnacResult.Error.Unexpected.message,
                    onRetry = items::retry,
                ) {
                    placeholder(false)
                }
            }

            is LoadState.Loading -> placeholder(true)
            else -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        count = items.itemCount,
                        key = items.itemKey { it.slug },
                    ) { index ->
                        val item = items[index]
                        if (item != null) {
                            RelatedCard(
                                media = item,
                                onClick = { onMediaClick(item) },
                            )
                        } else {
                            RelatedCardPlaceholder(index)
                        }
                    }
                    when (items.loadState.append) {
                        is LoadState.Loading -> item("append-loading") {
                            RelatedCardPlaceholder(items.itemCount)
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
private fun RelatedCard(
    media: Media,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val rotation =
        remember(media) { Random(media.slug.hashCode()).nextInt(-3, 3).toFloat() }

    Column(
        modifier = modifier
            .rotate(rotation)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
    ) {
        Image(
            imageUrl = media.imageUrl,
            modifier = Modifier
                .indication(interactionSource, SilhouettePressIndication())
                .border(2.dp, SnacColors.outline)
                .background(SnacColors.card)
                .padding(6.dp)
                .size(100.dp, 160.dp),
        )
        val tintColor = SnacColors.page
        Text(
            text = media.title,
            style = SnacTypography.tag,
            modifier = Modifier
                .indication(
                    interactionSource, TintIndication(
                        contentOnly = true,
                        focusOpacity = 0.2f,
                        pressOpacity = 0.5f,
                    ) { tintColor })
        )
    }
}

@Composable
private fun Placeholder(scrollable: Boolean) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState(), scrollable)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16f.dp),
    ) {
        repeat(10) { RelatedCardPlaceholder(it) }
    }
}

@Composable
private fun RelatedCardPlaceholder(seed: Int, modifier: Modifier = Modifier) {
    val rotation = remember(seed) { Random(seed + 80).nextInt(-3, 3).toFloat() }

    Column(modifier.rotate(rotation)) {
        Box(
            modifier = Modifier
                .silhouette()
                .border(2.dp, SnacColors.outline)
                .background(SnacColors.card)
                .padding(6.dp)
                .size(100.dp, 160.dp)
                .border(2.dp, SnacColors.outline)
                .background(imageGradient())
        )
        Box(
            Modifier
                .padding(vertical = 8.dp)
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
            RelatedSection(
                onMediaClick = { },
                onSeeAllClick = { },
                items = items,
                modifier = Modifier.padding(vertical = 16.dp)
            )
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
            RelatedSection(
                onMediaClick = { },
                onSeeAllClick = { },
                items = items,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}
