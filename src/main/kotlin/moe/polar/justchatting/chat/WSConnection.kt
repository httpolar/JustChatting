package moe.polar.justchatting.chat

import io.ktor.websocket.DefaultWebSocketSession
import java.util.UUID

class WSConnection(
    val session: DefaultWebSocketSession,
    val userId: UUID,
    val room: UInt = 0u
)
