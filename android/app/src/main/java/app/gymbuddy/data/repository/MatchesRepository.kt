package app.gymbuddy.data.repository

import app.gymbuddy.data.remote.ApiService
import app.gymbuddy.data.remote.WebSocketClient
import app.gymbuddy.data.remote.dto.MatchDto
import app.gymbuddy.data.remote.dto.MessageDto
import app.gymbuddy.data.remote.dto.SendMessageRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchesRepository @Inject constructor(
    private val api: ApiService,
    private val webSocket: WebSocketClient,
) {
    val webSocketEvents get() = webSocket.events

    suspend fun matches(): Result<List<MatchDto>> = runCatching {
        api.matches().items
    }

    suspend fun unmatch(id: String): Result<Unit> = runCatching {
        api.unmatch(id)
    }

    suspend fun messages(matchId: String, before: String? = null): Result<List<MessageDto>> = runCatching {
        api.messages(matchId, before = before).items
    }

    suspend fun sendMessage(matchId: String, text: String): Result<MessageDto> = runCatching {
        api.sendMessage(matchId, SendMessageRequest(text = text, kind = "text"))
    }

    fun connectStream(matchId: String, scope: kotlinx.coroutines.CoroutineScope) =
        webSocket.connect(matchId, scope)

    fun disconnectStream() = webSocket.disconnect()
}
