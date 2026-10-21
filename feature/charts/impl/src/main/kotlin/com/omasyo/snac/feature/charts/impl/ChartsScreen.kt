package com.omasyo.snac.feature.charts.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.TabRow
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.bottomBarPadding
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

enum class TrendingTab(val label: String) {
    Movie("Movie"), Show("Show")
}

@Composable
fun ChartsScreen(
    tab: TrendingTab,
    onTabChange: (TrendingTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
//        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(SnacColors.page)
            .bottomBarPadding()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            Box(Modifier.weight(1f)) {
                Text(
                    text = "CHARTS",
                    style = SnacTypography.headline,
                    modifier = Modifier
                        .rotate(-3f)
                        .silhouette(SnacColors.signal),
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                TabRow(
                    selectedTab = tab,
                    onSelectedTabChange = onTabChange,
                    name = { it.label },
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .clickable {}
                        .border(2.dp, SnacColors.ink)
                        .background(SnacColors.accent),
                )
            }
        }
        TestPreview()
    }
}

@Preview
@Composable
private fun Preview() {
    var trendingTab by remember { mutableStateOf(TrendingTab.Movie) }
    SnacTheme {
        ChartsScreen(
            tab = trendingTab,
            onTabChange = { trendingTab = it },
        )
    }
}