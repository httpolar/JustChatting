package moe.polar.justchatting.entities.dao

import java.util.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import moe.polar.justchatting.entities.tables.PasswordsTable
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.query
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(UsersTable)

    var username by UsersTable.name
    var powerLevel by UsersTable.powerLevel
    var createdAt by UsersTable.createdAt
    var seenAt by UsersTable.seenAt

    val password by Password optionalReferrersOn PasswordsTable.user
}

@Serializable
data class UserSerializable(
    val id: @Contextual UUID,
    val username: String,
    val powerLevel: Int,
    val createdAt: Instant,
    val seenAt: Instant?
)

fun User.toSerializable() = UserSerializable(
    id.value,
    username,
    powerLevel,
    createdAt,
    seenAt
)

suspend fun UUID.getUser(): User = query {
    User.find { UsersTable.id eq this@getUser }.single()
}

suspend fun Iterable<UUID>.getUsers(limit: Int = 500): List<User> = query {
    User.find { UsersTable.id inList this@getUsers }.limit(limit).toList()
}
