package moe.polar.justchatting.services

import moe.polar.justchatting.entities.dao.Password
import moe.polar.justchatting.entities.dao.Token
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.entities.tables.TokensTable
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.query

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

/**
 * This will verify user's password by matching against stored password hash,
 * so if user provided an invalid password this function will return null.
 */
suspend fun getUserByIdAndPassword(username: String, rawPassword: String): User? {
    val (user, passHash) = query {
        val user = User.find { UsersTable.name eq username }.singleOrNull()
        val passHash = user?.password?.singleOrNull()?.hash

        user to passHash
    }

    if (user == null || passHash == null) {
        return null
    }

    if (bcryptMatch(rawPassword, passHash)) {
        return user
    }

    return null
}

suspend fun getUserByName(username: String): User? = query {
    User.find { UsersTable.name eq username }.singleOrNull()
}

suspend fun getUserByToken(token: String): User? = query {
    Token.find { TokensTable.raw eq token }.singleOrNull()?.user
}
