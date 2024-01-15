package moe.polar.justchatting.principals

import io.ktor.server.auth.Principal
import java.util.UUID

class UserIdPrincipal(val id: UUID, val rawToken: String? = null) : Principal
