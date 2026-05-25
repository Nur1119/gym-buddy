package app.gymbuddy.fcm

import android.util.Log
import app.gymbuddy.data.local.TokenStore
import app.gymbuddy.data.remote.ApiService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import app.gymbuddy.util.NotificationHelper

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {

    @Inject lateinit var api: ApiService
    @Inject lateinit var tokenStore: TokenStore

    override fun onNewToken(fcmToken: String) {
        // Upload new FCM token to backend whenever it refreshes
        CoroutineScope(Dispatchers.IO).launch {
            runCatching { api.updateFcmToken(FcmTokenRequest(fcmToken)) }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: return
        val body  = message.notification?.body  ?: message.data["body"]  ?: return
        val type  = message.data["type"] ?: "message"

        val helper = NotificationHelper()
        when (type) {
            "match"  -> helper.showMatch(this, title)
            "friend" -> helper.showFriendRequest(this, body)
            else     -> helper.showMessage(this, title, body)
        }
    }
}

data class FcmTokenRequest(val fcmToken: String)
