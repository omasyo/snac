package com.omasyo.snac.core.network.person

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.person.models.PersonDetailsResponse
import com.omasyo.snac.core.network.person.models.PersonMovieCreditsResponse
import com.omasyo.snac.core.network.person.models.PersonShowCreditsResponse

interface PersonNetworkSource : NetworkSource {
    suspend fun getDetails(slug: String): SnacResult<PersonDetailsResponse>

    suspend fun getMovieCredits(slug: String): SnacResult<PersonMovieCreditsResponse>

    suspend fun getShowCredits(slug: String): SnacResult<PersonShowCreditsResponse>
}
