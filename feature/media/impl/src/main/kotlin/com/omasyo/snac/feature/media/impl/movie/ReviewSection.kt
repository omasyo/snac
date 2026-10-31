package com.omasyo.snac.feature.media.impl.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.components.Review
import com.omasyo.snac.feature.media.impl.components.ReviewPreviewCard
import com.omasyo.snac.feature.media.impl.components.ReviewPreviewPlaceholder
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds


@Composable
fun ReviewSection(
    onReviewClick: (Review) -> Unit,
    onSeeAllClick: () -> Unit,
    onCommentClick: () -> Unit,
    state: UiState<List<Review>>,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row {
            Text(
                text = "RELATED",
                style = SnacTypography.title,
                color = SnacColors.ink,
                modifier = Modifier
                    .weight(1f)
                    .alignByBaseline()
                    .silhouette(SnacColors.accent),
            )
            val tintColor = SnacColors.page
            Text(
                text = "SEE ALL →",
                style = SnacTypography.label,
                color = SnacColors.signal,
                modifier = Modifier
                    .alignByBaseline()
                    .clickable(
                        interactionSource = null,
                        indication = TintIndication(
                            contentOnly = true,
                            focusOpacity = 0.2f,
                            pressOpacity = 0.5f
                        ) { tintColor },
                        onClick = onSeeAllClick,
                    ),
            )
        }

        val placeholder =
            remember { movableContentOf { Placeholder() } }

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

            is UiState.Success -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    for (review in state.content) {

                        val rotation =
                            remember(review) { Random(review.id).nextInt(-1, 1).toFloat() }

                        ReviewPreviewCard(
                            review = review,
                            onClick = { onReviewClick(review) },
                            modifier = Modifier.rotate(rotation),
                        )
                    }
                }
            }
        }

        val borderColor = SnacColors.ink
        val tintColor = SnacColors.accent
        Text(
            text = "Sign in to comment".uppercase(),
            style = SnacTypography.subtitle.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .clickable(
                    interactionSource = null,
                    indication = TintIndication { tintColor },
                    onClick = onCommentClick,
                )
                .drawBehind {
                    drawRect(
                        color = borderColor,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(
                                    4.dp.toPx(),
                                    2.dp.toPx()
                                )
                            ),
                        )
                    )
                }
                .padding(12.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun Placeholder() {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(2) {

            val rotation =
                remember(it) { Random(it + 80).nextInt(-1, 1).toFloat() }

            ReviewPreviewPlaceholder(modifier = Modifier.rotate(rotation))
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            ReviewSection(
                onReviewClick = {},
                onSeeAllClick = { },
                onCommentClick = {},
                state = UiState.Success(Review.PreviewSample.take(2)),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<List<Review>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {

            ReviewSection(
                onReviewClick = {},
                onSeeAllClick = { },
                onCommentClick = {},
                state = state,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}