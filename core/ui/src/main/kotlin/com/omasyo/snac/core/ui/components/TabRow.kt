package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.colors.SnacColors


@Composable
inline fun <reified Tab : Enum<Tab>> TabRow(
    selectedTab: Tab,
    noinline onSelectedTabChange: (Tab) -> Unit,
    modifier: Modifier = Modifier,
    equalWidth: Boolean = false,
    noinline name: (Tab) -> String = { it.name },
) {
    val tabs = enumValues<Tab>().toList()
    TabRow(
        selectedTab = selectedTab,
        onSelectedTabChange = onSelectedTabChange,
        tabs = tabs,
        name = name,
        modifier = modifier,
        equalWidth = equalWidth,
    )
}

@Composable
fun <Tab> TabRow(
    selectedTab: Tab,
    onSelectedTabChange: (Tab) -> Unit,
    tabs: List<Tab>,
    name: (Tab) -> String,
    modifier: Modifier = Modifier,
    equalWidth: Boolean = false,
) {
    Row(
        modifier = modifier
            .silhouette()
            .border(2.dp, SnacColors.ink)
            .selectableGroup()
    ) {
        for (tab in tabs) {
            val selected = tab == selectedTab
            val indicationColor = SnacColors.accent
            Text(
                text = name(tab),
                color = if (selected) SnacColors.accent else SnacColors.ink,
                style = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier
                    .then(if (equalWidth) Modifier.weight(1f) else Modifier)
                    .background(if (selected) SnacColors.film else SnacColors.chip)
                    .selectable(
                        selected = selected,
                        interactionSource = null,
                        indication = TintIndication { indicationColor },
                        role = Role.Tab,
                        onClick = { onSelectedTabChange(tab) }
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

private enum class TrendingTab(val label: String) {
    Movie("Movie"), Show("Show")
}

@Preview
@Composable
private fun Preview() {
    var trendingTab by remember { mutableStateOf(TrendingTab.Movie) }
    SnacTheme {
        Box(Modifier.background(SnacColors.page)) {
            TabRow(
                selectedTab = trendingTab,
                onSelectedTabChange = { trendingTab = it },
                name = { it.label },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}