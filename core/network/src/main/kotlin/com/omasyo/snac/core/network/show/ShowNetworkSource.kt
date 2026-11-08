package com.omasyo.snac.core.network.show

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.network.NetworkSource
import com.omasyo.snac.core.network.media.models.AnticipatedShowResponse
import com.omasyo.snac.core.network.media.models.TrendingShowResponse
import com.omasyo.snac.core.network.models.CommentResponse
import com.omasyo.snac.core.network.models.CommentSort
import com.omasyo.snac.core.network.models.PeopleResponse
import com.omasyo.snac.core.network.models.Period
import com.omasyo.snac.core.network.models.RatingsResponse
import com.omasyo.snac.core.network.models.ShowResponse
import com.omasyo.snac.core.network.show.models.MostFavoritedShowResponse
import com.omasyo.snac.core.network.show.models.ShowDetailsResponse
import com.omasyo.snac.core.network.show.models.EpisodeDetailsResponse
import com.omasyo.snac.core.network.show.models.MostShowStatsResponse
import com.omasyo.snac.core.network.show.models.SeasonResponse

interface ShowNetworkSource : NetworkSource {
    suspend fun getAnticipated(page: Int, limit: Int): SnacResult<List<AnticipatedShowResponse>>

    suspend fun getDetails(slug: String): SnacResult<ShowDetailsResponse>

    suspend fun getSeasons(slug: String): SnacResult<List<SeasonResponse>>

    suspend fun getSeason(slug: String, season: Int): SnacResult<SeasonResponse>

    suspend fun getEpisodes(slug: String, season: Int): SnacResult<List<EpisodeDetailsResponse>>

    suspend fun getEpisode(slug: String, season: Int, episode: Int): SnacResult<EpisodeDetailsResponse>

    suspend fun getSeasonRatings(slug: String, season: Int): SnacResult<RatingsResponse>

    suspend fun getSeasonPeople(slug: String, season: Int): SnacResult<PeopleResponse>

    suspend fun getSeasonComments(
        slug: String,
        season: Int,
        sort: CommentSort,
        page: Int,
        limit: Int,
    ): SnacResult<List<CommentResponse>>

    suspend fun getEpisodeRatings(
        slug: String,
        season: Int,
        episode: Int,
    ): SnacResult<RatingsResponse>

    suspend fun getEpisodePeople(
        slug: String,
        season: Int,
        episode: Int,
    ): SnacResult<PeopleResponse>

    suspend fun getEpisodeComments(
        slug: String,
        season: Int,
        episode: Int,
        sort: CommentSort,
        page: Int,
        limit: Int,
    ): SnacResult<List<CommentResponse>>

    suspend fun getRatings(slug: String): SnacResult<RatingsResponse>

    suspend fun getPeople(slug: String): SnacResult<PeopleResponse>

    suspend fun getRelated(slug: String, page: Int, limit: Int): SnacResult<List<ShowResponse>>

    suspend fun getComments(
        slug: String,
        sort: CommentSort,
        page: Int,
        limit: Int,
    ): SnacResult<List<CommentResponse>>

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