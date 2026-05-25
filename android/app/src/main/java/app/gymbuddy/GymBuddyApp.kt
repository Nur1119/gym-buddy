package app.gymbuddy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GymBuddyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        app.gymbuddy.util.NotificationHelper.createChannels(this)
    }
}
