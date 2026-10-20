package com.omasyo.snac.core.model.media.show

import com.omasyo.snac.core.model.media.TrendingMedia

data class TrendingShow(
    override val slug: String,
    override val title: String,
    override val watchingCount: Int,
    override val imageUrl: String?,
) : Show, TrendingMedia {
    companion object {
        val PreviewSample = List(10) {
            TrendingShow(
                slug = "show-$it",
                title = "Dark",
                watchingCount = 300,
                imageUrl = null,
            )
        }
    }
}