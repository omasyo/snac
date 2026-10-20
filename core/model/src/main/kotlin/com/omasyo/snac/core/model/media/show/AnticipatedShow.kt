package com.omasyo.snac.core.model.media.show

import com.omasyo.snac.core.model.media.AnticipatedMedia

data class AnticipatedShow(
    override val slug: String,
    override val title: String,
    override val listCount: Int,
    override val imageUrl: String?,
) : Show, AnticipatedMedia