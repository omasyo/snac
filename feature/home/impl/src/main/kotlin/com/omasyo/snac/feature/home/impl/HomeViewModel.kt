package com.omasyo.snac.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.model.SnacResult
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.ui.models.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class HomeViewModel : ViewModel() {
    val trendingMovies: StateFlow<UiState<List<TrendingItem>>> field = MutableStateFlow<UiState<List<TrendingItem>>>(
        UiState.Loading
    )

    val trendingShows: StateFlow<UiState<List<TrendingItem>>> field = MutableStateFlow<UiState<List<TrendingItem>>>(
        UiState.Loading
    )

    val boxOffice: StateFlow<UiState<List<BoxOfficeItem>>> field = MutableStateFlow<UiState<List<BoxOfficeItem>>>(
        UiState.Loading
    )

    val anticipated: StateFlow<UiState<List<AnticipatedItem>>> field = MutableStateFlow<UiState<List<AnticipatedItem>>>(
        UiState.Loading
    )

    init {
        viewModelScope.launch {
            launch { seed(trendingMovies, TrendingItem.MoviePreviewSample) }
            launch { seed(trendingShows, TrendingItem.ShowPreviewSample) }
            launch { seed(boxOffice, BoxOfficeItem.PreviewSample) }
            launch { seed(anticipated, AnticipatedItem.PreviewSample) }
        }
    }

    private suspend fun <T> seed(state: MutableStateFlow<UiState<T>>, value: T) {
        delay(Random.nextLong(1000, 5000).milliseconds)
        if (Random.nextBoolean()) {
            state.value = UiState.Success(value)
        } else {
            state.value = UiState.Error(SnacResult.Error.Unexpected) {}
        }
    }
}