package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun RootScaffoldLayout(
    modifier: Modifier = Modifier,
    scrimColor: Color = SnacColors.page,
    content: @Composable () -> Unit,
) {
    var scrolled by rememberSaveable { mutableFloatStateOf(0f) }
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                scrolled = (scrolled - consumed.y).coerceAtLeast(0f)
                return Offset.Zero
            }
        }
    }

    val statusBarHeight = WindowInsets.statusBars.getTop(LocalDensity.current).toFloat()
    val progress by remember(statusBarHeight) {
        derivedStateOf {
            if (statusBarHeight <= 0f) 1f else (scrolled / statusBarHeight).coerceIn(0f, 1f)
        }
    }

    Box(modifier = modifier.nestedScroll(connection)) {
        content()
        Box(
            Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .silhouette(offset = DpOffset(x = 0.dp, y = 4.dp * progress))
                .graphicsLayer { translationY = scrolled.coerceAtMost(size.height) - size.height }
                .background(scrimColor)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        RootScaffoldLayout {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.background(SnacColors.page)
            ) {
                items(20) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color.Red)
                    )
                }
            }
        }
    }
}
