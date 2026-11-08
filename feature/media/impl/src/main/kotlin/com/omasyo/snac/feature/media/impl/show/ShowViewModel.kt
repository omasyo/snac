package com.omasyo.snac.feature.media.impl.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.data.models.ShowDetails
import com.omasyo.snac.core.data.show.ShowRepository
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.models.RefreshWrapper
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.data.models.Review
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@AssistedInject
class ShowViewModel(
    @Assisted private val slug: String,
    private val showRepository: ShowRepository,
) : ViewModel() {
    val show: StateFlow<UiState<ShowDetails>> =
        RefreshWrapper(viewModelScope) { showRepository.getDetails(slug) }.response

    val ratings: StateFlow<UiState<Ratings>> =
        RefreshWrapper(viewModelScope) { showRepository.getRatings(slug) }.response

    val seasons: StateFlow<UiState<List<Season>>> =
        RefreshWrapper(viewModelScope) { showRepository.getSeasons(slug) }.response

    val cast: StateFlow<UiState<List<Cast>>> =
        RefreshWrapper(viewModelScope) { showRepository.getCast(slug) }.response

    val review: StateFlow<UiState<List<Review>>> =
        RefreshWrapper(viewModelScope) { showRepository.getTopComments(slug) }.response

    val relatedShows: Flow<PagingData<Show>> =
        showRepository.getRelated(slug).cachedIn(viewModelScope)

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted slug: String): ShowViewModel
    }
}
