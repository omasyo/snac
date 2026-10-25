package com.omasyo.snac.core.data.movie

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toBoxOfficeItem
import com.omasyo.snac.core.data.mappers.toTrendingItem
import com.omasyo.snac.core.model.media.BoxOfficeItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.network.movie.MovieNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject


@ContributesBinding(AppScope::class)
@Inject
class MovieRepositoryImpl(
    private val movieNetworkSource: MovieNetworkSource,
) : MovieRepository {
    override suspend fun getBoxOffice(): SnacResult<List<BoxOfficeItem>> =
        movieNetworkSource.getBoxOffice().map { list -> list.map { it.toBoxOfficeItem() } }

    override suspend fun getTrending(): SnacResult<List<TrendingItem>> =
        movieNetworkSource.getTrending(1, 10).map { list -> list.map { it.toTrendingItem() } }
}