package com.omasyo.snac.feature.media.impl.people


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.ui.components.ErrorBox
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

@Composable
fun ColumnScope.CastTab(
    onPersonClick: (Person) -> Unit,
    cast: UiState<List<Cast>>,
    gridState: LazyGridState,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val modifier = modifier.weight(1f)
    val placeholder =
        remember {
            movableContentOf<Boolean> { scrollable ->
                Placeholder(
                    gridState,
                    scrollable,
                    contentPadding,
                    modifier
                )
            }
        }

    when (cast) {
        is UiState.Error -> {
            ErrorBox(
                description = cast.error.message,
                onRetry = cast.refresh,
            ) {
                placeholder(false)
            }
        }


        UiState.Loading -> placeholder(true)

        is UiState.Success -> {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(minSize = 160.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = contentPadding,
                modifier = modifier,
            ) {
                items(cast.content, key = { it.person.slug }) { item ->
                    CastCard(
                        cast = item,
                        onClick = { onPersonClick(item.person) },
                    )
                }
            }
        }
    }

}

@Composable
private fun CastCard(
    cast: Cast,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val rotation =
        remember(cast) { Random(cast.person.slug.hashCode()).nextInt(-3, 3).toFloat() }

    val tintColor = SnacColors.page
    Column(
        modifier = modifier
            .rotate(rotation)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .aspectRatio(2f / 3f),

        ) {
        Image(
            imageUrl = cast.person.imageUrl,
            modifier = Modifier
                .indication(interactionSource, SilhouettePressIndication())
                .border(2.dp, SnacColors.outline)
                .background(SnacColors.card)
                .padding(horizontal = 6.dp)
                .padding(top = 6.dp, bottom = 20.dp)
                .weight(1f),
        )
        Text(
            text = cast.person.name,
            style = SnacTypography.subtitle,
            maxLines = 1,
            modifier = Modifier
                .indication(
                    interactionSource, TintIndication(
                        contentOnly = true,
                        focusOpacity = 0.2f,
                        pressOpacity = 0.5f,
                    ) { tintColor })
        )
        Text(
            text = cast.character,
            style = SnacTypography.hand,
            color = SnacColors.signal,
            maxLines = 2,
            minLines = 2,
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


@OptIn(ExperimentalGridApi::class)
@Composable
private fun Placeholder(
    gridState: LazyGridState,
    scrollable: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier,
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Adaptive(minSize = 160.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding,
        userScrollEnabled = scrollable,
    ) {
        items(10) {
            val rotation = remember(it) { Random(it + 80).nextInt(-3, 3).toFloat() }
            Column(
                Modifier
                    .rotate(rotation)
                    .aspectRatio(2f / 3f)
            ) {
                Box(
                    modifier = Modifier
                        .silhouette()
                        .border(2.dp, SnacColors.outline)
                        .background(SnacColors.card)
                        .padding(horizontal = 6.dp)
                        .padding(top = 6.dp, bottom = 16.dp)
                        .background(imageGradient())
                        .fillMaxWidth()
                        .weight(1f),
                )
                Box(
                    Modifier
                        .padding(top = 8.dp)
                        .width(120.dp)
                        .height(14.dp)
                        .background(SnacColors.placeholder)
                )
                Box(
                    Modifier
                        .padding(vertical = 4.dp)
                        .width(72.dp)
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
        Column(Modifier.background(SnacColors.page)) {
            CastTab(
                onPersonClick = { },
                cast = UiState.Success(Cast.PreviewSample),
                gridState = rememberLazyGridState(),
                contentPadding = PaddingValues(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    var state by remember { mutableStateOf<UiState<List<Cast>>>(UiState.Loading) }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        state = UiState.Error(SnacResult.Error.Unexpected) {}
    }

    SnacTheme {
        Column(Modifier.background(SnacColors.page)) {
            CastTab(
                onPersonClick = { },
                cast = state,
                gridState = rememberLazyGridState(),
                contentPadding = PaddingValues(16.dp),
            )
        }
    }
}