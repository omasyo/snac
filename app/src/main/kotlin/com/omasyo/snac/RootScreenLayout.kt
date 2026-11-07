package com.omasyo.snac

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.omasyo.snac.core.ui.components.BottomBar
import com.omasyo.snac.core.ui.components.LocalBottomBarPadding
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun RootScreenLayout(
    bottomBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    SubcomposeLayout(modifier) { constraints ->
        val bottomBarPlaceables = subcompose("bottomBar", bottomBar).map { it.measure(constraints) }

        val layoutHeight = constraints.maxHeight
        val bottomBarHeight = bottomBarPlaceables.maxOfOrNull { it.height } ?: 0

        val contentPlaceable =
            subcompose("content") {
                CompositionLocalProvider(LocalBottomBarPadding provides PaddingValues(bottom = bottomBarHeight.toDp())) {
                    content()
                }
            }.map { it.measure(constraints) }


        layout(constraints.maxWidth, constraints.maxHeight) {
            contentPlaceable.forEach { it.placeRelative(0, 0) }
            bottomBarPlaceables.forEach { it.placeRelative(0, layoutHeight - bottomBarHeight) }
        }
    }
}

private enum class TestDestination(val label: String) {
    Home("Home"),
    Charts("Charts"),
    Search("Search"),
    Calendar("Calendar"),
    Me("Me"),
}

@Preview
@Composable
private fun Preview() {
    var destination by remember { mutableStateOf(TestDestination.Home) }
    var expanded by remember { mutableStateOf(true) }

    SnacTheme {
        RootScreenLayout(
            bottomBar = {
                BottomBar(
                    currentDestination = destination,
                    onCurrentDestinationChange = { destination = it },
                    expanded = expanded,
                    destinations = TestDestination.entries,
                    label = { it.label },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )
            }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(SnacColors.page)
                    .clickable { expanded = !expanded }
            )
        }
    }
}