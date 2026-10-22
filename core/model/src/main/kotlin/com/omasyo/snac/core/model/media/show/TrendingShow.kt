package com.omasyo.snac.core.model.media.show

import com.omasyo.snac.core.model.media.ChartMedia
import com.omasyo.snac.core.model.media.TrendingMedia

data class TrendingShow(
    override val slug: String,
    override val title: String,
    override val year: Int,
    override val watchingCount: Int,
    override val imageUrl: String?,
) : Show, TrendingMedia, ChartMedia {
    companion object {
        val PreviewSample = List(10) {
            TrendingShow(
                slug = "show-$it",
                title = "Dark",
                year = 2023,
                watchingCount = 300,
                imageUrl = null,
            )
        }
    }
}