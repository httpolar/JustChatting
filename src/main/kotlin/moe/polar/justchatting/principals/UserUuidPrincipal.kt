package moe.polar.justchatting.principals

import io.ktor.server.auth.Principal
import java.util.UUID
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.query

class UserUuidPrincipal(val uuid: UUID) : Principal {
    suspend fun toUser() = query {
        User.find { UsersTable.id eq uuid }.limit(1).first()
    }
}
