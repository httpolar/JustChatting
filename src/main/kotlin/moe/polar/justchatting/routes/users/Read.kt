package moe.polar.justchatting.routes.users

import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.entities.dao.toSerializable
import moe.polar.justchatting.entities.dao.getUsers
import moe.polar.justchatting.extensions.requireUser
import moe.polar.justchatting.plugins.AuthenticationType
import moe.polar.justchatting.routes.users.resource.UsersResource
import moe.polar.justchatting.services.badRequest
import java.util.UUID


@Serializable
private data class ReadBody(
    val ids: List<@Contextual UUID> = listOf(),
    val me: Boolean = false,
)

fun Route.readUsersRoute() {
    authenticate(AuthenticationType.BEARER, strategy = AuthenticationStrategy.Required) {
        get<UsersResource> {
            val me = call.requireUser()
            val body = call.receive<ReadBody>()

            if (body.ids.isEmpty() && !body.me) {
                badRequest("You must provide at least one in ids: [...] or get info about yourself via me: true!")
            }

            if (body.me) {
                call.respond(listOf(me.toSerializable()))
            } else {
                val users = body.ids.getUsers().map(User::toSerializable)
                call.respond(users)
            }
        }
    }
}
