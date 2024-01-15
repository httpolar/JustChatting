package moe.polar.justchatting.routes.users.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Resource("/users")
class UsersResource {
    @Serializable
    @Resource("@me")
    class Me(val parent: UsersResource = UsersResource())

    @Serializable
    @Resource("{id}")
    class Id(val parent: UsersResource = UsersResource(), val id: @Contextual UUID)
}
