package moe.polar.justchatting.services

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

private val bcryptEncoder = BCryptPasswordEncoder(14)

fun String.bcryptHash(): String {
    return bcryptEncoder.encode(this)
}

fun bcryptMatch(rawPassword: String, encodedPassword: String): Boolean {
    return bcryptEncoder.matches(rawPassword, encodedPassword)
}
