package moe.polar.justchatting.routes

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import java.util.UUID
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import moe.polar.justchatting.entities.dao.Password
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.entities.dao.toSerializable
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.services.bcryptHash
import org.jetbrains.exposed.sql.mapLazy

@Serializable
@Resource("/user")
class UserRoute()

@Serializable
data class NewUserData(val username: String, val rawPassword: String)

@Serializable
data class FindUsersData(val ids: @Contextual List<@Contextual UUID>)

fun Route.configureUserRoutes() {
    get<UserRoute> {
        val findUsersData = call.receive<FindUsersData>()
        if (findUsersData.ids.isEmpty()) {
            return@get call.respondText("List of IDs must not be empty", status = HttpStatusCode.BadRequest)
        }

        val serializableUsers = query {
            User.find { UsersTable.id inList findUsersData.ids }
                .limit(500)
                .mapLazy(User::toSerializable)
                .toList()
        }

        call.respond(serializableUsers)
    }

    post<UserRoute> {
        val newUserData = call.receive<NewUserData>()

        if (!newUserData.username.matches("""^[a-zA-Z_\d]{2,16}$""".toRegex())) {
            return@post call.respondText(
                "Usernames must be 2-16 latin characters and allowed to contain numbers and _",
                status = HttpStatusCode.BadRequest
            )
        }

        val existingUser = query {
            User.find { UsersTable.name eq newUserData.username }.limit(1).firstOrNull()
        }

        if (existingUser != null) {
            return@post call.respondText(
                "This username is already taken, please try a different one!",
                status = HttpStatusCode.BadRequest
            )
        }

        if (newUserData.rawPassword.length !in 6..72) {
            return@post call.respondText(
                "Passwords must be 6-72 characters in length",
                status = HttpStatusCode.BadRequest
            )
        }

        val hashedPassword = newUserData.rawPassword.bcryptHash()
        val user = query {
            val password = Password.new {
                hash = hashedPassword
                user = User.new {
                    username = newUserData.username
                }
            }

            password.user
        }

        if (user == null) {
            return@post call.respondText(
                "Something went wrong while creating your user account, please, try again.",
                status = HttpStatusCode.InternalServerError
            )
        }

        call.respond(HttpStatusCode.OK, user.toSerializable())
    }
}