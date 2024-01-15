package moe.polar.justchatting.routes.messages

import io.ktor.server.application.call
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import moe.polar.justchatting.entities.dao.Message
import moe.polar.justchatting.entities.dao.toSerializable
import moe.polar.justchatting.entities.tables.MessagesTable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.plugins.AuthenticationType
import moe.polar.justchatting.routes.messages.resource.MessagesRoute
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and


fun Route.readMessagesRoute() {
    authenticate(AuthenticationType.BEARER, strategy = AuthenticationStrategy.Required) {
        // Return last or that were sent before specified message/timestamp 1000 messages
        get<MessagesRoute> { param ->
            val messages = query {
                var beforeCondition: Op<Boolean> = Op.TRUE

                if (param.beforeTimestamp != null) {
                    beforeCondition = Op.build { MessagesTable.createdAt lessEq param.beforeTimestamp }
                }

                if (param.beforeMessageUuid != null) {
                    val targetMessage = Message
                        .find { MessagesTable.id eq param.beforeMessageUuid }
                        .limit(1)
                        .firstOrNull()

                    if (targetMessage != null) {
                        beforeCondition = Op.build { MessagesTable.createdAt lessEq targetMessage.createdAt }
                    }
                }


                val condition = Op.build { MessagesTable.room eq param.room and beforeCondition }
                val order = MessagesTable.createdAt to SortOrder.ASC

                Message.find { condition }.orderBy(order).map { it.toSerializable() }
            }

            call.respond(messages)
        }
    }
}
