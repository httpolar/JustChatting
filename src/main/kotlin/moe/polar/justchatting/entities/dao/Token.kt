package moe.polar.justchatting.entities.dao

import java.util.UUID
import moe.polar.justchatting.entities.tables.TokensTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Token(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Token>(TokensTable)

    var user by User referencedOn TokensTable.userId
    var raw by TokensTable.raw
    val createdAt by TokensTable.createdAt
    var expiresArray by TokensTable.expiresAt
}
