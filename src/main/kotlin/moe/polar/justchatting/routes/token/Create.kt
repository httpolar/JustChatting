package moe.polar.justchatting.routes.token

import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.resources.post
import io.ktor.server.response.respondText
import moe.polar.justchatting.entities.dao.Token
import moe.polar.justchatting.entities.tables.TokensTable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.extensions.requirePrincipal
import moe.polar.justchatting.plugins.AuthenticationType
import moe.polar.justchatting.principals.UserIdPrincipal
import moe.polar.justchatting.routes.token.resource.TokenResource
import moe.polar.justchatting.services.generateToken
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere


fun Route.createTokenRoute() {
    authenticate(AuthenticationType.FORM, strategy = AuthenticationStrategy.Required) {
        post<TokenResource> {
            val principal = call.requirePrincipal<UserIdPrincipal>()

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