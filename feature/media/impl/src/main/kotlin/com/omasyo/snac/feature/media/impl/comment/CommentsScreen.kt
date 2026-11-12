package com.omasyo.snac.feature.media.impl.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
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
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Reply
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.heroScrollable
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.common.MediaDetailsLayout


internal data class HeroContent(
    val title: String,
    val posterUrl: String?,
    val secondaryPosterUrl: String?,
    val extra: String,
) {
    companion object {
        val Empty = HeroContent(
            title = "",
            posterUrl = null,
            secondaryPosterUrl = null,
            extra = "",
        )
    }
}

@Composable
internal fun CommentsScreen(
    hero: HeroContent,
    comments: List<Comment>,
    replies: List<Reply>,
    onBackClick: () -> Unit,
    onCommentClick: (Comment) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    MediaDetailsLayout(
        onBackClick = onBackClick,
        title = hero.title,
        hero = {
            Column(Modifier.heroScrollable(listState)) {
                Hero(hero)
            }
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
            items(comments, key = { it.id }) { comment ->
                CommentCard(
                    comment = comment,
                    replies = replies,
                    onExpand = { onCommentClick(comment) },
                )
            }
        }
    }
}

@Composable
private fun Hero(
    hero: HeroContent,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .film()
            .background(SnacColors.film)
    ) {
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
    SnacTheme {
        CommentsScreen(
            hero = PreviewHero,
            comments = Comment.PreviewSample,
            replies = Reply.PreviewSample,
            onBackClick = {},
            onCommentClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
