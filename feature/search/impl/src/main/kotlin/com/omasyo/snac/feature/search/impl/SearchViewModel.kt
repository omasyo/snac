package com.omasyo.snac.feature.search.impl

import androidx.compose.foundation.text.input.TextFieldState
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class SearchViewModel(
    private val searchRepository: SearchRepository,
) : ViewModel() {
    val trendingSearches: Flow<PagingData<TrendingSearchItem>> =
        searchRepository.getTrendingSearchResults().cachedIn(viewModelScope)

    val searchState = TextFieldState()

    val query: StateFlow<String> field = MutableStateFlow("")

    val movies: Flow<PagingData<Movie>> = searchResults(searchRepository::searchMovies)

    val shows: Flow<PagingData<Show>> = searchResults(searchRepository::searchShows)

    val people: Flow<PagingData<Person>> = searchResults(searchRepository::searchPeople)

    fun search() {
        this.query.update { searchState.text.trim().toString() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T : Any> searchResults(
        search: (String) -> Flow<PagingData<T>>,
    ): Flow<PagingData<T>> = query
        .filter(String::isNotBlank)
        .flatMapLatest {
            if (it.isBlank()) flowOf(PagingData.empty()) else search(it)
        }
        .cachedIn(viewModelScope)

}
