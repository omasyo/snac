package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

data class ClapperInfo(
    val label: String,
    val value: String,
    val detail: String? = null,
)

@Composable
fun InfoClapper(
    info: List<ClapperInfo>,
    modifier: Modifier = Modifier,
    maxItemsInEachRow: Int = 4,
) {
    Column(
        modifier = modifier
            .silhouette(SnacColors.signalShadow, offset = DpOffset(6.dp, 6.dp))
            .border(4.dp, SnacColors.outline)
            .background(SnacColors.film)
    ) {
        ClapperboardStripes(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
        )
        info.chunked(maxItemsInEachRow).forEach { row ->
            Row(Modifier.height(IntrinsicSize.Min)) {
                row.forEach { (label, value, detail) ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .border(0.5.dp, SnacColors.filmLine)
                            .padding(8.dp),
                    ) {
                        Text(
                            text = label,
                            style = SnacTypography.overline,
                            color = SnacColors.mutedOnFilm,
                        )
                        Text(
                            text = value,
                            style = SnacTypography.subtitle,
                            color = SnacColors.onFilm,
                        )
                        if (detail != null) {
                            Text(
                                text = detail,
                                style = SnacTypography.caption,
                                color = SnacColors.mutedOnFilm,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoClapperPlaceholder(
    modifier: Modifier = Modifier,
    itemCount: Int = 8,
    maxItemsInEachRow: Int = 4,
    showDetail: Boolean = false,
) {
    Column(
        modifier = modifier
            .silhouette(SnacColors.signalShadow, offset = DpOffset(6.dp, 6.dp))
            .border(4.dp, SnacColors.outline)
            .background(SnacColors.film)
    ) {
        ClapperboardStripes(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
        )
        List(itemCount) { it }.chunked(maxItemsInEachRow).forEach { row ->
            Row(Modifier.height(IntrinsicSize.Min)) {
                row.forEach {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .border(0.5.dp, SnacColors.filmLine)
                            .padding(8.dp),
                    ) {
                        Box(
                            Modifier
                                .padding(vertical = 8.dp)
                                .width(40.dp)
                                .height(10.dp)
                                .background(SnacColors.placeholder)
                        )
                        Box(
                            Modifier
                                .width(64.dp)
                                .height(16.dp)
                                .background(SnacColors.placeholder)
                        )
                        if (showDetail) {
                            Box(
                                Modifier
                                    .width(32.dp)
                                    .height(12.dp)
                                    .background(SnacColors.placeholder)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp)
        ) {
            InfoClapper(
                info = listOf(
                    ClapperInfo("Year", "2025"),
                    ClapperInfo("Runtime", "2h 29m"),
                    ClapperInfo("Cert", "R"),
                    ClapperInfo("Status", "Released"),
                    ClapperInfo("Released", "17 Oct"),
                    ClapperInfo("Country", "us"),
                    ClapperInfo("Language", "en"),
                )
            )
        }
    }
}

@Preview
@Composable
private fun DetailPreview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp)
        ) {
            InfoClapper(
                info = listOf(
                    ClapperInfo("Born", "Oct 4, 1956", "Age 70"),
                    ClapperInfo("From", "Vienna", "Austria"),
                    ClapperInfo("In lists", "42", "on Trakt"),
                ),
                maxItemsInEachRow = 3,
            )
        }
    }
}

@Preview
@Composable
private fun PlaceholderPreview() {
    SnacTheme {
        Box(
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp)
        ) {
            InfoClapperPlaceholder()
        }
    }
}
