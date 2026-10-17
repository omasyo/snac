package com.omasyo.snac.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Button
import com.omasyo.snac.core.ui.modifiers.film
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

enum class TrendingTab {
    Movie, Show
}

@Composable
fun HomeScreen(
    onSignInClick: () -> Unit,
    onMovieClick: () -> Unit,
    onShowClick: () -> Unit,
    onAllChartsClick: () -> Unit,
    trendingTab: TrendingTab,
    onTrendingTabClick: (TrendingTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SnacColors.page)
            .padding(vertical = 24.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
        ) {
            BasicText(
                text = "Snac",
                style = SnacTypography.headline,
                modifier = Modifier
                    .rotate(-3f)
                    .silhouette(SnacColors.signal),
            )
            Button(
                text = "Sign in",
                onClick = onSignInClick,
            )
        }
        Box {
//            Box(Modifier.matchParentSize().background(Color.Gray))
            Box(
                modifier = Modifier
                    .film()
                    .background(SnacColors.film)
                    .heightIn(272.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Stamp("#1 trending", modifier = Modifier.rotate(-6f))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.align(
                        Alignment.BottomStart
                    )
                ) {
                    val titleColor = SnacColors.onFilm
                    BasicText(
                        text = "FRANKENSTEIN",
                        style = SnacTypography.display.copy(
                            lineHeightStyle = CapHeightLineHeightStyle,
                        ),
                        color = { titleColor },
                        modifier = Modifier.silhouette(SnacColors.signal)

                    )
                    val infoColor = SnacColors.accent
                    BasicText(
                        text = "1,204 watching now",
                        style = SnacTypography.hand,
                        color = { infoColor }

                    )
                }
            }
        }
    }
}

private val CapHeightLineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.Both,
    mode = LineHeightStyle.Mode.Tight,
)

@Preview
@Composable
private fun Preview() {
    SnacTheme {
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

@Composable
fun Stamp(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SnacColors.chip,
) {
    val contentColor = SnacColors.signal
    BasicText(
        text = text,
        style = SnacTypography.hand,
        color = { contentColor },
        modifier = modifier
            .border(4.dp, contentColor)
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 2.dp)
    )
}

@Preview
@Composable
private fun SPreview() {
    SnacTheme {
        Stamp("#1 trending")
    }
}