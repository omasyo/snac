package com.omasyo.snac.core.model.media.show

import com.omasyo.snac.core.model.media.Media

interface Show : Media {
    val slug: String
    val title: String
}