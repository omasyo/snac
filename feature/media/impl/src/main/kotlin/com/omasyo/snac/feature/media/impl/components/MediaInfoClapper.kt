package com.omasyo.snac.feature.media.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.ClapperboardStripes
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MediaInfoClapper(
    info: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .silhouette(SnacColors.signal, offset = DpOffset(6.dp, 6.dp))
            .border(4.dp, SnacColors.ink)
            .background(SnacColors.film)
    ) {
        ClapperboardStripes(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth()
        )
        FlowRow(
            maxItemsInEachRow = 4,
        ) {
            info.forEach { (key, value) ->
                Column(
                    modifier = Modifier
                        .widthIn(80.dp)
                        .weight(1f)
                        .fillMaxRowHeight()
                        .border(0.5.dp, SnacColors.filmLine)
                        .padding(8.dp),
                ) {
                    Text(
                        text = key,
                        style = SnacTypography.overline,
                        color = SnacColors.mutedOnFilm,
                    )
                    Text(
                        text = value,
                        style = SnacTypography.subtitle,
                        color = SnacColors.onFilm,
                    )
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
            MediaInfoClapper(
                info = listOf(
                    "Year" to "2025",
                    "Runtime" to "2h 29m",
                    "Cert" to "R",
                    "Status" to "Released",
                    "Released" to "17 Oct",
                    "Country" to "us",
                    "Language" to "en",
                )
            )
        }
    }
}