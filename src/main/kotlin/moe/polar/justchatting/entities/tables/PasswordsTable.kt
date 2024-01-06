package moe.polar.justchatting.entities.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object PasswordsTable : UUIDTable() {
    val userId = reference("user_id", UsersTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE).uniqueIndex()
    val hash = text("hash")
}
