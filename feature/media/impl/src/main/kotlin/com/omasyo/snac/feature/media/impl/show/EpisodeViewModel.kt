package com.omasyo.snac.feature.media.impl.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.show.ShowRepository
import com.omasyo.snac.core.ui.models.RefreshWrapper
import com.omasyo.snac.core.ui.models.UiState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.StateFlow

@AssistedInject
class EpisodeViewModel(
    @Assisted private val showSlug: String,
    @Assisted private val seasonNumber: Int,
    @Assisted private val episodeNumber: Int,
    private val showRepository: ShowRepository,
) : ViewModel() {
    val showTitle: StateFlow<UiState<String>> =
        RefreshWrapper(viewModelScope) {
            showRepository.getDetails(showSlug).map { it.title }
        }.response

    val episode: StateFlow<UiState<EpisodeDetails>> =
        RefreshWrapper(viewModelScope) {
            showRepository.getEpisode(showSlug, seasonNumber, episodeNumber)
        }.response

    val ratings: StateFlow<UiState<Ratings>> =
        RefreshWrapper(viewModelScope) {
            showRepository.getEpisodeRatings(showSlug, seasonNumber, episodeNumber)
        }.response

    val cast: StateFlow<UiState<List<Cast>>> =
        RefreshWrapper(viewModelScope) {
            showRepository.getEpisodeCast(showSlug, seasonNumber, episodeNumber)
        }.response

    val comment: StateFlow<UiState<List<Comment>>> =
        RefreshWrapper(viewModelScope) {
            showRepository.getEpisodeTopComments(showSlug, seasonNumber, episodeNumber)
        }.response

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(
            @Assisted showSlug: String,
            @Assisted seasonNumber: Int,
            @Assisted episodeNumber: Int,
        ): EpisodeViewModel
    }
}
