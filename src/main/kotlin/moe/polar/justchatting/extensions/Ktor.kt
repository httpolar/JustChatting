package moe.polar.justchatting.extensions

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.principal
import io.ktor.server.auth.Principal
import moe.polar.justchatting.services.unauthorized

inline fun <reified T : Principal> ApplicationCall.requirePrincipal(): T {
    return this.principal<T>() ?: unauthorized()
}
