package moe.polar.justchatting.routes

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import moe.polar.justchatting.routes.messages.readMessagesRoute
import moe.polar.justchatting.routes.token.createTokenRoute
import moe.polar.justchatting.routes.token.deleteTokenRoute
import moe.polar.justchatting.routes.users.createUsersRoute
import moe.polar.justchatting.routes.users.readUsersRoute

fun Application.configureUsersRoutes() {
    routing {
        createUsersRoute()
        readUsersRoute()
    }
}

fun Application.configureTokenRoutes() {
    routing {
        createTokenRoute()
        deleteTokenRoute()
    }
}

fun Application.configureMessagesRoutes() {
    routing {
        readMessagesRoute()
    }
}
