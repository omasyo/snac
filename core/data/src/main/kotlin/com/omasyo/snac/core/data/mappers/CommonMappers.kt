package com.omasyo.snac.core.data.mappers

import com.omasyo.snac.core.data.models.Genre
import com.omasyo.snac.core.data.models.SocialIds
import com.omasyo.snac.core.network.models.SocialIdsResponse

const val TAG = "CommonMappers"

internal fun String.toImageUrl(): String = "https://$this"

internal fun String.toGenre(): Genre = Genre(this)

internal fun SocialIdsResponse?.toSocialIds(): SocialIds =
    if (this == null) SocialIds.Empty
    else SocialIds(
        twitter = twitter,
        facebook = facebook,
        instagram = instagram,
        wikipedia = wikipedia,
    )
