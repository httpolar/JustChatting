package moe.polar.justchatting.services

import moe.polar.justchatting.entities.dao.Password
import moe.polar.justchatting.entities.dao.Token
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.entities.tables.TokensTable
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.query
import java.util.UUID

/**
 * Creates a new user entry in a database
 *
 * @param username unique name of the user
 * @param password raw password string of a user (will be stored as hash)
 */
suspend fun createUser(username: String, password: String): User {
    val hashedPassword = password.bcryptHash()

    val createdUser = query {
        Password.new {
            this.hash = hashedPassword
            this.user = User.new {
                this.username = username
            }
        }

        User.find { UsersTable.name eq username }.single()
    }

    return createdUser
}

suspend fun getUserByToken(token: String): User? = query {
    Token.find { TokensTable.raw eq token }.singleOrNull()?.user
}

suspend fun getUserByName(username: String): User? = query {
    User.find { UsersTable.name eq username }.singleOrNull()
}

suspend fun getUsersByUUIDs(uuids: List<UUID>, limit: Int = 500): List<User> = query {
    User.find { UsersTable.id inList uuids }.limit(limit).toList()
}

suspend fun getUserByUUID(uuid: UUID): User = query {
    User.find { UsersTable.id eq uuid }.single()
}
