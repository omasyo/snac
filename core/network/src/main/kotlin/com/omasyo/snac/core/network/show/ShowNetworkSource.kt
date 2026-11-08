package com.omasyo.snac.core.network.show

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.media.models.AnticipatedShowResponse
import com.omasyo.snac.core.network.media.models.TrendingShowResponse
import com.omasyo.snac.core.network.models.Period
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.show.models.MostFavoritedShowResponse
import com.omasyo.snac.core.network.show.models.ShowDetailsResponse
import com.omasyo.snac.core.network.show.models.MostShowStatsResponse

interface ShowNetworkSource : NetworkSource {
    suspend fun getAnticipated(page: Int, limit: Int): SnacResult<List<AnticipatedShowResponse>>

    suspend fun getDetails(slug: String): SnacResult<ShowDetailsResponse>

    suspend fun getMostCollected(
        period: Period,
        page: Int,
        limit: Int,
    ): SnacResult<List<MostShowStatsResponse>>

    suspend fun getMostFavorited(
        period: Period,
        page: Int,
        limit: Int,
    ): SnacResult<List<MostFavoritedShowResponse>>

    suspend fun getMostPlayed(
        period: Period,
        page: Int,
        limit: Int,
    ): SnacResult<List<MostShowStatsResponse>>

    suspend fun getMostWatched(
        period: Period,
        page: Int,
        limit: Int,
    ): SnacResult<List<MostShowStatsResponse>>

    suspend fun getPopular(page: Int, limit: Int): SnacResult<List<ShowResponse>>

    suspend fun getTrending(page: Int, limit: Int): SnacResult<List<TrendingShowResponse>>
}