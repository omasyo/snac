package com.omasyo.snac.feature.media.impl.show

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.format.metaLine
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R
import com.omasyo.snac.feature.media.impl.components.SectionHeader
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun SeasonsSection(
    onSeasonClick: (Season) -> Unit,
    state: UiState<List<Season>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SectionHeader(
            title = "SEASONS",
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        val placeholder =
            remember { movableContentOf<Boolean> { scrollable -> Placeholder(scrollable) } }

        when (state) {
            is UiState.Error -> {
                ErrorBox(
                    description = state.error.message,
                    onRetry = state.refresh,
                ) {
                    placeholder(false)
                }
            }


            UiState.Loading -> placeholder(true)

            is UiState.Success -> {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.content, key = { it.number }) { item ->
                        SeasonCard(
                            season = item,
                            onClick = { onSeasonClick(item) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SeasonCard(
    season: Season,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val rotation =
        remember(season) { Random(season.number).nextInt(-3, 3).toFloat() }

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
            imageUrl = season.posterUrl,
            modifier = Modifier
                .indication(interactionSource, SilhouettePressIndication())
                .border(2.dp, SnacColors.outline)
                .background(SnacColors.card)
                .padding(6.dp)
                .size(100.dp, 160.dp),
        )
        val tintColor = SnacColors.page
        Text(
            text = season.title,
            style = SnacTypography.tag,
            modifier = Modifier
                .indication(
                    interactionSource, TintIndication(
                        contentOnly = true,
                        focusOpacity = 0.2f,
                        pressOpacity = 0.5f,
                    ) { tintColor })
        )
        Text(
            text = season.caption(),
            style = SnacTypography.hand,
            color = SnacColors.signal,
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
private fun Season.caption(): String {
    val episodes =
        if (airedEpisodes < episodeCount) {
            pluralStringResource(R.plurals.season_aired_episodes, episodeCount, airedEpisodes, episodeCount)
        } else {
            pluralStringResource(R.plurals.season_episodes, episodeCount, episodeCount)
        }
    return metaLine(episodes, firstAired?.year?.toString())
}

@Composable
private fun Placeholder(scrollable: Boolean) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState(), scrollable)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16f.dp),
    ) {
        repeat(5) {
            val rotation = remember(it) { Random(it + 80).nextInt(-3, 3).toFloat() }

            Column(Modifier.rotate(rotation)) {
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
                        .padding(top = 8.dp)
                        .width(72.dp)
                        .height(14.dp)
                        .background(SnacColors.placeholder)
                )
                Box(
                    Modifier
                        .padding(vertical = 4.dp)
                        .width(56.dp)
                        .height(12.dp)
                        .background(SnacColors.placeholder)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            SeasonsSection(
                onSeasonClick = { },
                state = UiState.Success(Season.PreviewSample),
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<List<Season>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            SeasonsSection(
                onSeasonClick = { },
                state = state,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}
