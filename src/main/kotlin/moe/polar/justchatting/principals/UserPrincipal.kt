package moe.polar.justchatting.principals

import io.ktor.server.auth.Principal
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.query

/**
 * [UserPrincipal] can not exist without [User] being present in the database
 */
class UserPrincipal(private var underlyingUser: User) : Principal {
    val user: User
        get() = underlyingUser

    /**
     * Will fetch new user data from the database and update principal's [user] field
     */
    suspend fun getUser(): User {
        val user = query { User.find { UsersTable.id eq underlyingUser.id }.single() }
        underlyingUser = user
        return underlyingUser
    }
}

