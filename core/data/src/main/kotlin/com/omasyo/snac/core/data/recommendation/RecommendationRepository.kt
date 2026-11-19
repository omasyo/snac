package com.omasyo.snac.core.data.recommendation

import com.omasyo.snac.core.common.SnacResult
import com.omasyo.snac.core.model.media.Recommendation

interface RecommendationRepository {
    suspend fun getMovies(): SnacResult<List<Recommendation>>

    suspend fun getShows(): SnacResult<List<Recommendation>>
}
