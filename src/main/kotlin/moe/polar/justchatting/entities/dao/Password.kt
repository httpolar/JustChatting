package moe.polar.justchatting.entities.dao

import java.util.UUID
import moe.polar.justchatting.entities.tables.PasswordsTable
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID

class Password(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : EntityClass<UUID, Password>(PasswordsTable)

    var user by User referencedOn PasswordsTable.userId
    var hash by PasswordsTable.hash
}
