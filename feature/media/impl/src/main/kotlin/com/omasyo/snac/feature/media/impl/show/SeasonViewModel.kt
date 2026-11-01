package com.omasyo.snac.feature.media.impl.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.ui.models.RefreshWrapper
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.mock
import com.omasyo.snac.feature.media.impl.components.Review
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.StateFlow

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class SeasonViewModel : ViewModel() {
    val showTitle: StateFlow<UiState<String>> =
        RefreshWrapper(viewModelScope) { mock("Dark") }.response

    val season: StateFlow<UiState<Season>> =
        RefreshWrapper(viewModelScope) {
            mock(
                Season.PreviewSample.first()
                    .copy(overview = "A missing child brings four families together, and the search leads somewhere none of them expected.")
            )
        }.response

    val ratings: StateFlow<UiState<Ratings>> =
        RefreshWrapper(viewModelScope) { mock(Ratings.PreviewSample) }.response

    val episodes: StateFlow<UiState<List<EpisodeDetails>>> =
        RefreshWrapper(viewModelScope) { mock(EpisodeDetails.PreviewSample) }.response

    val cast: StateFlow<UiState<List<Cast>>> =
        RefreshWrapper(viewModelScope) { mock(Cast.PreviewSample) }.response

    val review: StateFlow<UiState<List<Review>>> =
        RefreshWrapper(viewModelScope) { mock(Review.PreviewSample.take(2)) }.response
}
