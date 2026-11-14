package com.omasyo.snac.feature.media.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.format.formatCount
import com.omasyo.snac.core.common.format.formatRating
import com.omasyo.snac.core.data.models.Rating
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.LocalContentColor
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

val colors
    @Composable get() = listOf(
        SnacColors.accent to SnacColors.onAccent,
        SnacColors.chip to SnacColors.ink,
        SnacColors.signal to SnacColors.onSignal,
        SnacColors.chip to SnacColors.ink,
        SnacColors.film to SnacColors.onFilm,
        SnacColors.chip to SnacColors.ink,
    )

@Composable
fun RatingsSection(
    state: UiState<Ratings>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "RATINGS",
            style = SnacTypography.title,
            modifier = Modifier.silhouette(SnacColors.accentShadow)
        )

        val placeholder = remember { movableContentOf { Placeholder() } }

        when (state) {
            is UiState.Error -> {
                ErrorBox(
                    description = state.error.message,
                    onRetry = state.refresh,
                ) {
                    placeholder()
                }
            }

            UiState.Loading -> placeholder()

            is UiState.Success -> Content(state.content)
        }
    }
}

@Composable
private fun Content(ratings: Ratings) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clickable(
                            indication = SilhouettePressIndication(),
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { }
                        )
                        .size(100.dp)
                        .border(4.dp, SnacColors.outline, CircleShape)
                        .background(SnacColors.signal, CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = ratings.traktRating.formatRating(),
                            style = SnacTypography.headline.copy(
                                lineHeightStyle = CapHeightLineHeightStyle,
                            ),
                            color = SnacColors.onSignal,
                        )
                        Text(
                            text = "Trakt",
                            color = SnacColors.onSignal,
                            style = SnacTypography.overline,
                        )
                    }
                }

                Text(
                    text = pluralStringResource(
                        R.plurals.votes,
                        ratings.voteCount,
                        ratings.voteCount.formatCount(),
                    ),
                    color = SnacColors.muted,
                    style = SnacTypography.overline,
                )
            }
            Chart(
                distribution = ratings.distribution,
                modifier = Modifier.height(124.dp),
            )
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 3,
        ) {
            ratings.externalRatings.forEachIndexed { index, rating ->
                val (backgroundColor, contentColor) = colors[index % colors.size]

                val rotation =
                    remember(rating) { Random(rating.hashCode()).nextInt(-2, 2).toFloat() }

                RatingCard(
                    rating = rating,
                    contentColor = contentColor,
                    backgroundColor = backgroundColor,
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotation),
                )
            }
        }
    }
}

@Composable
private fun Chart(
    distribution: Map<String, Int>,
    modifier: Modifier = Modifier,
) {
    val max = remember(distribution) { distribution.values.maxOrNull()?.coerceAtLeast(1) ?: 1 }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        for ((rating, count) in distribution) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .background(if (count == max) SnacColors.signal else SnacColors.outline)
                            .fillMaxHeight(count.toFloat() / max.toFloat())
                            .fillMaxWidth()
                    )
                }
                Text(
                    text = rating,
                    style = SnacTypography.overline,
                )
            }
        }
    }
}

@Composable
private fun RatingCard(
    rating: Rating,
    contentColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Column(
            modifier = modifier
                .clickable(
                    indication = SilhouettePressIndication(),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { }
                )
                .border(3.dp, SnacColors.outline)
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Text(
                text = rating.title,
                maxLines = 1,
                style = SnacTypography.overline,
            )
            BasicText(
                text = buildAnnotatedString {
                    append(rating.rating)
                    rating.outOf?.let {
                        withStyle(SnacTypography.tag.toSpanStyle()) {
                            append("/")
                            append(it)
                        }
                    }
                },
                style = SnacTypography.title.copy(
                    lineHeightStyle = CapHeightLineHeightStyle,
                ),
                color = { contentColor },
            )
            Text(
                text = rating.extra,
                style = SnacTypography.caption,
            )
        }
    }
}

@Composable
private fun Placeholder() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row {
            Box(
                modifier = Modifier
                    .silhouette()
                    .size(100.dp)
                    .border(4.dp, SnacColors.outline, CircleShape)
                    .background(SnacColors.placeholder, CircleShape)
            )
            Row(
                modifier = Modifier.height(124.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                PlaceholderBarHeights.forEach { fraction ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 16.dp)
                            .fillMaxHeight(fraction)
                            .background(SnacColors.placeholder)
                    )
                }
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 3,
        ) {
            repeat(6) {
                val rotation = remember(it) { Random(it + 80).nextInt(-2, 2).toFloat() }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotation)
                        .silhouette()
                        .border(3.dp, SnacColors.outline)
                        .background(SnacColors.chip)
                        .padding(12.dp)
                ) {
                    Box(
                        Modifier
                            .size(40.dp, 10.dp)
                            .background(SnacColors.placeholder)
                    )
                    Box(
                        Modifier
                            .size(56.dp, 24.dp)
                            .background(SnacColors.placeholder)
                    )
                    Box(
                        Modifier
                            .size(64.dp, 12.dp)
                            .background(SnacColors.placeholder)
                    )
                }
            }
        }
    }
}

private val PlaceholderBarHeights =
    listOf(0.1f, 0.1f, 0.1f, 0.1f, 0.15f, 0.25f, 0.4f, 0.75f, 0.5f, 1f)

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            RatingsSection(
                UiState.Success(Ratings.PreviewSample), Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<Ratings>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            RatingsSection(
                state, Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            )
        }
    }
}