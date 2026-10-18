package com.omasyo.snac

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.bottombar.BottomBar
import com.omasyo.snac.core.ui.components.navigation.TopLevelRoute
import com.omasyo.snac.core.ui.modifiers.LocalBottomBarPadding
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.feature.home.HomeScreen
import com.omasyo.snac.feature.home.TrendingTab

@Composable
fun Test(
    bottomBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    SubcomposeLayout(modifier) { constraints ->
        val bottomBarPlaceable = subcompose("bottomBar", bottomBar).first().measure(constraints)

        val layoutHeight = constraints.maxHeight
        val bottomBarHeight = bottomBarPlaceable.height

        val contentPlaceable =
            subcompose("content") {
                CompositionLocalProvider(LocalBottomBarPadding provides PaddingValues(bottom = bottomBarHeight.toDp())) {
                    content()
                }
            }.first()
                .measure(constraints)


        layout(constraints.maxWidth, constraints.maxHeight) {
            contentPlaceable.placeRelative(0, 0)
            bottomBarPlaceable.placeRelative(0, layoutHeight - bottomBarHeight)
        }
    }
}

enum class TestDestination(override val label: String) : TopLevelRoute {
    Home("Home"),
    Charts("Charts"),
    Search("Search"),
    Calendar("Calendar"),
    Me("Me"),
}

@Preview
@Composable
private fun Preview() {
    var destination by remember { mutableStateOf<TopLevelRoute>(TestDestination.Home) }
    SnacTheme {
        Test(
            bottomBar = {
                BottomBar(
                    currentDestination = destination,
                    onCurrentDestinationChange = { destination = it },
                    destinations = TestDestination.entries,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }
        ) {
            HomeScreen(
                onSignInClick = {},
                onMovieClick = {},
                onShowClick = {},
                onAllChartsClick = {},
                trendingTab = TrendingTab.Movie,
                onTrendingTabClick = {},
            )


        }
    }
}