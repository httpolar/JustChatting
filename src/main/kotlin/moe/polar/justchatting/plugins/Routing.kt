package moe.polar.justchatting.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.resources.Resources
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import moe.polar.justchatting.errors.responses.StatusCodeError
import moe.polar.justchatting.routes.configureMessagesRoutes
import moe.polar.justchatting.routes.configureTokenRoutes
import moe.polar.justchatting.routes.configureUsersRoutes

fun Application.configureRouting() {
    install(AutoHeadResponse)
    install(DoubleReceive)
    install(Resources)

    install(StatusPages) {
        exception<StatusCodeError> { call, cause ->
            val body = buildJsonObject {
                put("error", true)
                put("message", cause.message)

            }

            call.respond(cause.code, body)
        }

        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            cause.printStackTrace()
        }
    }

    configureUsersRoutes()
    configureTokenRoutes()
    configureMessagesRoutes()
}
