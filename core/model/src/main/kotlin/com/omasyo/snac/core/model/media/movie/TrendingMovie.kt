package com.omasyo.snac.core.model.media.movie

import com.omasyo.snac.core.model.media.ChartMedia
import com.omasyo.snac.core.model.media.TrendingMedia

data class TrendingMovie(
    override val slug: String,
    override val title: String,
    override val year: Int,
    override val watchingCount: Int,
    override val imageUrl: String?,
) : Movie, TrendingMedia, ChartMedia