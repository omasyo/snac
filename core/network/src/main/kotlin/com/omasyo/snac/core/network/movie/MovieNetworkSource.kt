package com.omasyo.snac.core.network.movie

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.media.models.TrendingMovieResponse
import com.omasyo.snac.core.network.movie.models.BoxOfficeResponse

interface MovieNetworkSource : NetworkSource {

    suspend fun getBoxOffice() : SnacResult<List<BoxOfficeResponse>>

    suspend fun getTrending(page: Int, limit: Int): SnacResult<List<TrendingMovieResponse>>
}