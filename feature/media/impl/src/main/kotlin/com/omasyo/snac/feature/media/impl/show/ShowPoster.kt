package com.omasyo.snac.feature.media.impl.show

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.media.impl.R

@Composable
internal fun ShowPoster(
    onClick: () -> Unit,
    state: UiState<String?>,
    modifier: Modifier = Modifier,
) {
    if (state is UiState.Error) return
    val interactionSource = remember { MutableInteractionSource() }
    val shadowColor = SnacColors.signalShadow
    Box(
        modifier = modifier
            .rotate(4f)
            .clickable(
                enabled = state is UiState.Success,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .indication(interactionSource, SilhouettePressIndication({ shadowColor }))
            .border(3.dp, SnacColors.chip)
            .size(56.dp, 84.dp),
    ) {
        when (state) {
            is UiState.Success -> Image(
                imageUrl = state.content,
                contentDescription = stringResource(R.string.show_poster_description),
                modifier = Modifier.matchParentSize(),
            )

            else -> Box(
                Modifier
                    .matchParentSize()
                    .background(imageGradient())
            )
        }
    }
}
