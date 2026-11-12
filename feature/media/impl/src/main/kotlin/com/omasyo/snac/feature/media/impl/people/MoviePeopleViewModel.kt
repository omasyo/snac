package com.omasyo.snac.feature.media.impl.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.models.MovieDetails
import com.omasyo.snac.core.data.models.PeopleList
import com.omasyo.snac.core.data.movie.MovieRepository
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
class MoviePeopleViewModel(
    @Assisted private val slug: String,
    private val movieRepository: MovieRepository,
) : ViewModel() {
    val movie: StateFlow<UiState<MovieDetails>> =
        RefreshWrapper(viewModelScope) { movieRepository.getDetails(slug) }.response

    val people: StateFlow<UiState<PeopleList>> =
        RefreshWrapper(viewModelScope) { movieRepository.getPeople(slug) }.response

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted slug: String): MoviePeopleViewModel
    }
}
