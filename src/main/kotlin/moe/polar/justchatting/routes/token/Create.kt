package moe.polar.justchatting.routes.token

import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable
import moe.polar.justchatting.entities.dao.Token
import moe.polar.justchatting.entities.dao.UserSerializable
import moe.polar.justchatting.entities.dao.toSerializable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.extensions.requireUser
import moe.polar.justchatting.plugins.AuthenticationType
import moe.polar.justchatting.routes.token.resource.TokenResource
import moe.polar.justchatting.services.generateToken

@Serializable
private data class NewTokenBody(
    val user: UserSerializable,
    val token: String,
)

fun Route.createTokenRoute() {
    authenticate(AuthenticationType.FORM, strategy = AuthenticationStrategy.Required) {
        post<TokenResource> {
            val tokenHolder = call.requireUser()
            val rawToken = generateToken()

            val token = query {
                Token.new {
                    raw = rawToken
                    user = tokenHolder
                }
            }

            val body = NewTokenBody(tokenHolder.toSerializable(), token.raw)

            call.respond(body)
        }
    }
}
