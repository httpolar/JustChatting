package moe.polar.justchatting.routes.token

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.resources.delete
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import moe.polar.justchatting.entities.dao.Token
import moe.polar.justchatting.entities.tables.TokensTable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.extensions.requireUser
import moe.polar.justchatting.plugins.AuthenticationType
import moe.polar.justchatting.routes.token.resource.TokenResource

/**
 * This route will delete all tokens associated
 * with currently authenticated user
 */
fun Route.deleteTokenRoute() {
    authenticate(AuthenticationType.BEARER, strategy = AuthenticationStrategy.Required) {
        delete<TokenResource> {
            val user = call.requireUser()

            query {
                Token.find { TokensTable.userId eq user.id }.forEach(Token::delete)
            }

            call.respond(HttpStatusCode.OK)
        }
    }
}
