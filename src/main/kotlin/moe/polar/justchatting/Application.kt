package moe.polar.justchatting

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import moe.polar.justchatting.plugins.configureHTTP
import moe.polar.justchatting.plugins.configureMonitoring
import moe.polar.justchatting.plugins.configureRouting
import moe.polar.justchatting.plugins.configureSecurity
import moe.polar.justchatting.plugins.configureSerialization
import moe.polar.justchatting.plugins.configureSockets
import moe.polar.justchatting.plugins.setupDatabase

suspend fun main() {
    setupDatabase()

    embeddedServer(CIO, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureHTTP()
    configureMonitoring()
    configureSockets()
    configureRouting()
}
