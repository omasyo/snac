package com.omasyo.snac.feature.auth

import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.io.encoding.Base64


const val ClientId = "Stz7nOfC8A-IU6feny4jZrsmBL3PM4nl45YSxcx1x3U"

const val RedirectUri = "snac://redirect"

private fun generateRandomBytes(): ByteArray {
    val values = ByteArray(64)
    SecureRandom().nextBytes(values)
    return values
}

private fun generateCodeVerifier(): String {
    return Base64.UrlSafe.encode(generateRandomBytes()).dropLast(2)
}

private fun generateCodeChallenge(codeVerifier: String): String {
    val hash = MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
    return Base64.UrlSafe.encode(hash).dropLast(1)
}

fun generateUrl(
    redirectUri: String,
    codeChallenge: String = generateCodeChallenge(generateCodeVerifier())
): String {
    return "https://auth.trakt.tv/oauth/authorize?response_type=code" +
            "&client_id=$ClientId&redirect_uri=$redirectUri" +
            "&code_challenge=$codeChallenge&code_challenge_method=S256"
}