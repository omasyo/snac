package com.omasyo.snac.feature.media.impl.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.models.PeopleList
import com.omasyo.snac.core.data.models.Season
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
class SeasonPeopleViewModel(
    @Assisted private val showSlug: String,
    @Assisted private val seasonNumber: Int,
    private val showRepository: ShowRepository,
) : ViewModel() {
    val show: StateFlow<UiState<ShowDetails>> =
        RefreshWrapper(viewModelScope) { showRepository.getDetails(showSlug) }.response

    val season: StateFlow<UiState<Season>> =
        RefreshWrapper(viewModelScope) { showRepository.getSeason(showSlug, seasonNumber) }.response

    val people: StateFlow<UiState<PeopleList>> =
        RefreshWrapper(viewModelScope) {
            showRepository.getSeasonPeople(showSlug, seasonNumber)
        }.response

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted showSlug: String, @Assisted seasonNumber: Int): SeasonPeopleViewModel
    }
}
