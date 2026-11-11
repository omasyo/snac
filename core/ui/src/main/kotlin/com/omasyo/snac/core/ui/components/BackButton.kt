package com.omasyo.snac.core.ui.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.omasyo.snac.core.ui.R
import com.omasyo.snac.core.ui.navigation.LocalNavSharedTransitionScope
import com.omasyo.snac.core.ui.theme.colors.SnacColors

private const val BackButtonKey = "nav-back-button"

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sharedTransitionScope = LocalNavSharedTransitionScope.current

    val sharedModifier = if (sharedTransitionScope == null) {
        Modifier
    } else {
        val animatedContentScope = LocalNavAnimatedContentScope.current
        with(sharedTransitionScope) {
            with(animatedContentScope) {
                Modifier
                    .animateEnterExit(
                        enter = fadeIn() + scaleIn(initialScale = 0.6f),
                        exit = fadeOut() + scaleOut(targetScale = 0.6f),
                        label = "BackButtonEnterExit",
                    )
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(BackButtonKey),
                        animatedVisibilityScope = animatedContentScope,
                        boundsTransform = { _, _ -> spring(stiffness = Spring.StiffnessMediumLow) },
                    )
            }
        }
    }

    IconButton(
        icon = R.drawable.caret_left_bold,
        contentDescription = stringResource(R.string.back),
        onClick = onClick,
        backgroundColor = SnacColors.chip,
        contentColor = SnacColors.ink,
        modifier = modifier
            .then(sharedModifier)
            .size(48.dp),
    )
}
