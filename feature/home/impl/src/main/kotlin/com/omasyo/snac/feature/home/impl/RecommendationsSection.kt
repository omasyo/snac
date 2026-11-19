package com.omasyo.snac.feature.home.impl

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Recommendation
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.PosterAspectRatio
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun RecommendationsSection(
    onMediaClick: (Media) -> Unit,
    moviesState: UiState<List<Recommendation>>,
    showsState: UiState<List<Recommendation>>,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableStateOf(MediaTab.Movie) }
    val stateHolder = rememberSaveableStateHolder()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "FOR YOU",
                    style = SnacTypography.title,
                    color = SnacColors.ink,
                    modifier = Modifier.silhouette(SnacColors.accentShadow),
                )
            }
            MediaToggle(
                selectedTab = selectedTab,
                onTabClick = { selectedTab = it },
                color = SnacColors.ink,
                backgroundColor = SnacColors.card,
                selectedColor = SnacColors.ink,
                selectedContentColor = SnacColors.accent,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = { //TODO different animation ideas, let the tickets fall into place
                val towards =
                    if (targetState > initialState) AnimatedContentTransitionScope.SlideDirection.Left
                    else AnimatedContentTransitionScope.SlideDirection.Right
                slideIntoContainer(towards) togetherWith slideOutOfContainer(towards)
            },
            label = "recommendations",
        ) { tab ->
            val items = when (tab) {
                MediaTab.Movie -> moviesState
                MediaTab.Show -> showsState
            }

            val placeholder =
                remember { movableContentOf<Boolean> { scrollable -> Placeholder(scrollable) } }

            stateHolder.SaveableStateProvider(tab) {
                when (items) {
                    is UiState.Error -> {
                        ErrorBox(
                            description = items.error.message,
                            onRetry = items.refresh,
                        ) {
                            placeholder(false)
                        }
                    }

                    UiState.Loading -> placeholder(true)

                    is UiState.Success -> {
                        if (items.content.isEmpty()) {
                            Text(
                                text = stringResource(R.string.recommendations_empty),
                                style = SnacTypography.hand,
                                color = SnacColors.muted,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        } else {
                            Row(items.content, onMediaClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Row(
    items: List<Recommendation>,
    onMediaClick: (Media) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items, key = { it.media.slug }) { item ->
            val media = item.media
            val rotation = Random(media.slug.hashCode()).nextInt(-2, 2).toFloat()

            TicketCard(
                item = item,
                onClick = { onMediaClick(media) },
                modifier = Modifier
                    .width(TicketWidth)
                    .rotate(rotation),
            )
        }
    }
}

@Composable
private fun TicketCard(
    item: Recommendation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val media = item.media
    val meta = listOfNotNull(
        when (media) {
            is Movie -> "Movie"
            is Show -> "Show"
        },
        media.year?.toString(),
        item.genre?.name,
    ).joinToString(" · ")
    TicketCardLayout(
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = SilhouettePressIndication(offset = TicketShadowOffset),
            onClick = onClick,
        ),
        poster = {
            Image(
                imageUrl = media.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(PosterAspectRatio),
            )
        },
        details = {
            Text(
                text = media.title.uppercase(),
                style = SnacTypography.subtitle,
                color = SnacColors.ink,
                maxLines = 1,
            )
            Text(
                text = meta,
                style = SnacTypography.label,
                color = SnacColors.muted,
                maxLines = 1,
            )
        },
    )
}

@Composable
private fun TicketCardLayout(
    poster: @Composable () -> Unit,
    details: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    val lineColor = SnacColors.outline
    Column(
        modifier = modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .background(SnacColors.card)
            .drawBehind {
                val stroke = 2.dp.toPx()
                drawRect(
                    color = lineColor,
                    topLeft = Offset(stroke / 2, stroke / 2),
                    size = Size(size.width - stroke, size.height - stroke),
                    style = Stroke(stroke),
                )
            },
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .border(2.dp, lineColor),
        ) {
            poster()
        }
        TearLine(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
        )
        Column(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
            content = details,
        )
    }
}

@Composable
private fun TearLine(modifier: Modifier = Modifier) {
    val lineColor = SnacColors.outline
    Canvas(modifier) {
        val y = size.height / 2
        val radius = 8.dp.toPx()
        val stroke = 2.dp.toPx()
        drawLine(
            color = lineColor,
            start = Offset(radius, y),
            end = Offset(size.width - radius, y),
            strokeWidth = stroke,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(4.dp.toPx(), 3.dp.toPx())
            ),
        )
        for (x in listOf(0f, size.width)) {
            drawCircle(Color.Black, radius, Offset(x, y), blendMode = BlendMode.Clear)
            drawArc(
                color = lineColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(x - radius, y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(stroke),
            )
        }
    }
}

@Composable
private fun Placeholder(scrollable: Boolean) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState(), scrollable)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(3) {
            TicketCardLayout(
                modifier = Modifier
                    .width(TicketWidth)
                    .silhouette(offset = TicketShadowOffset),
                poster = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(PosterAspectRatio)
                            .background(imageGradient())
                    )
                },
                details = {
                    Box(
                        Modifier
                            .padding(vertical = 4.dp)
                            .width(120.dp)
                            .height(18.dp)
                            .background(SnacColors.placeholder)
                    )
                    Box(
                        Modifier
                            .width(100.dp)
                            .height(14.dp)
                            .background(SnacColors.placeholder)
                    )
                },
            )
        }
    }
}

private val TicketWidth = 200.dp
private val TicketShadowOffset = DpOffset(4.dp, 4.dp)

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(
            Modifier
                .background(SnacColors.page)
                .padding(vertical = 16.dp)
        ) {
            RecommendationsSection(
                onMediaClick = {},
                moviesState = UiState.Success(Recommendation.MoviePreviewSample),
                showsState = UiState.Success(emptyList()),
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var moviesState by remember { mutableStateOf<UiState<List<Recommendation>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        moviesState = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        Box(
            Modifier
                .background(SnacColors.page)
                .padding(vertical = 16.dp)
        ) {
            RecommendationsSection(
                onMediaClick = {},
                moviesState = moviesState,
                showsState = UiState.Loading,
            )
        }
    }
}
