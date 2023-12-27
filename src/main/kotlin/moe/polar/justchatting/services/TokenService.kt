package moe.polar.justchatting.services

import java.security.SecureRandom

private val secureRandom = SecureRandom()

fun generateToken(): String {
    val bytes = ByteArray(64)
    secureRandom.nextBytes(bytes)

    return bytes.joinToString("") { "%02x".format(it) }
}
