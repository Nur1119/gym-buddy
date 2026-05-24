import Foundation

public struct Match: Codable, Identifiable, Hashable {
    public let id: String
    public let user: User
    public let matchedAt: String
    public let lastMessage: Message?
    public let unreadCount: Int
    public let isUnmatched: Bool
}

public struct MatchListResponse: Codable {
    public let items: [Match]
}

public struct SwipeRequest: Codable {
    public enum Direction: String, Codable {
        case like, pass, superlike
    }
    public let targetUserId: String
    public let direction: Direction
}

public struct SwipeResponse: Codable {
    public let matched: Bool
    public let match: Match?
}

public struct DiscoverFeed: Codable {
    public let items: [User]
}

public struct DiscoverFilters: Codable {
    public var ageMin: Int
    public var ageMax: Int
    public var maxDistanceKm: Int
    public var goals: [String]
    public var levels: [String]
    public var scheduleDays: [Int]

    public static let `default` = DiscoverFilters(
        ageMin: 21, ageMax: 35, maxDistanceKm: 15,
        goals: ["Strength", "Hypertrophy"],
        levels: ["Intermediate", "Advanced"],
        scheduleDays: [1,2,3,4,5]
    )
}
