package moe.polar.justchatting.services

import io.ktor.http.HttpStatusCode
import moe.polar.justchatting.errors.StatusCodeError

fun unauthorized(message: String = "Unauthorized"): Nothing {
    throw StatusCodeError(HttpStatusCode.Unauthorized, message)
}

fun badRequest(message: String = "Bad Request"): Nothing {
    throw StatusCodeError(HttpStatusCode.BadRequest, message)
}

fun internalServerError(message: String = "Internal Server Error"): Nothing {
    throw StatusCodeError(HttpStatusCode.InternalServerError, message)
}
