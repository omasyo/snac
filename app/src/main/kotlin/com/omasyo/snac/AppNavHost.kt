package com.omasyo.snac

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.omasyo.snac.feature.auth.Auth
import com.omasyo.snac.feature.auth.AuthScreen
import com.omasyo.snac.feature.settings.Settings
import com.omasyo.snac.feature.settings.SettingsScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Auth::class, Auth.serializer())
            subclass(Settings::class, Settings.serializer())
        }
    }
}

@Composable
fun AppNavHost(
    isAuthenticated: Boolean,
    modifier: Modifier = Modifier,
) {

    val startRoute: NavKey = if (isAuthenticated) Settings else Auth

    val backStack = rememberNavBackStack(config, startRoute)


    LaunchedEffect(isAuthenticated) {
        backStack.clear()
        backStack.add(startRoute)
    }


    NavDisplay(
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
        ),
        backStack = backStack,
        transitionSpec = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                spring(stiffness = Spring.StiffnessMediumLow)
            ) togetherWith fadeOut() + scaleOut(targetScale = 0.9f)
        },
        popTransitionSpec = {
            fadeIn() + scaleIn(initialScale = 0.9f) togetherWith slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                spring(stiffness = Spring.StiffnessMediumLow)
            )
        },
        entryProvider = entryProvider {
            entry<Auth> {
                AuthScreen()
            }
            entry<Settings> {
                SettingsScreen()
            }
        },
    )
}