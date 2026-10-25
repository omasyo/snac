package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.model.media.Show
import com.omasyo.snac.core.network.models.Show as NetworkShow

fun NetworkShow.toShow() = Show(
    slug = ids.slug,
    title = title,
    year = year,
    imageUrl = null,
)