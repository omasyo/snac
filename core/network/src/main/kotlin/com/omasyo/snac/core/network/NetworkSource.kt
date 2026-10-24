package com.omasyo.snac.core.network

import android.util.Log
import com.omasyo.snac.core.model.SnacError
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
        toSnacError(response)
    } catch (_: IOException) {
        SnacError.Connection
    } catch (e: ResponseException) {
        toSnacError(e.response)
    } catch (e: ContentConvertException) {
        errorOnMyPart(e.toString())
    } catch (e: NoTransformationFoundException) {
        errorOnMyPart(e.toString())
    }

    if (error !is SnacError.ErrorOnMyPart) Log.i(tag, error.toString())
    return SnacResult.Failure(error)
}

internal suspend fun NetworkSource.toSnacError(response: HttpResponse): SnacError =
    when (response.status.value) {
        404 -> SnacError.NotFound
        420 -> SnacError.AccountLimitExceeded(response.headers["X-Account-Limit"])
        423 -> SnacError.LockedAccount
        429 -> SnacError.RateLimitExceeded(response.headers[HttpHeaders.RetryAfter]?.toLongOrNull())
        in 500..599 -> SnacError.ServerUnavailable
        else -> errorOnMyPart("HTTP ${response.status.value}: ${response.bodyAsText()}")
    }

internal fun NetworkSource.errorOnMyPart(detail: String): SnacError {
    Log.e(tag, detail)
    return SnacError.ErrorOnMyPart
}
