package com.omasyo.snac.feature.search.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.snac.core.model.media.Media
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.navigation.LocalSharedViewModelStoreOwner
import com.omasyo.snac.core.navigation.Navigator
import com.omasyo.snac.core.navigation.SharedViewModelStoreNavEntryDecorator
import com.omasyo.snac.feature.media.api.navigation.MovieNavKey
import com.omasyo.snac.feature.media.api.navigation.ShowNavKey
import com.omasyo.snac.feature.person.api.navigation.PersonNavKey
import com.omasyo.snac.feature.search.api.navigation.SearchMoviesNavKey
import com.omasyo.snac.feature.search.api.navigation.SearchNavKey
import com.omasyo.snac.feature.search.api.navigation.SearchPeopleNavKey
import com.omasyo.snac.feature.search.api.navigation.SearchShowsNavKey
import com.omasyo.snac.feature.search.impl.SearchResultSectionScreen
import com.omasyo.snac.feature.search.impl.SearchScreen
import com.omasyo.snac.feature.search.impl.SearchViewModel
import com.omasyo.snac.feature.search.impl.components.MediaResultCard
import com.omasyo.snac.feature.search.impl.components.PersonResultCard
import dev.zacsweers.metrox.viewmodel.metroViewModel

private const val SEARCH_CONTENT_KEY = "SearchNavKey"

private val searchParentMetadata = SharedViewModelStoreNavEntryDecorator.parent(SEARCH_CONTENT_KEY)

fun EntryProviderScope<NavKey>.searchEntry(navigator: Navigator) {
    val onBackClick: () -> Unit = navigator::goBack
    val onMediaClick: (Media) -> Unit = {
        when (it) {
            is Movie -> navigator.navigate(MovieNavKey(it.slug))
            is Show -> navigator.navigate(ShowNavKey(it.slug))
        }
    }

    entry<SearchNavKey>(clazzContentKey = { SEARCH_CONTENT_KEY }) {
        SearchScreen(
            onMediaClick = onMediaClick,
            onPersonClick = { navigator.navigate(PersonNavKey(it.slug)) },
            onSeeAllMoviesClick = { navigator.navigate(SearchMoviesNavKey) },
            onSeeAllShowsClick = { navigator.navigate(SearchShowsNavKey) },
            onSeeAllPeopleClick = { navigator.navigate(SearchPeopleNavKey) },
        )
    }

    entry<SearchMoviesNavKey>(metadata = searchParentMetadata) {
        val viewModel: SearchViewModel = metroViewModel(LocalSharedViewModelStoreOwner.current)
        SearchResultSectionScreen(
            title = "Movies",
            onBackClick = onBackClick,
            items = viewModel.movies.collectAsLazyPagingItems(),
        ) {
            MediaResultCard(media = it, onClick = { onMediaClick(it) })
        }
    }

    entry<SearchShowsNavKey>(metadata = searchParentMetadata) {
        val viewModel: SearchViewModel = metroViewModel(LocalSharedViewModelStoreOwner.current)
        SearchResultSectionScreen(
            title = "Shows",
            onBackClick = onBackClick,
            items = viewModel.shows.collectAsLazyPagingItems(),
        ) {
            MediaResultCard(media = it, onClick = { onMediaClick(it) })
        }
    }

    entry<SearchPeopleNavKey>(metadata = searchParentMetadata) {
        val viewModel: SearchViewModel = metroViewModel(LocalSharedViewModelStoreOwner.current)
        SearchResultSectionScreen(
            title = "People",
            onBackClick = onBackClick,
            items = viewModel.people.collectAsLazyPagingItems(),
        ) {
            PersonResultCard(
                person = it,
                onClick = { navigator.navigate(PersonNavKey(it.slug)) },
            )
        }
    }
}
