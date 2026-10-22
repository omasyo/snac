package com.omasyo.snac.core.model.media

interface ChartMedia : Media {
    val title: String
    val year: Int
    val imageUrl: String?
}