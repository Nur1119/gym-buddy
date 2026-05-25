import Foundation
import UserNotifications

@MainActor
public final class NotificationManager {
    public static let shared = NotificationManager()
    private init() {}

    public func requestPermission() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound, .badge]) { _, _ in }
    }

    public func showMessage(from senderName: String, text: String) {
        post(id: UUID().uuidString,
             title: senderName,
             body: text,
             categoryId: "message")
    }

    public func showMatch(with name: String) {
        post(id: "match_\(name)",
             title: "New Match! 🏋️",
             body: "You matched with \(name)",
             categoryId: "match")
    }

    public func showFriendRequest(from name: String) {
        post(id: "friend_\(name)",
             title: "Friend Request",
             body: "\(name) wants to be your gym buddy",
             categoryId: "friend")
    }

    private func post(id: String, title: String, body: String, categoryId: String) {
        let content = UNMutableNotificationContent()
        content.title = title
        content.body = body
        content.sound = .default
        content.categoryIdentifier = categoryId
        let request = UNNotificationRequest(
            identifier: id,
            content: content,
            trigger: nil // deliver immediately
        )
        UNUserNotificationCenter.current().add(request)
    }
}
