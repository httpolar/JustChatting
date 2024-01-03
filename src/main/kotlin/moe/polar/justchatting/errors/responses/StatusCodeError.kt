package moe.polar.justchatting.errors.responses

import io.ktor.http.HttpStatusCode

class StatusCodeError(
    val code: HttpStatusCode,
    override val message: String,
) : RuntimeException()
