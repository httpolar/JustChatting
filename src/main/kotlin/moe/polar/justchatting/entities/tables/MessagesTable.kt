package moe.polar.justchatting.entities.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object MessagesTable : UUIDTable() {
    val sender = optReference(
        "sender_id",
        UsersTable,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.SET_NULL,
    )
    val isGlobal = bool("is_global").default(false)
    val room = uinteger("room")
    val content = varchar("content", 6000)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
}
