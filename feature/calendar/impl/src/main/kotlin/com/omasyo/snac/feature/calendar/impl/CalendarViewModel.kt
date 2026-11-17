package com.omasyo.snac.feature.calendar.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omasyo.snac.core.data.calendar.CalendarRepository
import com.omasyo.snac.core.data.genre.MovieGenres
import com.omasyo.snac.core.data.genre.ShowGenres
import com.omasyo.snac.core.model.media.CalendarMediaType
import com.omasyo.snac.core.model.media.Genre
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate

private val DefaultGenre: Genre = MovieGenres.first { it.slug == "action" }

private val CalendarMediaType.genres: List<Genre>
    get() = when (this) {
        CalendarMediaType.All, CalendarMediaType.Shows -> ShowGenres
        CalendarMediaType.Movies -> MovieGenres
    }

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class CalendarViewModel(
    calendarRepository: CalendarRepository,
) : ViewModel() {

    private data class Query(
        val mediaType: CalendarMediaType,
        val genre: Genre,
        val startDate: LocalDate,
    )

    private val query = MutableStateFlow(
        Query(CalendarMediaType.All, DefaultGenre, LocalDate.now())
    )

    val mediaType: StateFlow<CalendarMediaType> = query.stateOf { it.mediaType }

    val genre: StateFlow<Genre> = query.stateOf { it.genre }

    val genres: StateFlow<List<Genre>> = query.stateOf { it.mediaType.genres }

    fun jumpToDate(date: LocalDate) {
        query.update { it.copy(startDate = date) }
    }

    fun changeMediaType(mediaType: CalendarMediaType, visibleDate: LocalDate) {
        if (query.value.mediaType == mediaType) return
        query.update {
            it.copy(
                mediaType = mediaType,
                genre = it.genre.takeIf { genre -> genre in mediaType.genres } ?: DefaultGenre,
                startDate = visibleDate,
            )
        }
    }

    fun changeGenre(genre: Genre, visibleDate: LocalDate) {
        if (query.value.genre == genre) return
        query.update { it.copy(genre = genre, startDate = visibleDate) }
    }

    private fun <T> StateFlow<Query>.stateOf(select: (Query) -> T): StateFlow<T> =
        map(select)
            .distinctUntilChanged()
            .stateIn(viewModelScope, SharingStarted.Eagerly, select(value))

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: Flow<PagingData<CalendarUiItem>> = query
        .flatMapLatest { query ->
            val days = calendarRepository.getAllMedia(query.startDate, query.mediaType, query.genre)
            days.map { it.toUiItems() }
        }
        .cachedIn(viewModelScope)
}
