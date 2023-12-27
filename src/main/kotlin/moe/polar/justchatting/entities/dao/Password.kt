package moe.polar.justchatting.entities.dao

import java.util.UUID
import moe.polar.justchatting.entities.tables.PasswordsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Password(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Password>(PasswordsTable)

    var user by User optionalReferencedOn PasswordsTable.user
    var hash by PasswordsTable.hash
}
