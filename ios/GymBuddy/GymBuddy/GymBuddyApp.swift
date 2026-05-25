import SwiftUI

@main
struct GymBuddyApp: App {
    @StateObject private var themeManager = ThemeManager()
    @StateObject private var auth = AuthViewModel()

    var body: some Scene {
        WindowGroup {
            RootView()
                .environmentObject(themeManager)
                .environmentObject(auth)
                .onAppear { NotificationManager.shared.requestPermission() }
        }
    }
}
