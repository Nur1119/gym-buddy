package app.gymbuddy.data.remote

import app.gymbuddy.BuildConfig
import app.gymbuddy.data.local.TokenStore
import app.gymbuddy.data.remote.dto.WsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.math.pow

/**
 * Lightweight WebSocket client for the chat stream.
 *
 * Usage:
 * - call [connect(matchId)] to open the socket for a match
 * - observe [events] for incoming events
 * - call [send] to push a text frame
 * - call [disconnect] when leaving the screen
 *
 * Reconnects on unexpected disconnect with exponential backoff (1s, 2s, 4s, 8s, max 30s).
 */
@Singleton
class WebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val tokenStore: TokenStore,
) {
    private val json: Json = Json { ignoreUnknownKeys = true }

    private val _events = MutableSharedFlow<WsEvent>(replay = 0, extraBufferCapacity = 64)
    val events: SharedFlow<WsEvent> = _events.asSharedFlow()

    private var socket: WebSocket? = null
    private var connectJob: Job? = null
    private var currentMatchId: String? = null
    private var attempt = 0
    private var manuallyClosed = false

    fun connect(matchId: String, scope: CoroutineScope) {
        if (currentMatchId == matchId && socket != null) return
        disconnect()
        currentMatchId = matchId
        manuallyClosed = false
        connectJob = scope.launch(Dispatchers.IO) { openSocket(matchId) }
    }

    private suspend fun openSocket(matchId: String) {
        if (manuallyClosed) return
        val token = tokenStore.tokenFlow.first().orEmpty()
        val url = "${BuildConfig.WS_BASE_URL}/matches/$matchId/stream"
        val request = Request.Builder()
            .url(url)
            .apply { if (token.isNotBlank()) addHeader("Authorization", "Bearer $token") }
            .build()
        socket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                attempt = 0
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                runCatching { json.decodeFromString(WsEvent.serializer(), text) }
                    .onSuccess { _events.tryEmit(it) }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                scheduleReconnect(matchId)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                if (!manuallyClosed) scheduleReconnect(matchId)
            }
        })
    }

    private fun scheduleReconnect(matchId: String) {
        if (manuallyClosed) return
        attempt = min(attempt + 1, 6)
        val delayMs = (1000L * 2.0.pow(attempt - 1).toLong()).coerceAtMost(30_000L)
        connectJob = CoroutineScope(Dispatchers.IO).launch {
            delay(delayMs)
            openSocket(matchId)
        }
    }

    fun send(payload: String): Boolean = socket?.send(payload) ?: false

    fun disconnect() {
        manuallyClosed = true
        socket?.close(1000, "client closed")
        socket = null
        connectJob?.cancel()
        connectJob = null
        currentMatchId = null
        attempt = 0
    }
}
