package moe.polar.justchatting.chat

import io.ktor.websocket.DefaultWebSocketSession
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.principals.UserUuidPrincipal

class WSConnection(val session: DefaultWebSocketSession, val principal: UserUuidPrincipal, val room: UInt = 0u) {
    suspend fun toUser(): User = principal.toUser()
}
