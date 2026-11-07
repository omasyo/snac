package com.omasyo.snac.feature.media.impl.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.data.models.ShowDetails
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.models.RefreshWrapper
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.mock
import com.omasyo.snac.core.data.models.Review
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class ShowViewModel : ViewModel() {
    val show: StateFlow<UiState<ShowDetails>> =
        RefreshWrapper(viewModelScope) { mock(ShowDetails.PreviewSample) }.response

    val ratings: StateFlow<UiState<Ratings>> =
        RefreshWrapper(viewModelScope) { mock(Ratings.PreviewSample) }.response

    val seasons: StateFlow<UiState<List<Season>>> =
        RefreshWrapper(viewModelScope) { mock(Season.PreviewSample) }.response

    val cast: StateFlow<UiState<List<Cast>>> =
        RefreshWrapper(viewModelScope) { mock(Cast.PreviewSample) }.response

    val review: StateFlow<UiState<List<Review>>> =
        RefreshWrapper(viewModelScope) { mock(Review.PreviewSample.take(2)) }.response

    val relatedShows: Flow<PagingData<Show>> = flowOf(PagingData.from(Show.PreviewSample))
}
