package com.omasyo.snac.feature.search.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.R
import com.omasyo.snac.core.ui.components.ClapperboardStripes
import com.omasyo.snac.core.ui.components.Icon
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.models.message
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration.Companion.seconds

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
) {
    SearchScreen(
        onSearchClick = {},
        onMediaClick = {},
        media = remember { flowOf(PagingData.from<Media>(Movie.PreviewSample)) }.collectAsLazyPagingItems(),
        modifier = modifier
    )
}

@Composable
fun SearchScreen(
    onSearchClick: () -> Unit,
    onMediaClick: () -> Unit,
    media: LazyPagingItems<Media>,
    modifier: Modifier = Modifier,
) {

    SearchScreenLayout(
        modifier = modifier.background(SnacColors.page),
        header = {
            val shadowColor = SnacColors.signalShadow
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp, bottom = 16.dp)
                    .rotate(-1f)
                    .clickable(
                        indication = SilhouettePressIndication(
                            color = { shadowColor },
                            offset = DpOffset(6.dp, 6.dp)
                        ),
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onSearchClick,
                    )
                    .border(4.dp, SnacColors.outline)
                    .background(SnacColors.film)
            ) {
                ClapperboardStripes(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        icon = R.drawable.magnifying_glass_bold,
                        contentDescription = null,
                        tint = SnacColors.onFilm,
                    )
                    Text(
                        text = "Films, series, people, lists",
                        style = SnacTypography.body,
                        color = SnacColors.faint,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        },
    ) { innerPadding ->


        BentoGrid(
            contentPadding = innerPadding,
            items = media,
        ) { item ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = onMediaClick
                    )
            ) {
                Image(
                    imageUrl = item.imageUrl,
                    modifier = Modifier.matchParentSize(),
                )
            }
        }
    }
}

@Composable
private fun SearchScreenLayout(
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {

    SubcomposeLayout(modifier) { constraint ->
        val headerPlaceable =
            subcompose("header") { header() }.first().measure(constraint.copy(minHeight = 0))

        val headerHeight = headerPlaceable.height.toDp()

        val gridPlaceable =
            subcompose("grid") {
                val windowPaddingValues = WindowInsets.systemBars.asPaddingValues()
                content(object : PaddingValues {
                    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
                        windowPaddingValues.calculateLeftPadding(layoutDirection)

                    override fun calculateTopPadding(): Dp = headerHeight

                    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
                        windowPaddingValues.calculateRightPadding(layoutDirection)

                    override fun calculateBottomPadding(): Dp =
                        windowPaddingValues.calculateBottomPadding()
                })
            }.first()
                .measure(constraint)

        layout(gridPlaceable.width, gridPlaceable.height) {
            gridPlaceable.placeRelative(0, 0)
            headerPlaceable.placeRelative(0, 0)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val media =
        remember { flowOf(PagingData.from<Media>(Movie.PreviewSample)) }.collectAsLazyPagingItems()
    SnacTheme {
        SearchScreen(
            onSearchClick = {},
            onMediaClick = {},
            media = media,
        )
    }
}

@Preview
@Composable
private fun LoadingToErrorPreview() {
    val flow = remember {
        MutableStateFlow(
            PagingData.empty<Media>(
                LoadStates(
                    LoadState.Loading,
                    LoadState.Loading,
                    LoadState.Loading,
                )
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(3.seconds)
        val error = LoadState.Error(Exception(SnacResult.Error.Unexpected.message))
        flow.value = PagingData.empty(LoadStates(error, error, error))
    }

    val media = flow.collectAsLazyPagingItems()
    SnacTheme {
        SearchScreen(
            onSearchClick = {},
            onMediaClick = {},
            media = media,
        )
    }
}