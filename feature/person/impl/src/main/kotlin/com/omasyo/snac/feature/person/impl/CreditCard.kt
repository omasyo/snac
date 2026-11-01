package com.omasyo.snac.feature.person.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.snac.core.data.models.CreditRole
import com.omasyo.snac.core.data.models.PersonCredit
import com.omasyo.snac.core.ui.components.Image
import com.omasyo.snac.core.ui.components.Stamp
import com.omasyo.snac.core.ui.components.Text
import com.omasyo.snac.core.ui.components.imageGradient
import com.omasyo.snac.core.ui.format.metaLine
import com.omasyo.snac.core.ui.modifiers.TintIndication
import com.omasyo.snac.core.ui.theme.SnacTheme
import com.omasyo.snac.core.ui.theme.SnacTypography
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import kotlin.random.Random

@Composable
internal fun CreditCard(
    credit: PersonCredit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = SnacColors.filmLine
    val indicationColor = SnacColors.accent
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 32.dp, end = 8.dp)
            .drawBehind {
                drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height))
            }
            .clickable(
                onClick = onClick,
                indication = remember { TintIndication { indicationColor } },
                interactionSource = null,
            )
            .padding(end = 8.dp)
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = credit.media.year.toString(),
            style = SnacTypography.title.copy(textAlign = TextAlign.End),
            color = SnacColors.faint,
            modifier = Modifier.widthIn(YearWidth)
        )

        Image(
            imageUrl = credit.media.imageUrl,
            modifier = Modifier
                .rotate(remember(credit.media.slug) {
                    Random(credit.media.slug.hashCode()).nextInt(
                        -3, 3
                    ).toFloat()
                })
                .border(2.dp, SnacColors.chip)
                .background(SnacColors.muted)
                .size(56.dp, 80.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = credit.media.title.uppercase(),
                style = SnacTypography.subtitle,
                color = SnacColors.onFilm,
            )
            Text(
                text = credit.info(),
                style = SnacTypography.hand,
                color = SnacColors.accent,
            )
            if (credit.isRegular) {
                Stamp(
                    text = stringResource(R.string.credit_regular),
                    backgroundColor = Color.Transparent,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .rotate(-3f),
                )
            }
        }
    }
}

@Composable
private fun PersonCredit.info(): String = metaLine(
    when (role) {
        CreditRole.Acting -> stringResource(R.string.credit_as, description)
        CreditRole.Crew -> description
    },
    episodeCount?.let { pluralStringResource(R.plurals.credit_episodes, it, it) },
)

@Composable
internal fun CreditCardPlaceholder(
    modifier: Modifier = Modifier,
) {
    val borderColor = SnacColors.filmLine
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 32.dp, end = 8.dp)
            .drawBehind {
                drawLine(borderColor, Offset(0f, size.height), Offset(size.width, size.height))
            }
            .padding(end = 8.dp)
            .padding(vertical = 8.dp),
    ) {
        Box(
            Modifier
                .size(YearWidth, 28.dp)
                .background(SnacColors.faint)
        )
        Box(
            modifier = Modifier
                .rotate(-2f)
                .border(2.dp, SnacColors.chip)
                .background(imageGradient())
                .size(56.dp, 80.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            Box(
                Modifier
                    .width(160.dp)
                    .height(18.dp)
                    .background(SnacColors.faint)
            )
            Box(
                Modifier
                    .width(104.dp)
                    .height(14.dp)
                    .background(SnacColors.faint)
            )
        }
    }
}

private val YearWidth = 56.dp

@Preview
@Composable
private fun Preview() {
    SnacTheme {
        CreditCard(
            credit = PersonCredit.PreviewSample[1],
            onClick = {},
            modifier = Modifier.background(SnacColors.film),
        )
    }
}

@Preview
@Composable
private fun PlaceholderPreview() {
    SnacTheme {
        CreditCardPlaceholder(Modifier.background(SnacColors.film))
    }
}
