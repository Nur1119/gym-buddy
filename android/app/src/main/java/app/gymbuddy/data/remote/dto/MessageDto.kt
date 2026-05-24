package app.gymbuddy.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class MessageDto(
    val id: String,
    val matchId: String,
    val senderId: String,
    val kind: String = "text",
    val text: String? = null,
    val imageUrl: String? = null,
    val payload: JsonObject? = null,
    val createdAt: String,
    val readAt: String? = null,
)

@Serializable
data class MessageListResponse(
    val items: List<MessageDto>,
    val nextCursor: String? = null,
)

@Serializable
data class SendMessageRequest(
    val text: String? = null,
    val kind: String = "text",
    val payload: JsonObject? = null,
)

@Serializable
data class WorkoutInvitePayload(
    val date: String,
    val gymId: String? = null,
    val routineId: String? = null,
)

/** Real-time stream event from `WS /matches/:id/stream`. */
@Serializable
data class WsEvent(
    val type: String,
    val data: MessageDto? = null,
    val userId: String? = null,
    val messageId: String? = null,
)
