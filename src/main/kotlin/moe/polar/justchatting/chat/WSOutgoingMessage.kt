package moe.polar.justchatting.chat

import io.ktor.websocket.Frame
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import moe.polar.justchatting.entities.dao.UserSerializable
import moe.polar.justchatting.services.JsonSerializer

@Serializable
data class WSOutgoingMessage(
    val user: UserSerializable? = null,
    val room: UInt? = null,
    val content: String,
)

fun WSOutgoingMessage.toFrame(): Frame {
    return JsonSerializer.encodeToString(this).let { Frame.Text(it) }
}
