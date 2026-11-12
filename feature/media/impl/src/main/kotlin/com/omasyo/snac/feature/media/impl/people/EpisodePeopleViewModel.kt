package com.omasyo.snac.feature.media.impl.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.PeopleList
import com.omasyo.snac.core.data.models.ShowDetails
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
class EpisodePeopleViewModel(
    @Assisted private val showSlug: String,
    @Assisted private val seasonNumber: Int,
    @Assisted private val episodeNumber: Int,
    private val showRepository: ShowRepository,
) : ViewModel() {
    val show: StateFlow<UiState<ShowDetails>> =
        RefreshWrapper(viewModelScope) { showRepository.getDetails(showSlug) }.response

    val episode: StateFlow<UiState<EpisodeDetails>> =
        RefreshWrapper(viewModelScope) {
            showRepository.getEpisode(showSlug, seasonNumber, episodeNumber)
        }.response

    val people: StateFlow<UiState<PeopleList>> =
        RefreshWrapper(viewModelScope) {
            showRepository.getEpisodePeople(showSlug, seasonNumber, episodeNumber)
        }.response

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(
            @Assisted showSlug: String,
            @Assisted seasonNumber: Int,
            @Assisted episodeNumber: Int,
        ): EpisodePeopleViewModel
    }
}
