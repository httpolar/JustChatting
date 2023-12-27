package moe.polar.justchatting.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import moe.polar.justchatting.entities.dao.Token
import moe.polar.justchatting.entities.tables.TokensTable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.plugins.AuthenticationType
import moe.polar.justchatting.principals.UserUuidPrincipal
import moe.polar.justchatting.services.generateToken
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere


fun Route.configureTokenRoute() {
    authenticate(AuthenticationType.FORM, strategy = AuthenticationStrategy.Required) {
        post("/token") {
            val principal = call.principal<UserUuidPrincipal>()
                ?: return@post call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)

            val tokenHolder = principal.toUser()
            val rawToken = generateToken()

            val token = query {
                // we should delete all existing tokens whenever user requests new token
                TokensTable.deleteWhere { user eq tokenHolder.id }

                Token.new {
                    raw = rawToken
                    user = tokenHolder
                }
            }

            call.respondText(token.raw)
        }
    }
}
