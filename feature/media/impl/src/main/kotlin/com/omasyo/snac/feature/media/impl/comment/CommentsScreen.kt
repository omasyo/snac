package com.omasyo.snac.feature.media.impl.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Reply
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.heroScrollable
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.mapOrNull
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.api.navigation.CommentsNavKey
import com.omasyo.snac.feature.media.impl.common.MediaDetailsLayout
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.coroutines.flow.flowOf


data class HeroContent(
    val title: String,
    val posterUrl: String?,
    val secondaryPosterUrl: String?,
    val extra: String,
)

@Composable
internal fun CommentsScreen(
    key: CommentsNavKey,
    onBackClick: () -> Unit,
    onCommentClick: (Comment) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommentsViewModel = assistedMetroViewModel<CommentsViewModel, CommentsViewModel.Factory> {
        create(key)
    },
) {
    CommentsScreen(
        hero = viewModel.hero.collectAsStateWithLifecycle().value,
        comments = viewModel.comments.collectAsLazyPagingItems(),
        topReplies = viewModel.topReplies.collectAsStateWithLifecycle().value,
        onTopRepliesNeeded = viewModel::loadTopReplies,
        onBackClick = onBackClick,
        onCommentClick = onCommentClick,
        modifier = modifier,
    )
}

@Composable
internal fun CommentsScreen(
    hero: UiState<HeroContent>,
    comments: LazyPagingItems<Comment>,
    topReplies: Map<Long, UiState<List<Reply>>>,
    onTopRepliesNeeded: (Comment) -> Unit,
    onBackClick: () -> Unit,
    onCommentClick: (Comment) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    MediaDetailsLayout(
        onBackClick = onBackClick,
        title = hero.mapOrNull { it.title } ?: "",
        hero = {
            Hero(
                state = hero,
                modifier = Modifier.heroScrollable(listState)
            )
        },
        modifier = modifier,
    ) { contentPadding ->
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding()),
        ) {
            when (val refreshState = comments.loadState.refresh) {
                is LoadState.Loading -> {
                    if (comments.itemCount == 0) {
                        item("refresh-loading") { Placeholder() }
                    }
                }

                is LoadState.Error -> {
                    item("refresh-error") {
                        ErrorBox(
                            description = refreshState.error.message
                                ?: SnacResult.Error.Unexpected.message,
                            onRetry = comments::retry,
                            alignment = Alignment.TopCenter,
                            errorPadding = PaddingValues(top = 48.dp),
                        ) {
                            Placeholder()
                        }
                    }
                }

                is LoadState.NotLoading -> {}
            }

            items(
                count = comments.itemCount,
                key = comments.itemKey { it.id },
            ) { index ->
                val comment = comments[index]
                if (comment != null) {
                    LaunchedEffect(comment.id) { onTopRepliesNeeded(comment) }
                    CommentCard(
                        comment = comment,
                        replies = topReplies[comment.id] ?: UiState.Loading,
                        onExpand = { onCommentClick(comment) },
                    )
                } else {
                    CommentCardPlaceholder(seed = index)
                }
            }
            when (comments.loadState.append) {
                is LoadState.Loading -> item("append-loading") {
                    CommentCardPlaceholder(seed = comments.itemCount)
                }

                is LoadState.Error -> item("append-error") {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(text = "Retry", onClick = comments::retry)
                    }
                }

                is LoadState.NotLoading -> {}
            }
        }
    }
}

private const val PLACEHOLDER_COUNT = 4

@Composable
private fun Hero(
    state: UiState<HeroContent>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .film()
            .background(SnacColors.film)
    ) {
        when (state) {
            is UiState.Error -> ErrorBox(
                description = state.error.message,
                onRetry = state.refresh,
                modifier = Modifier.matchParentSize(),
            ) {}

            UiState.Loading -> HeroPlaceholder(Modifier.align(Alignment.BottomStart))

            is UiState.Success -> HeroLoaded(state.content)
        }
    }
}

@Composable
private fun HeroPlaceholder(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .rotate(-4f)
            .padding(horizontal = 32.dp, vertical = 32.dp),
    ) {
        Box(
            modifier = Modifier
                .silhouette(SnacColors.signalShadow)
                .border(4.dp, SnacColors.chip)
                .size(74.dp, 108.dp)
                .background(imageGradient())
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                Modifier
                    .size(72.dp, 14.dp)
                    .background(SnacColors.accent)
            )
            Box(
                Modifier
                    .size(200.dp, 32.dp)
                    .background(SnacColors.faint)
            )
            Box(
                Modifier
                    .size(120.dp, 12.dp)
                    .background(SnacColors.faint)
            )
        }
    }
}

@Composable
private fun BoxScope.HeroLoaded(hero: HeroContent) {
    val brush = Brush.linearGradient(
        listOf(
            Color.Transparent,
            Color.Transparent,
            SnacColors.film.copy(0.5f),
        ), end = Offset(0f, Float.POSITIVE_INFINITY)
    )
    Image(
        imageUrl = hero.secondaryPosterUrl ?: hero.posterUrl,
        modifier = Modifier
            .matchParentSize()
            .drawWithContent {
                drawContent()
                drawRect(brush)
            },
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .rotate(if (hero.secondaryPosterUrl != null) -4f else 0f)
            .padding(horizontal = 32.dp, vertical = 32.dp)
            .align(Alignment.BottomStart),
    ) {
        if (hero.secondaryPosterUrl != null) {
            Image(
                imageUrl = hero.posterUrl,
                modifier = Modifier
                    .silhouette(SnacColors.signalShadow)
                    .border(4.dp, SnacColors.chip)
                    .size(74.dp, 108.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Comments",
                style = SnacTypography.overline,
                color = SnacColors.onAccent,
                modifier = Modifier
                    .background(SnacColors.accent)
                    .padding(horizontal = 6.dp)
            )
            Text(
                text = hero.title,
                style = SnacTypography.headline.copy(
                    lineHeightStyle = CapHeightLineHeightStyle,
                    hyphens = Hyphens.Auto,
                ),
                color = SnacColors.onFilm,
                modifier = Modifier.silhouette(SnacColors.signalShadow)
            )
            Text(
                text = hero.extra,
                style = SnacTypography.caption,
                color = SnacColors.mutedOnFilm
            )
        }
    }
}

@Composable
private fun Placeholder() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        repeat(PLACEHOLDER_COUNT) { CommentCardPlaceholder(seed = it) }
    }
}

private val PreviewHero = HeroContent(
    title = "Frankenstein",
    posterUrl = "",
    secondaryPosterUrl = "",
    extra = "2025 · Guillermo del Toro"
)

@Preview
@Composable
private fun Preview() {
    val comments =
        remember { flowOf(PagingData.from(Comment.PreviewSample)) }.collectAsLazyPagingItems()
    SnacTheme {
        CommentsScreen(
            hero = UiState.Success(PreviewHero),
            comments = comments,
            topReplies = Comment.PreviewSample.associate {
                it.id to UiState.Success(Reply.PreviewSample)
            },
            onTopRepliesNeeded = {},
            onBackClick = {},
            onCommentClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun LoadingPreview() {
    val loading =
        LoadStates(LoadState.Loading, LoadState.NotLoading(false), LoadState.NotLoading(false))
    val comments =
        remember { flowOf(PagingData.empty<Comment>(loading)) }.collectAsLazyPagingItems()
    SnacTheme {
        CommentsScreen(
            hero = UiState.Loading,
            comments = comments,
            topReplies = emptyMap(),
            onTopRepliesNeeded = {},
            onBackClick = {},
            onCommentClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}


@Preview
@Composable
private fun ErrorPreview() {
    val loading = LoadStates(
        LoadState.Error(Exception("Couldn't get comments")),
        LoadState.NotLoading(false),
        LoadState.NotLoading(false)
    )
    val comments =
        remember { flowOf(PagingData.empty<Comment>(loading)) }.collectAsLazyPagingItems()
    SnacTheme {
        CommentsScreen(
            hero = UiState.Loading,
            comments = comments,
            topReplies = emptyMap(),
            onTopRepliesNeeded = {},
            onBackClick = {},
            onCommentClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
