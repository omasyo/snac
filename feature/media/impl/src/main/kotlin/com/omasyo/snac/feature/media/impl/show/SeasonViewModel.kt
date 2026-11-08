package com.omasyo.snac.feature.media.impl.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.data.show.ShowRepository
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
import kotlinx.coroutines.flow.StateFlow

@AssistedInject
class SeasonViewModel(
    @Assisted private val showSlug: String,
    @Assisted private val seasonNumber: Int,
    private val showRepository: ShowRepository,
) : ViewModel() {
    val showTitle: StateFlow<UiState<String>> =
        RefreshWrapper(viewModelScope) { showRepository.getDetails(showSlug).map { it.title } }.response

    val season: StateFlow<UiState<Season>> =
        RefreshWrapper(viewModelScope) { showRepository.getSeason(showSlug, seasonNumber) }.response

    val ratings: StateFlow<UiState<Ratings>> =
        RefreshWrapper(viewModelScope) { showRepository.getSeasonRatings(showSlug, seasonNumber) }.response

    val episodes: StateFlow<UiState<List<EpisodeDetails>>> =
        RefreshWrapper(viewModelScope) { showRepository.getEpisodes(showSlug, seasonNumber) }.response

    val cast: StateFlow<UiState<List<Cast>>> =
        RefreshWrapper(viewModelScope) { showRepository.getSeasonCast(showSlug, seasonNumber) }.response

    val review: StateFlow<UiState<List<Review>>> =
        RefreshWrapper(viewModelScope) {
            showRepository.getSeasonTopComments(showSlug, seasonNumber)
        }.response

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted showSlug: String, @Assisted seasonNumber: Int): SeasonViewModel
    }
}
