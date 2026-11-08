package com.omasyo.snac.core.network.person

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.person.models.PersonDetailsResponse
import com.omasyo.snac.core.network.person.models.PersonMovieCreditsResponse
import com.omasyo.snac.core.network.person.models.PersonShowCreditsResponse
import com.omasyo.snac.core.network.safeCall
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get

@ContributesBinding(AppScope::class)
@Inject
class PersonNetworkSourceImpl(
    private val client: HttpClient
) : PersonNetworkSource {
    override suspend fun getDetails(slug: String): SnacResult<PersonDetailsResponse> =
        safeCall { client.get("people/$slug/") }

    override suspend fun getMovieCredits(slug: String): SnacResult<PersonMovieCreditsResponse> =
        safeCall { client.get("people/$slug/movies") }

    override suspend fun getShowCredits(slug: String): SnacResult<PersonShowCreditsResponse> =
        safeCall { client.get("people/$slug/shows") }
}
