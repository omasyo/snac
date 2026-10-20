package com.omasyo.snac.core.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun Image(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    alignment: Alignment = Alignment.Center,
    placeholder: Painter = BrushPainter(
        Brush.linearGradient(
            listOf(
                SnacColors.film,
                SnacColors.filmLine,
                SnacColors.faint,
            ), end = Offset(0f, Float.POSITIVE_INFINITY)
        )
    ),
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(imageUrl)
            .crossfade(true).build(),
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        alignment = alignment,
        contentScale = contentScale,
    )
}