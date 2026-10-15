package com.omasyo.snac.core.data.auth

import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.io.encoding.Base64

private val secureRandom = SecureRandom()

private val urlSafeNoPadding = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT)

private fun randomUrlSafeString(byteCount: Int): String {
    val bytes = ByteArray(byteCount)
    secureRandom.nextBytes(bytes)
    return urlSafeNoPadding.encode(bytes)
}

internal fun generateCodeVerifier(): String = randomUrlSafeString(64)

internal fun generateState(): String = randomUrlSafeString(32)

internal fun generateCodeChallenge(codeVerifier: String): String {
    val hash = MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
    return urlSafeNoPadding.encode(hash)
}
