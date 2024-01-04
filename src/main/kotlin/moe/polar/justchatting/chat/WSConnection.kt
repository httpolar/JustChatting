package moe.polar.justchatting.chat

import io.ktor.websocket.DefaultWebSocketSession
import moe.polar.justchatting.entities.dao.User

class WSConnection(
    val session: DefaultWebSocketSession,
    val user: User,
    val room: UInt = 0u
)
