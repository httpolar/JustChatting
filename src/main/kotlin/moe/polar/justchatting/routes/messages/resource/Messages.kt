package moe.polar.justchatting.routes.messages.resource

import io.ktor.resources.Resource
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Resource("/messages")
class MessagesRoute(
    val room: UInt = 0u,
    val beforeTimestamp: Instant? = null,
    val beforeMessageUuid: @Contextual UUID? = null
)