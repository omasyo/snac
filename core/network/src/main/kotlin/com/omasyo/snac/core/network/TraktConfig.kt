package com.omasyo.snac.core.network

object TraktConfig {
    const val ClientId = "Stz7nOfC8A-IU6feny4jZrsmBL3PM4nl45YSxcx1x3U"
    const val RedirectUri = "snac://redirect"

    const val AuthHost = "auth.trakt.tv"
    const val ApiHost = "api.trakt.tv"

    const val AuthorizeUrl = "https://$AuthHost/oauth/authorize"
    internal const val TokenUrl = "https://$AuthHost/oauth/token"
    internal const val RevokeUrl = "https://$AuthHost/oauth/revoke"
    internal const val ApiBaseUrl = "https://$ApiHost/"
}
