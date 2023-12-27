package moe.polar.justchatting.services

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import moe.polar.justchatting.serializers.UUIDSerializer

val JsonSerializer = Json {
    serializersModule = SerializersModule {
        contextual(UUIDSerializer)
    }
}
