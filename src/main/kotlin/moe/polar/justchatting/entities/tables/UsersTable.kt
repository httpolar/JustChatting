package moe.polar.justchatting.entities.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object UsersTable : UUIDTable() {
    val name = text("username").uniqueIndex()
    val powerLevel = integer("power_level").default(0)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp())
    val seenAt = timestamp("seen_at").nullable()
}
