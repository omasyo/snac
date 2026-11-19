package com.omasyo.snac.core.ui.models

import com.omasyo.snac.core.common.SnacResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class GatedRefreshWrapper<T>(
    private val scope: CoroutineScope,
    isOpen: Flow<Boolean>,
    private val request: suspend () -> SnacResult<T>,
) {
    val response: StateFlow<UiState<T>> field = MutableStateFlow<UiState<T>>(UiState.Loading)

    private var open = false

    private var job: Job? = null

    init {
        scope.launch {
            isOpen.distinctUntilChanged().collect {
                open = it
                if (it) {
                    refresh()
                } else {
                    job?.cancel()
                    response.value = UiState.Loading
                }
            }
        }
    }

    fun refresh() {
        if (!open) return
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
