package com.omasyo.snac.feature.calendar.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omasyo.snac.core.data.calendar.CalendarRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate

enum class CalendarFilter { All, HotReleases }

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class CalendarViewModel(
    calendarRepository: CalendarRepository,
) : ViewModel() {

    private data class Query(val filter: CalendarFilter, val startDate: LocalDate)

    private val query = MutableStateFlow(Query(CalendarFilter.All, LocalDate.now()))

    val filter: StateFlow<CalendarFilter> = query
        .map { it.filter }
        .stateIn(viewModelScope, SharingStarted.Eagerly, query.value.filter)

    fun jumpToDate(date: LocalDate) {
        restart(query.value.filter, date)
    }

    fun changeFilter(filter: CalendarFilter, visibleDate: LocalDate) {
        if (query.value.filter == filter) return
        restart(filter, visibleDate)
    }

    private fun restart(filter: CalendarFilter, startDate: LocalDate) {
        query.update { Query( filter, startDate) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: Flow<PagingData<CalendarUiItem>> = query
        .flatMapLatest { query ->
            val days = when (query.filter) {
                CalendarFilter.All -> calendarRepository.getAllMedia(query.startDate)
                CalendarFilter.HotReleases -> calendarRepository.getHotReleases(query.startDate)
            }
            days.map { it.toUiItems() }
        }
        .cachedIn(viewModelScope)
}
