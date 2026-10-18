package com.omasyo.snac

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.omasyo.snac.core.ui.components.bottombar.BottomBar
import com.omasyo.snac.core.ui.components.navigation.TopLevelRoute
import com.omasyo.snac.feature.auth.Auth
import com.omasyo.snac.feature.calendar.CalendarRoute
import com.omasyo.snac.feature.calendar.calendarNavigation
import com.omasyo.snac.feature.charts.ChartsRoute
import com.omasyo.snac.feature.charts.chartsNavigation
import com.omasyo.snac.feature.home.HomeRoute
import com.omasyo.snac.feature.home.homeNavigation
import com.omasyo.snac.feature.search.SearchRoute
import com.omasyo.snac.feature.search.searchNavigation
import com.omasyo.snac.feature.settings.Settings
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Auth::class, Auth.serializer())
            subclass(CalendarRoute::class, CalendarRoute.serializer())
            subclass(ChartsRoute::class, ChartsRoute.serializer())
            subclass(HomeRoute::class, HomeRoute.serializer())
            subclass(Settings::class, Settings.serializer())
            subclass(SearchRoute::class, SearchRoute.serializer())
        }
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(config, HomeRoute)

    val destination = backStack.last { it is TopLevelRoute } as TopLevelRoute

    Test(
        bottomBar = {
            BottomBar(
                currentDestination = destination,
                onCurrentDestinationChange = {
//                    backStack.clear()
                    backStack.add(it)
                },
                destinations = listOf(HomeRoute, ChartsRoute, SearchRoute, CalendarRoute),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    ) {
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
                calendarNavigation()
                chartsNavigation()
                homeNavigation()
                searchNavigation()
            },
        )

    }


}