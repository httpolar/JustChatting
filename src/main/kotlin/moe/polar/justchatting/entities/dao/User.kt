package moe.polar.justchatting.entities.dao

import java.util.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import moe.polar.justchatting.entities.tables.PasswordsTable
import moe.polar.justchatting.entities.tables.TokensTable
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.query
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(UsersTable)

    var username by UsersTable.name
    var createdAt by UsersTable.createdAt
    var power by UsersTable.power

    val password by Password referencedOn PasswordsTable.userId
    val tokens by Token referrersOn TokensTable.user
}

@Serializable
data class UserSerializable(
    val id: @Contextual UUID,
    val username: String,
    val createdAt: Instant,
    val power: Int
)

fun User.toSerializable() = UserSerializable(
    id.value,
    username,
    createdAt,
    power
)

suspend fun UUID.getUser(): User = query {
    User.find { UsersTable.id eq this@getUser }.single()
}

suspend fun Iterable<UUID>.getUsers(limit: Int = 500): List<User> = query {
    User.find { UsersTable.id inList this@getUsers }.limit(limit).toList()
}
