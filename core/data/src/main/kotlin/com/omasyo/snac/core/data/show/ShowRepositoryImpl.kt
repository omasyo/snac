package com.omasyo.snac.core.data.show

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toTrendingItem
import com.omasyo.snac.core.model.media.TrendingItem
import com.omasyo.snac.core.network.show.ShowNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject


@ContributesBinding(AppScope::class)
@Inject
class ShowRepositoryImpl(
    private val showNetworkSource: ShowNetworkSource,
) : ShowRepository {
    override suspend fun getTrending(): SnacResult<List<TrendingItem>> =
        showNetworkSource.getTrending(1, 10).map { list -> list.map { it.toTrendingItem() } }
}
