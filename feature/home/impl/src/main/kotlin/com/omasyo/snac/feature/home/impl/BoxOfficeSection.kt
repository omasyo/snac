package com.omasyo.snac.feature.home.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.modifiers.silhouette
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

data class BoxOfficeMovie(
    val name: String,
    val revenue: String,
)

@Composable
internal fun BoxOfficeSection(
    onMovieClick: () -> Unit,
    movies: List<BoxOfficeMovie>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .silhouette(SnacColors.accent, DpOffset(6.dp, 6.dp))
            .background(SnacColors.film)
            .padding(16.dp),
    ) {
        Row {
            Text(
                text = "BOX OFFICE",
                style = SnacTypography.title,
                color = SnacColors.accent,
                modifier = Modifier.alignByBaseline()
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Revenue",
                style = SnacTypography.hand,
                color = SnacColors.onFilm,
                modifier = Modifier.alignByBaseline()
            )
        }
        movies.forEachIndexed { index, movie ->
            Entry(
                position = index + 1,
                title = movie.name,
                revenue = movie.revenue,
                onClick = { onMovieClick() },
            )
        }
    }
}

@Composable
private fun Entry(
    position: Int,
    title: String,
    revenue: String,
    onClick: () -> Unit,
) {
    val borderColor = SnacColors.filmLine
    val indicationColor = SnacColors.accent
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4.dp.toPx(), 2.dp.toPx()))
                )
            }
            .clickable(
                onClick = onClick,
                indication = remember { TintIndication { indicationColor } },
                interactionSource = null,
            ),
    ) {
        val positionColor = when (position) {
            1 -> SnacColors.signal
            2, 3 -> SnacColors.onFilm
            else -> SnacColors.faint
        }
        Text(
            text = "$position",
            style = SnacTypography.title,
            color = positionColor,
            modifier = Modifier.widthIn(44.dp)
        )
        Text(
            text = title,
            style = SnacTypography.label,
            color = SnacColors.onFilm,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = revenue,
            style = SnacTypography.subtitle,
            color = SnacColors.accent,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        BoxOfficeSection(
            onMovieClick = {},
            movies = listOf(
                BoxOfficeMovie("Harmattan", "$21.7M"),
                BoxOfficeMovie("Orbital Diner", "$12.9M"),
                BoxOfficeMovie("Ghost Frequency", "$8.4"),
                BoxOfficeMovie("Velvet Hour", "$5.1M"),
                BoxOfficeMovie("Saltline", "$3.3M"),
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}