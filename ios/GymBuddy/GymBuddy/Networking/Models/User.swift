import Foundation

public enum Goal: String, Codable, CaseIterable, Identifiable {
    case strength = "Strength"
    case hypertrophy = "Hypertrophy"
    case mobility = "Mobility"
    case calisthenics = "Calisthenics"
    case crossFit = "CrossFit"
    case cardio = "Cardio"
    public var id: String { rawValue }
}

public enum Level: String, Codable, CaseIterable, Identifiable {
    case beginner = "Beginner"
    case intermediate = "Intermediate"
    case advanced = "Advanced"
    case elite = "Elite"
    public var id: String { rawValue }
}

public struct UserPhoto: Codable, Identifiable, Hashable {
    public let id: String
    public let url: String
    public let position: Int
}

public struct UserStats: Codable, Hashable {
    public let level: Int
    public let xp: Int
    public let xpToNext: Int
    public let totalXp: Int
    public let streak: Int
    public let bestStreak: Int
    public let coins: Int
    public let totalWorkouts: Int
    public let workoutsThisWeek: Int

    public static let preview = UserStats(
        level: 79, xp: 3854, xpToNext: 5213, totalXp: 158913,
        streak: 125, bestStreak: 125, coins: 3099,
        totalWorkouts: 412, workoutsThisWeek: 4
    )
}

public struct User: Codable, Identifiable, Hashable {
    public let id: String
    public let email: String
    public var name: String
    public var username: String
    public var age: Int
    public var height: Int
    public var weight: Int
    public var bio: String
    public var goal: Goal
    public var level: Level
    public var gymName: String?
    public var gymLat: Double?
    public var gymLng: Double?
    public var schedule: [Int]
    public var interests: [String]
    public var photos: [UserPhoto]
    public var stats: UserStats
    public let createdAt: String

    public static let preview = User(
        id: "me-1",
        email: "alex@example.com",
        name: "Alex",
        username: "@alex_lifts",
        age: 28,
        height: 181,
        weight: 82,
        bio: "Powerbuilder. Coffee fueled. Always down for legs.",
        goal: .hypertrophy,
        level: .advanced,
        gymName: "Iron Temple",
        gymLat: nil,
        gymLng: nil,
        schedule: [1,2,3,4,5],
        interests: ["Powerlifting", "Bodybuilding", "Coffee"],
        photos: [],
        stats: .preview,
        createdAt: "2024-01-01T00:00:00Z"
    )
}

public struct AuthResponse: Codable {
    public let token: String
    public let user: User
}
