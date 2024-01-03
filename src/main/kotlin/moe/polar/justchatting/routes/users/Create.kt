package moe.polar.justchatting.routes.users

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import kotlinx.serialization.Serializable
import moe.polar.justchatting.entities.dao.toSerializable
import moe.polar.justchatting.routes.users.resource.UsersResource
import moe.polar.justchatting.services.getUserByName
import moe.polar.justchatting.services.badRequest
import moe.polar.justchatting.services.createUser


@Serializable
private data class CreateBody(
    val username: String,
    val password: String
)

fun Route.createUsersRoute() {
    post<UsersResource> {
        val body = call.receive<CreateBody>()

        val existingUser = getUserByName(body.username)
        if (existingUser != null) {
            badRequest("This username is already taken!")
        }

        val newUser = createUser(body.username, body.password)
        call.respond(newUser.toSerializable())
    }
}
