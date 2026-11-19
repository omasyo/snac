package com.omasyo.snac.core.data.recommendation

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.common.map
import com.omasyo.snac.core.data.mappers.toRecommendation
import com.omasyo.snac.core.model.media.Recommendation
import com.omasyo.snac.core.network.recommendation.RecommendationNetworkSource
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class RecommendationRepositoryImpl(
    private val recommendationNetworkSource: RecommendationNetworkSource,
) : RecommendationRepository {
    override suspend fun getMovies(): SnacResult<List<Recommendation>> =
        recommendationNetworkSource.getMovies(limit = 10)
            .map { items -> items.map { it.toRecommendation() } }

    override suspend fun getShows(): SnacResult<List<Recommendation>> =
        recommendationNetworkSource.getShows(limit = 10)
            .map { items -> items.map { it.toRecommendation() } }
}
