package com.omasyo.snac.feature.media.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.LocalContentColor
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.movie.Rating
import com.omasyo.snac.feature.media.impl.movie.Ratings
import java.text.DecimalFormat
import kotlin.random.Random

val colors
    @Composable get() = listOf(
        SnacColors.accent to SnacColors.onAccent,
        SnacColors.chip to SnacColors.ink,
        SnacColors.signal to SnacColors.onFilm,
        SnacColors.chip to SnacColors.ink,
        SnacColors.film to SnacColors.onFilm,
        SnacColors.chip to SnacColors.ink,
    )

@Composable
fun RatingsSection(
    ratings: Ratings,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "RATINGS",
            style = SnacTypography.title,
            modifier = Modifier.silhouette(SnacColors.accent)
        )
        Row {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .silhouette()
                        .size(100.dp)
                        .border(4.dp, SnacColors.ink, CircleShape)
                        .background(SnacColors.signal, CircleShape)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = DecimalFormat("#.#").format(ratings.traktRating),
                            style = SnacTypography.headline.copy(
                                lineHeightStyle = CapHeightLineHeightStyle,
                            ),
                            color = SnacColors.onFilm,
                        )
                        Text(
                            text = "Trakt",
                            color = SnacColors.onFilm,
                            style = SnacTypography.overline,
                        )
                    }
                }

                Text(
                    text = "${ratings.voteCount} votes", //TODO: pluralize
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
                val (backgroundColor, contentColor) = colors[index]

                val rotation =
                    remember(rating) { Random(rating.hashCode()).nextInt(-3, 3).toFloat() }

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
    val max = remember(distribution) { distribution.values.max() }
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
                            .background(if (count == max) SnacColors.signal else SnacColors.ink)
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

                .silhouette()
                .border(3.dp, SnacColors.ink)
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

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            RatingsSection(
                Ratings.PreviewSample, Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            )
        }
    }
}