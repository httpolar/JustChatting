package moe.polar.justchatting.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import moe.polar.justchatting.routes.configureMessagesRoute
import moe.polar.justchatting.routes.configureTokenRoute
import moe.polar.justchatting.routes.configureUserRoutes

fun Application.configureRouting() {
    install(AutoHeadResponse)
    install(DoubleReceive)
    install(Resources)

    install(StatusPages) {
        exception<StatusCodeError> { call, cause ->
            call.respond(cause.code, ErrorResponse(cause.message))
        }

        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
            cause.printStackTrace()
        }
    }

    routing {
        get("/") {
            call.respondText("Hello!")
        }
    }

    routing {
        configureUserRoutes()
        configureTokenRoute()
        configureMessagesRoute()
    }
}
