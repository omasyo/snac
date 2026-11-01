package com.omasyo.snac

import androidx.compose.animation.AnimatedContentTransitionScope
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.components.BottomBar
import com.omasyo.snac.core.ui.theme.colors.SnacColors
import com.omasyo.snac.feature.calendar.api.navigation.CalendarNavKey
import com.omasyo.snac.feature.calendar.impl.navigation.calendarEntry
import com.omasyo.snac.feature.charts.api.navigation.ChartsNavKey
import com.omasyo.snac.feature.charts.impl.navigation.chartsEntry
import com.omasyo.snac.feature.home.api.navigation.HomeNavKey
import com.omasyo.snac.feature.home.impl.navigation.homeEntry
import com.omasyo.snac.feature.media.api.navigation.EpisodeNavKey
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.SeasonNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey
import com.omasyo.snac.feature.media.impl.navigation.mediaEntry
import com.omasyo.snac.feature.person.api.navigation.PersonNavKey
import com.omasyo.snac.feature.person.impl.navigation.personEntry
import com.omasyo.snac.feature.search.api.navigation.SearchNavKey
import com.omasyo.snac.feature.search.impl.navigation.searchEntry
import com.omasyo.snac.feature.settings.api.navigation.SettingsNavKey
import com.omasyo.snac.feature.settings.impl.navigation.settingsEntry
import com.omasyo.snac.navigation.TOP_LEVEL_NAV_ITEMS
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(CalendarNavKey::class, CalendarNavKey.serializer())
            subclass(ChartsNavKey::class, ChartsNavKey.serializer())
            subclass(EpisodeNavKey::class, EpisodeNavKey.serializer())
            subclass(HomeNavKey::class, HomeNavKey.serializer())
            subclass(MovieNavKey::class, MovieNavKey.serializer())
            subclass(PersonNavKey::class, PersonNavKey.serializer())
            subclass(SearchNavKey::class, SearchNavKey.serializer())
            subclass(SeasonNavKey::class, SeasonNavKey.serializer())
            subclass(SettingsNavKey::class, SettingsNavKey.serializer())
            subclass(ShowNavKey::class, ShowNavKey.serializer())
        }
    }
}

private fun Media.navKey(): NavKey = when (this) {
    is Movie -> MovieNavKey(slug)
    is Show -> ShowNavKey(slug)
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(config, HomeNavKey)

    val destination = backStack.last { it in TOP_LEVEL_NAV_ITEMS }

    val onMediaClick: (Media) -> Unit = { backStack.add(it.navKey()) }

    RootScreenLayout(
        modifier = Modifier.background(SnacColors.page),
        bottomBar = {
            BottomBar(
                currentDestination = destination,
                onCurrentDestinationChange = {
//                    backStack.clear()
                    backStack.add(it)
                },
                destinations = TOP_LEVEL_NAV_ITEMS.keys.toList(),
                label = { TOP_LEVEL_NAV_ITEMS.getValue(it).label },
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    ) {
        NavDisplay(
            modifier = modifier,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
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
                calendarEntry()
                chartsEntry(onMediaClick = onMediaClick)
                homeEntry(
                    onMediaClick = onMediaClick,
                    onSettingsClick = { backStack.add(SettingsNavKey) },
                )
                mediaEntry(
                    onBackClick = { backStack.removeLastOrNull() },
                    onMediaClick = onMediaClick,
                    onSeasonClick = { backStack.add(it) },
                    onEpisodeClick = { backStack.add(it) },
                    onPersonClick = { backStack.add(PersonNavKey(it.slug)) },
                )
                personEntry(
                    onBackClick = { backStack.removeLastOrNull() },
                    onMediaClick = onMediaClick,
                )
                searchEntry()
                settingsEntry(onBackClick = { backStack.removeLastOrNull() })
            },
        )

    }


}