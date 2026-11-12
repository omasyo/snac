package com.omasyo.snac

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.components.BottomBar
import com.omasyo.snac.core.ui.navigation.LocalNavSharedTransitionScope
import com.omasyo.snac.core.ui.navigation.Navigator
import com.omasyo.snac.core.ui.navigation.rememberNavigationState
import com.omasyo.snac.core.ui.navigation.toDecoratedEntries
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.calendar.impl.navigation.calendarEntry
import com.omasyo.snac.feature.charts.impl.navigation.chartsEntry
import com.omasyo.snac.feature.home.api.navigation.HomeNavKey
import com.omasyo.snac.feature.home.impl.navigation.homeEntry
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey
import com.omasyo.snac.feature.media.impl.navigation.mediaEntry
import com.omasyo.snac.feature.person.api.navigation.PersonNavKey
import com.omasyo.snac.feature.person.impl.navigation.personEntry
import com.omasyo.snac.feature.search.impl.navigation.searchEntry
import com.omasyo.snac.feature.settings.api.navigation.SettingsNavKey
import com.omasyo.snac.feature.settings.impl.navigation.settingsEntry
import com.omasyo.snac.navigation.TOP_LEVEL_NAV_ITEMS

private fun Media.navKey(): NavKey = when (this) {
    is Movie -> MovieNavKey(slug)
    is Show -> ShowNavKey(slug)
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {

    val navigationState = rememberNavigationState(
        startRoute = HomeNavKey,
        topLevelRoutes = TOP_LEVEL_NAV_ITEMS.keys
    )

    val navigator = remember { Navigator(navigationState) }

    val destination = navigationState.topLevelRoute

    val onMediaClick: (Media) -> Unit = { navigator.navigate(it.navKey()) }

    RootScreenLayout(
        modifier = Modifier.background(SnacColors.page),
        bottomBar = {
            BottomBar(
                currentDestination = destination,
                onCurrentDestinationChange = {
//                    backStack.clear()
                    navigator.navigate(it)
                },
                expanded = navigationState.currentRoute in TOP_LEVEL_NAV_ITEMS.keys,
                destinations = TOP_LEVEL_NAV_ITEMS.keys.toList(),
                label = { TOP_LEVEL_NAV_ITEMS.getValue(it).label },
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    ) {
        SharedTransitionLayout {
            CompositionLocalProvider(LocalNavSharedTransitionScope provides this) {
                NavDisplay(
                    sharedTransitionScope = this,
                    modifier = modifier,
                    onBack = { navigator.goBack() },
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
                    predictivePopTransitionSpec = { //TODO
                        fadeIn() + scaleIn(initialScale = 0.9f) togetherWith slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            spring(stiffness = Spring.StiffnessMediumLow)
                        )
                    },
                    entries = navigationState.toDecoratedEntries(
                        entryProvider {
                            calendarEntry()
                            chartsEntry(onMediaClick = onMediaClick)
                            homeEntry(
                                onMediaClick = onMediaClick,
                                onSettingsClick = { navigator.navigate(SettingsNavKey) },
                            )
                            mediaEntry(
                                onBackClick = { navigator.goBack() },
                                onMediaClick = onMediaClick,
                                onSeasonClick = { navigator.navigate(it) },
                                onEpisodeClick = { navigator.navigate(it) },
                                onPeopleClick = { navigator.navigate(it) },
                                onPersonClick = { navigator.navigate(PersonNavKey(it.slug)) },
                                onCommentsClick = { navigator.navigate(it) },
                                onCommentThreadClick = { navigator.navigate(it) },
                            )
                            personEntry(
                                onBackClick = { navigator.goBack() },
                                onMediaClick = onMediaClick,
                            )
                            searchEntry()
                            settingsEntry(onBackClick = { navigator.goBack() })
                        },
                    ),
                )
            }
        }

    }


}
