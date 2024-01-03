package moe.polar.justchatting.routes

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import moe.polar.justchatting.routes.token.createTokenRoute
import moe.polar.justchatting.routes.users.createUsersRoute
import moe.polar.justchatting.routes.users.readUsersRoute

fun Application.installUsersRoutes() {
    routing {
        createUsersRoute()
        readUsersRoute()
    }
}

fun Application.installTokenRoutes() {
    routing {
        createTokenRoute()
    }
}
