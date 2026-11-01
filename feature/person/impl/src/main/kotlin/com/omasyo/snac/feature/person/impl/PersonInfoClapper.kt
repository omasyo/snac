package com.omasyo.snac.feature.person.impl

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
import com.omasyo.snac.core.ui.components.ClapperboardStripes
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

internal data class PersonInfo(
    val label: String,
    val value: String,
    val detail: String?,
)

@Composable
internal fun PersonInfoClapper(
    info: List<PersonInfo>,
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
        Row(Modifier.height(IntrinsicSize.Min)) {
            info.forEach { (label, value, detail) ->
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

@Composable
internal fun PersonInfoClapperPlaceholder(
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
        Row(Modifier.height(IntrinsicSize.Min)) {
            repeat(3) {
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
                            .width(40.dp)
                            .height(10.dp)
                            .background(SnacColors.faint)
                    )
                    Box(
                        Modifier
                            .width(64.dp)
                            .height(20.dp)
                            .background(SnacColors.faint)
                    )
                    Box(
                        Modifier
                            .width(32.dp)
                            .height(12.dp)
                            .background(SnacColors.faint)
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
            PersonInfoClapper(
                info = listOf(
                    PersonInfo("Born", "Oct 4, 1956", "Age 70"),
                    PersonInfo("From", "Vienna", "Austria"),
                    PersonInfo("In lists", "42", "on Trakt"),
                )
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
            PersonInfoClapperPlaceholder()
        }
    }
}
