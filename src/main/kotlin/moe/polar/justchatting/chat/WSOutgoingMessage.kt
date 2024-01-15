package moe.polar.justchatting.chat

import io.ktor.websocket.Frame
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import moe.polar.justchatting.entities.dao.UserSerializable
import moe.polar.justchatting.services.JsonSerializer
import java.util.UUID

@Serializable
data class WSOutgoingMessage(
    val id: @Contextual UUID? = null,
    val sender: UserSerializable? = null,
    val room: UInt? = null,
    val createdAt: Instant = Clock.System.now(),
    val content: String,
)

fun WSOutgoingMessage.toFrame(): Frame {
    return JsonSerializer.encodeToString(this).let { Frame.Text(it) }
}
