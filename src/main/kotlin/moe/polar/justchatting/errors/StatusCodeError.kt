package moe.polar.justchatting.errors

import io.ktor.http.HttpStatusCode

class StatusCodeError(
    val code: HttpStatusCode,
    override val message: String,
) : RuntimeException()
