package moe.polar.justchatting.routes.users.me

import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import moe.polar.justchatting.entities.dao.toSerializable
import moe.polar.justchatting.extensions.requireUser
import moe.polar.justchatting.plugins.AuthenticationType
import moe.polar.justchatting.routes.users.resource.UsersResource

fun Route.readUserMe() {
    authenticate(AuthenticationType.BEARER, strategy = AuthenticationStrategy.Required) {
        get<UsersResource.Me> {
            val user = call.requireUser()
            call.respond(user.toSerializable())
        }
    }
}
