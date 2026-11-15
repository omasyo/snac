package com.omasyo.snac.core.network.person

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.models.PersonResponse
import com.omasyo.snac.core.network.person.models.PersonMovieCreditsResponse
import com.omasyo.snac.core.network.person.models.PersonShowCreditsResponse
import com.omasyo.snac.core.network.safeCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@ContributesBinding(AppScope::class)
@Inject
class PersonNetworkSourceImpl(
    private val client: HttpClient
) : PersonNetworkSource {
    override suspend fun getDetails(slug: String): SnacResult<PersonResponse> =
        safeCall {
            client.get("people/$slug/") {
                parameter("extended", "full")
            }
        }

    override suspend fun getMovieCredits(slug: String): SnacResult<PersonMovieCreditsResponse> =
        safeCall {
            client.get("people/$slug/movies") {
                parameter("extended", "full")
            }
        }

    override suspend fun getShowCredits(slug: String): SnacResult<PersonShowCreditsResponse> =
        safeCall {
            client.get("people/$slug/shows") {
                parameter("extended", "full")
            }
        }
}
