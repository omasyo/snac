package com.omasyo.snac.core.ui.models

import com.omasyo.snac.core.common.SnacResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class RefreshWrapper<T>(
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

suspend fun <T> mock(value: T): SnacResult<T> {
    delay(Random.nextLong(500, 3000).milliseconds)
    return if (Random.nextBoolean()) {
        SnacResult.Success(value)
    } else {
        SnacResult.Failure(SnacResult.Error.Unexpected)
    }
}
