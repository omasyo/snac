package com.omasyo.snac.core.model.media.movie

import com.omasyo.snac.core.model.media.Media

interface Movie : Media {
    val slug: String
    val title: String
}