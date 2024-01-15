package moe.polar.justchatting.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.datetime.Clock
import java.time.Duration
import java.util.Collections
import moe.polar.justchatting.chat.WSConnection
import moe.polar.justchatting.chat.WSOutgoingMessage
import moe.polar.justchatting.chat.toFrame
import moe.polar.justchatting.entities.dao.Message
import moe.polar.justchatting.entities.dao.getUser
import moe.polar.justchatting.entities.dao.toSerializable
import moe.polar.justchatting.extensions.query
import moe.polar.justchatting.services.badRequest
import moe.polar.justchatting.services.getUserByToken
import moe.polar.justchatting.services.unauthorized


private val connections: MutableSet<WSConnection> = Collections.synchronizedSet(LinkedHashSet())

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/chat") {
            val paramRoom = call.parameters["room"]?.toUInt() ?: 0u

            /** We must handle authorization manually, because browser WebSocket doesn't support headers */
            val token = call.parameters["token"]
                ?: return@webSocket close(CloseReason(CloseReason.Codes.NORMAL, "Token query parameter is missing!"))

            val user = getUserByToken(token)
                ?: return@webSocket close(CloseReason(CloseReason.Codes.NORMAL, "User not found with provided token!"))

            val thisConnection = WSConnection(this, user.id.value, paramRoom)
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
                        val user = thisConnection.userId.getUser()
                        val now = Clock.System.now()

                        val message = Message.new {
                            sender = user
                            room = thisConnection.room
                            content = receivedText
                            createdAt = now
                        }

                        WSOutgoingMessage(
                            message.id.value,
                            message.sender?.toSerializable(),
                            message.room,
                            message.createdAt,
                            message.content
                        )
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
