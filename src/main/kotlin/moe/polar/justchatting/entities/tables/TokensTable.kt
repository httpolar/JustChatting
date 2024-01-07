package moe.polar.justchatting.entities.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TokensTable : UUIDTable() {
    val userId = reference(
        "user_id",
        UsersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    val raw = text("raw")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
    val expiresAt = timestamp("expires_at").nullable()
}
