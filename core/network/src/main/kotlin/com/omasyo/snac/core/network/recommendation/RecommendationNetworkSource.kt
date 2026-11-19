package com.omasyo.snac.core.network.recommendation

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.models.MovieResponse
import com.omasyo.snac.core.network.models.ShowResponse

interface RecommendationNetworkSource : NetworkSource {
    suspend fun getMovies(limit: Int): SnacResult<List<MovieResponse>>

    suspend fun getShows(limit: Int): SnacResult<List<ShowResponse>>
}
