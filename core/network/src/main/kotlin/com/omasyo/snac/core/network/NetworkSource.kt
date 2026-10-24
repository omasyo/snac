package com.omasyo.snac.core.network

import android.util.Log
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.ContentConvertException
import java.io.IOException

interface NetworkSource {
    val tag: String
        get() = this::class.simpleName.toString()
}

internal suspend inline fun <reified T> NetworkSource.safeCall(block: () -> HttpResponse): NetworkResult<T> {
    val error = try {
        val response = block()
        if (response.status.isSuccess()) {
            return NetworkResult.Success(response.body<T>())
        }
        response.toNetworkError()
    } catch (_: IOException) {
        NetworkResult.Error.Connection
    } catch (e: ResponseException) {
        e.response.toNetworkError()
    } catch (e: ContentConvertException) {
        NetworkResult.Error.ErrorOnMyPart(e.toString())
    } catch (e: NoTransformationFoundException) {
        NetworkResult.Error.ErrorOnMyPart(e.toString())
    }

    logNetworkError(error)
    return NetworkResult.Failure(error)
}

private fun NetworkSource.logNetworkError(error: NetworkResult.Error) {
    when (error) {
        is NetworkResult.Error.ErrorOnMyPart -> Log.e(tag, error.detail)
        else -> Log.i(tag, error.toString())
    }
}
