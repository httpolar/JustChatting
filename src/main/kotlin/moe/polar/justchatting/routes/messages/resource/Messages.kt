package moe.polar.justchatting.routes.messages.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/messages")
class MessagesRoute