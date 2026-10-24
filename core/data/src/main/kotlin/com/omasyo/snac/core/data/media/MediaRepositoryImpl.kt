package com.omasyo.snac.core.data.media

import com.omasyo.snac.core.model.SnacResult
import com.omasyo.snac.core.model.map
import com.omasyo.snac.core.model.media.Movie
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.network.media.MediaNetworkSource
import com.omasyo.snac.core.network.media.models.MovieItem
import com.omasyo.snac.core.network.media.models.ShowItem
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject


@ContributesBinding(AppScope::class)
@Inject
class MediaRepositoryImpl(
    private val mediaNetworkSource: MediaNetworkSource,
) : MediaRepository {
    override suspend fun getTopTrendingMedia(): SnacResult<TrendingItem> =
        mediaNetworkSource.getTopTrendingMedia().map {
            when (val item = it.first()) {
                is MovieItem -> TrendingItem(
                    Movie(
                        item.movie.ids.slug,
                        item.movie.title,
                        item.movie.year,
                        null
                    ), watchers = item.watchers
                )

                is ShowItem -> TrendingItem(
                    Movie(
                        item.show.ids.slug,
                        item.show.title,
                        item.show.year,
                        null
                    ), watchers = item.watchers
                )
            }
        }
}