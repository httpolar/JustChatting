package moe.polar.justchatting.routes

import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.routing.Route
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import java.util.UUID
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import moe.polar.justchatting.entities.dao.Message
import moe.polar.justchatting.entities.dao.toSerializable
import moe.polar.justchatting.entities.tables.MessagesTable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.plugins.AuthenticationType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and

@Serializable
@Resource("/messages")
class MessagesRoute

@Serializable
data class GetMessagesBody(
    val room: UInt = 0u,
    val beforeTimestamp: Instant? = null,
    val beforeMessageUuid: @Contextual UUID? = null
)

fun Route.configureMessagesRoute() {
    authenticate(AuthenticationType.BEARER, strategy = AuthenticationStrategy.Required) {
        // Return last or that were sent before specified message/timestamp 1000 messages
        get<MessagesRoute> {
            val body = runCatching { call.receiveNullable<GetMessagesBody>() }.getOrNull() ?: GetMessagesBody()

            val messages = query {
                var beforeCondition: Op<Boolean> = Op.TRUE

                if (body.beforeTimestamp != null) {
                    beforeCondition = Op.build { MessagesTable.createdAt lessEq body.beforeTimestamp }
                }

                if (body.beforeMessageUuid != null) {
                    val targetMessage = Message
                        .find { MessagesTable.id eq body.beforeMessageUuid }
                        .limit(1)
                        .firstOrNull()

                    if (targetMessage != null) {
                        beforeCondition = Op.build { MessagesTable.createdAt lessEq targetMessage.createdAt }
                    }
                }


                val condition = Op.build { MessagesTable.room eq body.room and beforeCondition }
                val order = MessagesTable.createdAt to SortOrder.DESC

                Message.find { condition }.orderBy(order).map { it.toSerializable() }
            }

            call.respond(messages)
        }
    }
}
