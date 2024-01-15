package moe.polar.justchatting.routes.users.id

import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import moe.polar.justchatting.entities.dao.getUser
import moe.polar.justchatting.entities.dao.toSerializable
import moe.polar.justchatting.plugins.AuthenticationType
import moe.polar.justchatting.routes.users.resource.UsersResource

fun Route.readUserById() {
    authenticate(AuthenticationType.BEARER, strategy = AuthenticationStrategy.Required) {
        get<UsersResource.Id> { param ->
            val target = param.id.getUser()

            call.respond(target.toSerializable())
        }
    }
}
