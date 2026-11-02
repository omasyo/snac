package com.omasyo.snac.feature.person.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.data.models.PersonDetails
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.CapHeightLineHeightStyle
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
internal fun PersonHero(
    state: UiState<PersonDetails>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SnacColors.film)
    ) {
        when (state) {
            is UiState.Error -> {
                ErrorBox(
                    description = state.error.message,
                    onRetry = state.refresh,
                ) {
                    HeroPlaceholder()
                }
            }

            UiState.Loading -> HeroPlaceholder()

            is UiState.Success -> {
                val person = state.content
                Image(
                    imageUrl = person.backdropUrl ?: person.imageUrl,
                    modifier = Modifier.matchParentSize(),
                )
                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    SnacColors.film.copy(0.7f),
                                    SnacColors.film.copy(0.8f),
                                    SnacColors.film,
                                )
                            )
                        )
                )
                HeroLayout(
                    polaroid = {
                        Polaroid(
                            imageUrl = person.imageUrl,
                            caption = person.knownFor?.let {
                                stringResource(R.string.known_for, it)
                            },
                        )
                    },
                    name = {
                        Text(
                            text = person.name.uppercase(),
                            style = SnacTypography.display.copy(
                                lineHeightStyle = CapHeightLineHeightStyle,
                            ),
                            color = SnacColors.onFilm,
                            modifier = Modifier.silhouette(SnacColors.signalShadow),
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun HeroLayout(
    polaroid: @Composable () -> Unit,
    name: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(16.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            polaroid()
        }
        name()
    }
}

@Composable
private fun Polaroid(
    imageUrl: String?,
    caption: String?,
    modifier: Modifier = Modifier,
) {
    Box(modifier.rotate(-4f)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 12.dp)
                .silhouette(SnacColors.signalShadow, offset = DpOffset(6.dp, 6.dp))
                .background(SnacColors.card)
                .padding(8.dp)
                .width(PhotoWidth),
        ) {
            Image(
                imageUrl = imageUrl,
                modifier = Modifier.size(PhotoWidth, PhotoHeight),
            )
            if (caption != null) {
                Text(
                    text = caption,
                    style = SnacTypography.hand,
                    color = SnacColors.ink,
                )
            }
        }
        Tape(Modifier.align(Alignment.TopCenter))
    }
}

@Composable
private fun Tape(modifier: Modifier = Modifier) {
    Box(
        modifier
            .rotate(3f)
            .size(72.dp, 24.dp)
            .background(SnacColors.tape.copy(0.85f))
    )
}

@Composable
private fun HeroPlaceholder() {
    HeroLayout(
        polaroid = {
            Box(Modifier.rotate(-4f)) {
                Box(
                    Modifier
                        .padding(top = 12.dp)
                        .silhouette(SnacColors.signalShadow, offset = DpOffset(6.dp, 6.dp))
                        .background(SnacColors.card)
                        .padding(8.dp)
                        .padding(bottom = 26.dp)
                        .size(PhotoWidth, PhotoHeight)
                        .background(imageGradient())
                )
                Tape(Modifier.align(Alignment.TopCenter))
            }
        },
        name = {
            Box(
                Modifier
                    .size(240.dp, 40.dp)
                    .background(SnacColors.placeholder)
            )
        },
    )
}

private val PhotoWidth = 200.dp
private val PhotoHeight = 240.dp

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        PersonHero(state = UiState.Success(PersonDetails.PreviewSample))
    }
}

@Preview
@Composable
private fun PlaceholderPreview() {
    SnacTheme {
        PersonHero(state = UiState.Loading)
    }
}
