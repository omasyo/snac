package com.omasyo.snac.core.data.show

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toAnticipatedItem
import com.omasyo.snac.core.data.mappers.toCast
import com.omasyo.snac.core.data.mappers.toCollectedItem
import com.omasyo.snac.core.data.mappers.toEpisodeDetails
import com.omasyo.snac.core.data.mappers.toFavoritedItem
import com.omasyo.snac.core.data.mappers.toNetworkCommentSort
import com.omasyo.snac.core.data.mappers.toPeopleList
import com.omasyo.snac.core.data.mappers.toPeriod
import com.omasyo.snac.core.data.mappers.toPlayedItem
import com.omasyo.snac.core.data.mappers.toPopularItem
import com.omasyo.snac.core.data.mappers.toRatings
import com.omasyo.snac.core.data.mappers.toReview
import com.omasyo.snac.core.data.mappers.toSeason
import com.omasyo.snac.core.data.mappers.toShow
import com.omasyo.snac.core.data.mappers.toShowDetails
import com.omasyo.snac.core.data.mappers.toTrendingItem
import com.omasyo.snac.core.data.mappers.toWatchedItem
import com.omasyo.snac.core.data.models.Cast
import com.omasyo.snac.core.data.models.EpisodeDetails
import com.omasyo.snac.core.data.models.PeopleList
import com.omasyo.snac.core.data.models.Ratings
import com.omasyo.snac.core.data.models.Comment
import com.omasyo.snac.core.data.models.Season
import com.omasyo.snac.core.data.models.ShowDetails
import com.omasyo.snac.core.data.paging.ChartPagingSource
import com.omasyo.snac.core.data.paging.CustomPagingSource
import com.omasyo.snac.core.model.extra.CommentSort
import com.omasyo.snac.core.model.extra.TimePeriod
import com.omasyo.snac.core.model.media.ChartItem
import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.network.show.ShowNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import com.omasyo.snac.core.network.models.CommentSort as NetworkCommentSort


@ContributesBinding(AppScope::class)
@Inject
class ShowRepositoryImpl(
    private val showNetworkSource: ShowNetworkSource,
) : ShowRepository {
    override fun getAnticipated(startPosition: Int): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> showNetworkSource.getAnticipated(page, PAGE_SIZE) },
                mapper = { it.toAnticipatedItem() },
            )
        }.flow

    override suspend fun getDetails(slug: String): SnacResult<ShowDetails> =
        showNetworkSource.getDetails(slug).map { it.toShowDetails() }

    override suspend fun getSeasons(slug: String): SnacResult<List<Season>> =
        showNetworkSource.getSeasons(slug).map { list -> list.map { it.toSeason() } }

    override suspend fun getSeason(slug: String, season: Int): SnacResult<Season> =
        showNetworkSource.getSeason(slug, season).map { it.toSeason() }

    override suspend fun getEpisodes(slug: String, season: Int): SnacResult<List<EpisodeDetails>> =
        showNetworkSource.getEpisodes(slug, season)
            .map { list -> list.map { it.toEpisodeDetails() } }

    override suspend fun getEpisode(
        slug: String,
        season: Int,
        episode: Int,
    ): SnacResult<EpisodeDetails> =
        showNetworkSource.getEpisode(slug, season, episode).map { it.toEpisodeDetails() }

    override suspend fun getSeasonRatings(slug: String, season: Int): SnacResult<Ratings> =
        showNetworkSource.getSeasonRatings(slug, season).map { it.toRatings() }

    override suspend fun getSeasonCast(slug: String, season: Int): SnacResult<List<Cast>> =
        showNetworkSource.getSeasonPeople(slug, season).map { it.toCast() }

    override suspend fun getSeasonPeople(slug: String, season: Int): SnacResult<PeopleList> =
        showNetworkSource.getSeasonPeople(slug, season).map { it.toPeopleList() }

    override suspend fun getSeasonTopComments(slug: String, season: Int): SnacResult<List<Comment>> =
        showNetworkSource.getSeasonComments(
            slug,
            season,
            NetworkCommentSort.Likes,
            page = 1,
            limit = 2
        )
            .map { list -> list.map { it.toReview() } }

    override suspend fun getEpisodeRatings(
        slug: String,
        season: Int,
        episode: Int,
    ): SnacResult<Ratings> =
        showNetworkSource.getEpisodeRatings(slug, season, episode).map { it.toRatings() }

    override suspend fun getEpisodeCast(
        slug: String,
        season: Int,
        episode: Int,
    ): SnacResult<List<Cast>> =
        showNetworkSource.getEpisodePeople(slug, season, episode).map { it.toCast() }

    override suspend fun getEpisodePeople(
        slug: String,
        season: Int,
        episode: Int,
    ): SnacResult<PeopleList> =
        showNetworkSource.getEpisodePeople(slug, season, episode).map { it.toPeopleList() }

    override suspend fun getEpisodeTopComments(
        slug: String,
        season: Int,
        episode: Int,
    ): SnacResult<List<Comment>> =
        showNetworkSource.getEpisodeComments(
            slug, season, episode, NetworkCommentSort.Likes, page = 1, limit = 2,
        ).map { list -> list.map { it.toReview() } }

    override suspend fun getRatings(slug: String): SnacResult<Ratings> =
        showNetworkSource.getRatings(slug).map { it.toRatings() }

    override suspend fun getCast(slug: String): SnacResult<List<Cast>> =
        showNetworkSource.getPeople(slug).map { it.toCast() }

    override suspend fun getPeople(slug: String): SnacResult<PeopleList> =
        showNetworkSource.getPeople(slug).map { it.toPeopleList() }

    override fun getRelated(slug: String): Flow<PagingData<Show>> =
        Pager(config = pagingConfig) {
            CustomPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> showNetworkSource.getRelated(slug, page, PAGE_SIZE) },
                mapper = { it.toShow() },
            )
        }.flow

    override fun getComments(slug: String, sort: CommentSort): Flow<PagingData<Comment>> =
        Pager(config = pagingConfig) {
            CustomPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    showNetworkSource.getComments(
                        slug,
                        sort.toNetworkCommentSort(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toReview() },
            )
        }.flow

    override suspend fun getTopComments(slug: String): SnacResult<List<Comment>> =
        showNetworkSource.getComments(slug, NetworkCommentSort.Likes, page = 1, limit = 2)
            .map { list -> list.map { it.toReview() } }

    override fun getMostCollected(
        period: TimePeriod,
        startPosition: Int
    ): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    showNetworkSource.getMostCollected(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toCollectedItem() },
            )
        }.flow

    override fun getMostFavorited(
        period: TimePeriod,
        startPosition: Int
    ): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    showNetworkSource.getMostFavorited(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toFavoritedItem() },
            )
        }.flow

    override fun getMostPlayed(
        period: TimePeriod,
        startPosition: Int
    ): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    showNetworkSource.getMostPlayed(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toPlayedItem() },
            )
        }.flow

    override fun getMostWatched(
        period: TimePeriod,
        startPosition: Int
    ): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page ->
                    showNetworkSource.getMostCollected(
                        period.toPeriod(),
                        page,
                        PAGE_SIZE
                    )
                },
                mapper = { it.toWatchedItem() },
            )
        }.flow

    override fun getPopular(startPosition: Int): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> showNetworkSource.getPopular(page, PAGE_SIZE) },
                mapper = { it.toPopularItem() },
            )
        }.flow

    override suspend fun getTopTrending(): SnacResult<List<TrendingItem>> =
        showNetworkSource.getTrending(1, 10).map { list -> list.map { it.toTrendingItem() } }

    override fun getTrending(startPosition: Int): Flow<PagingData<ChartItem>> =
        Pager(initialKey = startPosition, config = pagingConfig) {
            ChartPagingSource(
                pageSize = PAGE_SIZE,
                provider = { page -> showNetworkSource.getTrending(page, PAGE_SIZE) },
                mapper = { it.toTrendingItem() },
            )
        }.flow

    companion object {
        private const val PAGE_SIZE = 30

        private val pagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            jumpThreshold = PAGE_SIZE * 3,
        )
    }
}
