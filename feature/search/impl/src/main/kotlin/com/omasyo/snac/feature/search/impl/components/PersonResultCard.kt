package com.omasyo.snac.feature.search.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.modifiers.SilhouettePressIndication
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.random.Random

@Composable
internal fun PersonResultCard(
    person: Person,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val rotation =
        remember(person) { Random(person.slug.hashCode()).nextInt(-3, 3).toFloat() }

    Column(
        modifier = modifier
            .rotate(rotation)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .width(112.dp),
    ) {
        Image(
            imageUrl = person.imageUrl,
            modifier = Modifier
                .indication(interactionSource, SilhouettePressIndication())
                .border(2.dp, SnacColors.outline)
                .background(SnacColors.card)
                .padding(6.dp)
                .fillMaxWidth()
                .height(160.dp),
        )
        val tintColor = SnacColors.page
        Text(
            text = person.name,
            style = SnacTypography.tag,
            maxLines = 2,
            minLines = 2,
            modifier = Modifier
                .indication(
                    interactionSource, TintIndication(
                        contentOnly = true,
                        focusOpacity = 0.2f,
                        pressOpacity = 0.5f,
                    ) { tintColor })
        )
    }
}

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        MediaResultCard(
            media = Movie.PreviewSample.first(),
            onClick = {},
            modifier = Modifier
                .background(SnacColors.page)
                .padding(16.dp),
        )
    }
}