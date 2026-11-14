package com.omasyo.snac.feature.search.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omasyo.snac.core.data.search.SearchRepository
import com.omasyo.snac.core.model.media.TrendingSearchItem
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class SearchViewModel(
    searchRepository: SearchRepository,
) : ViewModel() {
    val trendingSearches: Flow<PagingData<TrendingSearchItem>> =
        searchRepository.getTrendingSearchResults().cachedIn(viewModelScope)
}
