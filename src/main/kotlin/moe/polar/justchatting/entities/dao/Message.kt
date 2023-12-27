package moe.polar.justchatting.entities.dao

import io.ktor.websocket.Frame
import java.util.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import moe.polar.justchatting.entities.tables.MessagesTable
import moe.polar.justchatting.services.JsonSerializer
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Message(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Message>(MessagesTable)

    var sender by User optionalReferencedOn MessagesTable.sender
    var isGlobal by MessagesTable.isGlobal
    var room by MessagesTable.room
    var content by MessagesTable.content
    var createdAt by MessagesTable.createdAt
}

@Serializable
data class MessageSerializable(
    val id: @Contextual UUID,
    val sender: UserSerializable?,
    val isGlobal: Boolean,
    val room: UInt,
    val content: String,
    val createdAt: Instant,
)

fun Message.toSerializable(): MessageSerializable {
    return MessageSerializable(
        id.value,
        sender?.toSerializable(),
        isGlobal,
        room,
        content,
        createdAt,
    )
}

fun MessageSerializable.toFrame(): Frame {
    return JsonSerializer.encodeToString(this).let { Frame.Text(it) }
}
