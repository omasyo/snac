package com.omasyo.snac.feature.search.impl

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omasyo.snac.core.data.models.Person
import com.omasyo.snac.core.data.search.SearchRepository
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.TrendingSearchItem
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class SearchViewModel(
    private val searchRepository: SearchRepository,
) : ViewModel() {
    val trendingSearches: Flow<PagingData<TrendingSearchItem>> =
        searchRepository.getTrendingSearchResults().cachedIn(viewModelScope)

    val searchState = TextFieldState()

    @OptIn(FlowPreview::class)
    val query: StateFlow<String> = snapshotFlow { searchState.text.toString().trim() }
        .debounce(500.milliseconds)
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val movies: Flow<PagingData<Movie>> = searchResults(searchRepository::searchMovies)

    val shows: Flow<PagingData<Show>> = searchResults(searchRepository::searchShows)

    val people: Flow<PagingData<Person>> = searchResults(searchRepository::searchPeople)

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T : Any> searchResults(
        search: (String) -> Flow<PagingData<T>>,
    ): Flow<PagingData<T>> = query
        .filter(String::isNotBlank)
        .flatMapLatest(search)
        .cachedIn(viewModelScope)

}
