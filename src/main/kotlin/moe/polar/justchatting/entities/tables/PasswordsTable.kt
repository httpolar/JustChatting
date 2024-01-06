package moe.polar.justchatting.entities.tables

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.UUID

object PasswordsTable : IdTable<UUID>() {
    val userId = reference("user_id", UsersTable, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val hash = text("hash")

    override val primaryKey = PrimaryKey(userId)
    override val id = userId
}
