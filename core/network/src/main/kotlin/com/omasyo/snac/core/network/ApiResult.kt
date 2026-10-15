package com.omasyo.snac.core.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlin.coroutines.cancellation.CancellationException

class TraktHttpException(
    val status: HttpStatusCode,
    val body: String,
) : Exception("HTTP ${status.value}: $body")

internal suspend inline fun <reified T> safeCall(block: () -> HttpResponse): Result<T> =
    try {
        val response = block()
        if (response.status.isSuccess()) {
            Result.success(response.body<T>())
        } else {
            Result.failure(TraktHttpException(response.status, response.bodyAsText()))
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
