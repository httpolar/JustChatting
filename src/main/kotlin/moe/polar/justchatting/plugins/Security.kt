package moe.polar.justchatting.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.bearer
import io.ktor.server.auth.form
import io.ktor.server.response.respondText
import moe.polar.justchatting.entities.dao.Token
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.entities.tables.TokensTable
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.principals.UserUuidPrincipal
import moe.polar.justchatting.services.bcryptMatch

object AuthenticationType {
    const val BEARER = "bearer-token"
    const val FORM = "user-pass"
}

fun Application.configureSecurity() {
    authentication {
        bearer(AuthenticationType.BEARER) {
            authenticate { credentials ->
                val user = query { Token.find { TokensTable.raw eq credentials.token }.firstOrNull()?.user }
                    ?: return@authenticate null

                UserUuidPrincipal(user.id.value)
            }
        }

        form(AuthenticationType.FORM) {
            userParamName = "username"
            passwordParamName = "password"

            validate { credentials ->
                val (user, password) = query {
                    val user = User.find { UsersTable.name eq credentials.name }.firstOrNull()
                    val password = user?.password?.firstOrNull()
                    user to password
                }

                if (user == null || password == null)
                    return@validate null

                val isMatching = bcryptMatch(credentials.password, password.hash)
                if (isMatching) {
                    UserUuidPrincipal(user.id.value)
                } else {
                    null
                }
            }

            challenge {
                call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)
            }
        }
    }
}
