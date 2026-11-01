package com.omasyo.snac.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.ui.R
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors

@Composable
fun ArrowLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tintColor = SnacColors.page
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(
            interactionSource = null,
            indication = TintIndication(
                contentOnly = true,
                focusOpacity = 0.2f,
                pressOpacity = 0.5f
            ) { tintColor },
            onClick = onClick,
        ),
    ) {
        Text(
            text = text,
            style = SnacTypography.label,
            color = SnacColors.signal,
        )
        Icon(
            icon = R.drawable.caret_right_bold,
            contentDescription = null,
            tint = SnacColors.signal,
            modifier = Modifier.size(14.dp),
        )
    }
}
