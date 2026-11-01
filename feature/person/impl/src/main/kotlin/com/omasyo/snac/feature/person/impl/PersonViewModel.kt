package com.omasyo.snac.feature.person.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.snac.core.data.models.CreditRole
import com.omasyo.snac.core.data.models.PersonCredit
import com.omasyo.snac.core.data.models.PersonDetails
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.ui.models.UiState
import com.omasyo.snac.core.ui.models.map
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class MediaFilter { All, Films, Series }

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class PersonViewModel : ViewModel() {
    val person: StateFlow<UiState<PersonDetails>> =
        RefreshWrapper(viewModelScope) { mock(PersonDetails.PreviewSample) }.response

    val selectedRole: StateFlow<CreditRole> field = MutableStateFlow(CreditRole.Acting)

    fun changeRole(role: CreditRole) {
        selectedRole.value = role
    }

    val selectedMediaFilter: StateFlow<MediaFilter> field = MutableStateFlow(MediaFilter.All)

    fun changeMediaFilter(filter: MediaFilter) {
        selectedMediaFilter.value = filter
    }

    val credits: StateFlow<UiState<List<PersonCredit>>> = combine(
        RefreshWrapper(viewModelScope) { mock(PersonCredit.PreviewSample) }.response,
        selectedRole,
        selectedMediaFilter,
    ) { credits, role, filter ->
        credits.map { items ->
            items.filter { it.role == role && filter.matches(it) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState.Loading)

    private fun MediaFilter.matches(credit: PersonCredit): Boolean = when (this) {
        MediaFilter.All -> true
        MediaFilter.Films -> credit.media is Movie
        MediaFilter.Series -> credit.media is Show
    }
}
