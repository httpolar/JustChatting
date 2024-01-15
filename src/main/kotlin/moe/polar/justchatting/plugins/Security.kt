package moe.polar.justchatting.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.bearer
import io.ktor.server.auth.form
import io.ktor.server.response.respondText
import moe.polar.justchatting.principals.UserIdPrincipal
import moe.polar.justchatting.services.getUserByIdAndPassword
import moe.polar.justchatting.services.getUserByToken

object AuthenticationType {
    const val BEARER = "bearer-token"
    const val FORM = "user-pass"
}

fun Application.configureSecurity() {
    authentication {
        bearer(AuthenticationType.BEARER) {
            authenticate { credentials ->
                val user = getUserByToken(credentials.token)

                if (user == null) {
                    null
                } else {
                    UserIdPrincipal(user.id.value, credentials.token)
                }
            }
        }

        form(AuthenticationType.FORM) {
            userParamName = "username"
            passwordParamName = "password"

            validate { credentials ->
                val user = getUserByIdAndPassword(credentials.name, credentials.password)

                if (user == null) {
                    null
                } else {
                    UserIdPrincipal(user.id.value)
                }
            }

            challenge {
                call.respondText("Unauthorized", status = HttpStatusCode.Unauthorized)
            }
        }
    }
}
