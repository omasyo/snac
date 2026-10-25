package com.omasyo.snac.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.data.media.MediaRepository
import com.omasyo.snac.core.data.show.ShowRepository
import com.omasyo.snac.core.model.media.AnticipatedItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.ui.models.UiState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class HomeViewModel(
    private val mediaRepository: MediaRepository,
    private val showRepository: ShowRepository,
) : ViewModel() {
    val topTrendingMedia: StateFlow<UiState<TrendingItem?>> =
        RefreshWrapper(viewModelScope) { mediaRepository.getTopTrendingMedia() }.response

    val trendingMovies: StateFlow<UiState<List<TrendingItem>>> =
        RefreshWrapper(viewModelScope) { mock(TrendingItem.MoviePreviewSample) }.response

    val trendingShows: StateFlow<UiState<List<TrendingItem>>> =
        RefreshWrapper(viewModelScope) { showRepository.getTrendingMedia() }.response

    val boxOffice: StateFlow<UiState<List<BoxOfficeItem>>> =
        RefreshWrapper(viewModelScope) { mock(BoxOfficeItem.PreviewSample) }.response

    val anticipated: StateFlow<UiState<List<AnticipatedItem>>> =
        RefreshWrapper(viewModelScope) { mock(AnticipatedItem.PreviewSample) }.response

    private suspend fun <T> mock(value: T): SnacResult<T> {
        delay(Random.nextLong(1000, 5000).milliseconds)
        return if (Random.nextBoolean()) {
            SnacResult.Success(value)
        } else {
            SnacResult.Failure(SnacResult.Error.Unexpected)
        }
    }
}

internal class RefreshWrapper<T>(
    private val scope: CoroutineScope,
    private val request: suspend () -> SnacResult<T>,
) {
    val response: StateFlow<UiState<T>> field = MutableStateFlow<UiState<T>>(UiState.Loading)

    private var job: Job? = null

    init {
        refresh()
    }

    fun refresh() {
        job?.cancel()
        job = scope.launch {
            response.value = UiState.Loading
            response.value = when (val response = request()) {
                is SnacResult.Failure -> UiState.Error(response.error, ::refresh)
                is SnacResult.Success -> UiState.Success(response.value)
            }
        }
    }
}