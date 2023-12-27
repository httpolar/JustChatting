package moe.polar.justchatting.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import moe.polar.justchatting.services.JsonSerializer


fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(JsonSerializer)
    }
}
