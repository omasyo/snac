package com.omasyo.snac.core.network

object TraktConfig {
    const val CLIENT_ID = "Stz7nOfC8A-IU6feny4jZrsmBL3PM4nl45YSxcx1x3U"
    const val REDIRECT_URI = "snac://redirect"

    const val AUTH_HOST = "auth.trakt.tv"
    const val API_HOST = "api.trakt.tv"

    const val API_VERSION = "2"

    const val AUTHORIZE_URL = "https://$AUTH_HOST/oauth/authorize"
    internal const val TOKEN_URL = "https://$AUTH_HOST/oauth/token"
    internal const val REVOKE_URL = "https://$AUTH_HOST/oauth/revoke"
    internal const val API_BASE_URL = "https://$API_HOST/"
}
