package com.omasyo.snac.core.network

import android.util.Log
import com.omasyo.snac.core.model.SnacResult
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.ContentConvertException
import java.io.IOException

interface NetworkSource {
    val tag: String
        get() = this::class.simpleName.toString()
}

//TODO review logging
internal suspend inline fun <reified T> NetworkSource.safeCall(block: () -> HttpResponse): SnacResult<T> {
    val error = try {
        val response = block()
        if (response.status.isSuccess()) {
            return SnacResult.Success(response.body<T>())
        }
        toError(response)
    } catch (_: IOException) {
        SnacResult.Error.NoConnection
    } catch (e: ResponseException) {
        toError(e.response)
    } catch (e: ContentConvertException) {
        unexpected(e.toString())
    } catch (e: NoTransformationFoundException) {
        unexpected(e.toString())
    }

    if (error !is SnacResult.Error.Unexpected) Log.i(tag, error.toString())
    return SnacResult.Failure(error)
}

internal suspend fun NetworkSource.toError(response: HttpResponse): SnacResult.Error =
    when (response.status.value) {
        404 -> SnacResult.Error.NotFound
        420 -> SnacResult.Error.AccountLimitExceeded(response.headers["X-Account-Limit"])
        423 -> SnacResult.Error.AccountLocked
        429 -> SnacResult.Error.RateLimitExceeded(response.headers[HttpHeaders.RetryAfter]?.toLongOrNull())
        in 500..599 -> SnacResult.Error.ServerUnavailable
        else -> unexpected("HTTP ${response.status.value}: ${response.bodyAsText()}")
    }

internal fun NetworkSource.unexpected(detail: String): SnacResult.Error {
    Log.e(tag, detail)
    return SnacResult.Error.Unexpected
}
