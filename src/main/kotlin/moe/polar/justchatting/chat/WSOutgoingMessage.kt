package moe.polar.justchatting.chat

import io.ktor.websocket.Frame
import java.util.UUID
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import moe.polar.justchatting.services.JsonSerializer

@Serializable
data class WSOutgoingMessage(
    val senderId: @Contextual UUID? = null,
    val room: UInt? = null,
    val content: String,
)

fun WSOutgoingMessage.toFrame(): Frame {
    return JsonSerializer.encodeToString(this).let { Frame.Text(it) }
}
