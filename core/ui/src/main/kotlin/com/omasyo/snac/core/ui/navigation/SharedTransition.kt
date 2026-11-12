package com.omasyo.snac.core.ui.navigation

import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.LocalNavAnimatedContentScope

val LocalNavSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

val NavBoundsTransform = BoundsTransform { _, _ -> spring(stiffness = Spring.StiffnessMediumLow) }

@Composable
fun Modifier.navSharedElement(
    key: Any,
    zIndexInOverlay: Float = 0f,
    boundsTransform: BoundsTransform = NavBoundsTransform,
): Modifier {
    val sharedTransitionScope = LocalNavSharedTransitionScope.current ?: return this
    val animatedContentScope = LocalNavAnimatedContentScope.current
    return with(sharedTransitionScope) {
        this@navSharedElement.sharedElement(
            sharedContentState = rememberSharedContentState(key),
            animatedVisibilityScope = animatedContentScope,
            boundsTransform = boundsTransform,
            zIndexInOverlay = zIndexInOverlay,
        )
    }
}

@Composable
fun Modifier.navSharedBounds(
    key: Any,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    resizeMode: ResizeMode = ResizeMode.RemeasureToBounds,
    zIndexInOverlay: Float = 0f,
    boundsTransform: BoundsTransform = NavBoundsTransform,
): Modifier {
    val sharedTransitionScope = LocalNavSharedTransitionScope.current ?: return this
    val animatedContentScope = LocalNavAnimatedContentScope.current
    return with(sharedTransitionScope) {
        this@navSharedBounds.sharedBounds(
            sharedContentState = rememberSharedContentState(key),
            animatedVisibilityScope = animatedContentScope,
            enter = enter,
            exit = exit,
            boundsTransform = boundsTransform,
            resizeMode = resizeMode,
            zIndexInOverlay = zIndexInOverlay,
        )
    }
}
