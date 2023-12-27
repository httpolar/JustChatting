package moe.polar.justchatting.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.AuthenticationStrategy
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.Collections
import moe.polar.justchatting.chat.WSConnection
import moe.polar.justchatting.chat.WSOutgoingMessage
import moe.polar.justchatting.chat.toFrame
import moe.polar.justchatting.entities.dao.Message
import moe.polar.justchatting.entities.dao.User
import moe.polar.justchatting.entities.tables.UsersTable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.principals.UserUuidPrincipal


val connections: MutableSet<WSConnection> = Collections.synchronizedSet(LinkedHashSet())

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }


    routing {
        authenticate(AuthenticationType.BEARER, strategy = AuthenticationStrategy.Required) {
            webSocket("/chat") {
                val paramRoom = call.parameters["room"]?.toUInt() ?: 0u

                val principal = call.principal<UserUuidPrincipal>()
                    ?: return@webSocket close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Unauthorized"))

                val thisConnection = WSConnection(this, principal, paramRoom)
                connections += thisConnection

                try {
                    send(WSOutgoingMessage(content = "You are connected to room ${thisConnection.room}! There are ${connections.count { it.room == thisConnection.room }} users here.").toFrame())

                    for (frame in incoming) {
                        if (frame !is Frame.Text) {
                            continue
                        }

                        val receivedText = frame.readText()
                        if (receivedText.length > 6000) {
                            thisConnection.session.send(WSOutgoingMessage(content = "Message is too long! It must not exceed 6000 characters.").toFrame())
                            continue
                        }

                        val message = query {
                            val user = User.find { UsersTable.id eq thisConnection.principal.uuid }.limit(1).first()

                            val message = Message.new {
                                sender = user
                                room = thisConnection.room
                                content = receivedText
                            }

                            WSOutgoingMessage(message.sender?.id?.value, message.room, message.content)
                        }

                        for (connection in connections) {
                            if (connection.room == message.room) {
                                connection.session.send(message.toFrame())
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    connections -= thisConnection
                }
            }
        }
    }
}
