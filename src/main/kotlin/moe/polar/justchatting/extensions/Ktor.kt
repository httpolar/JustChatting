package moe.polar.justchatting.extensions

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.principal
import io.ktor.server.auth.Principal
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.principals.UserPrincipal
import moe.polar.justchatting.services.unauthorized

inline fun <reified T : Principal> ApplicationCall.requirePrincipal(): T =
    principal<T>() ?: unauthorized()

fun ApplicationCall.requireUser(): User =
    requirePrincipal<UserPrincipal>().user
