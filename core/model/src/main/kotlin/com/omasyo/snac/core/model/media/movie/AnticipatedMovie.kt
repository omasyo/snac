package com.omasyo.snac.core.model.media.movie

import com.omasyo.snac.core.model.media.AnticipatedMedia

data class AnticipatedMovie(
    override val slug: String,
    override val title: String,
    override val listCount: Int,
    override val imageUrl: String?,
) : Movie, AnticipatedMedia