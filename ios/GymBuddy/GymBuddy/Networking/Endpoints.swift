import Foundation

public enum HTTPMethod: String {
    case GET, POST, PUT, PATCH, DELETE
}

public struct Endpoint {
    public let method: HTTPMethod
    public let path: String

    // Auth
    public static let register = Endpoint(method: .POST, path: "/auth/register")
    public static let login    = Endpoint(method: .POST, path: "/auth/login")
    public static let refresh  = Endpoint(method: .POST, path: "/auth/refresh")
    public static let me       = Endpoint(method: .GET, path: "/auth/me")

    // Users
    public static let usersMe        = Endpoint(method: .GET, path: "/users/me")
    public static let usersMeUpdate  = Endpoint(method: .PATCH, path: "/users/me")
    public static let uploadPhoto    = Endpoint(method: .POST, path: "/users/me/photos")
    public static func deletePhoto(_ id: String) -> Endpoint {
        Endpoint(method: .DELETE, path: "/users/me/photos/\(id)")
    }
    public static func userById(_ id: String) -> Endpoint {
        Endpoint(method: .GET, path: "/users/\(id)")
    }

    // Discover
    public static func discoverFeed(limit: Int = 10) -> Endpoint {
        Endpoint(method: .GET, path: "/discover/feed?limit=\(limit)")
    }
    public static let discoverFilters     = Endpoint(method: .GET, path: "/discover/filters")
    public static let discoverFiltersPut  = Endpoint(method: .PUT, path: "/discover/filters")
    public static let discoverSwipe       = Endpoint(method: .POST, path: "/discover/swipe")
    public static let discoverRewind      = Endpoint(method: .POST, path: "/discover/rewind")
    public static let discoverBoost       = Endpoint(method: .POST, path: "/discover/boost")

    // Matches
    public static let matches             = Endpoint(method: .GET, path: "/matches")
    public static func unmatch(_ id: String) -> Endpoint {
        Endpoint(method: .DELETE, path: "/matches/\(id)")
    }

    // Chat
    public static func messages(matchId: String, limit: Int = 50) -> Endpoint {
        Endpoint(method: .GET, path: "/matches/\(matchId)/messages?limit=\(limit)")
    }
    public static func sendMessage(matchId: String) -> Endpoint {
        Endpoint(method: .POST, path: "/matches/\(matchId)/messages")
    }

    // Workouts
    public static let workouts        = Endpoint(method: .GET, path: "/workouts")
    public static let createWorkout   = Endpoint(method: .POST, path: "/workouts")
    public static func workout(_ id: String) -> Endpoint {
        Endpoint(method: .GET, path: "/workouts/\(id)")
    }
    public static func updateWorkout(_ id: String) -> Endpoint {
        Endpoint(method: .PATCH, path: "/workouts/\(id)")
    }
    public static func finishWorkout(_ id: String) -> Endpoint {
        Endpoint(method: .POST, path: "/workouts/\(id)/finish")
    }
    public static let workoutStats    = Endpoint(method: .GET, path: "/workouts/stats")

    // Exercises
    public static func exercises(muscle: String? = nil, search: String? = nil) -> Endpoint {
        var query: [String] = []
        if let m = muscle, !m.isEmpty, m != "all" { query.append("muscle=\(m)") }
        if let s = search, !s.isEmpty { query.append("search=\(s)") }
        let q = query.isEmpty ? "" : "?" + query.joined(separator: "&")
        return Endpoint(method: .GET, path: "/exercises\(q)")
    }
    public static let createExercise  = Endpoint(method: .POST, path: "/exercises")
    public static func exercise(_ id: String) -> Endpoint {
        Endpoint(method: .GET, path: "/exercises/\(id)")
    }

    // Routines
    public static let routines        = Endpoint(method: .GET, path: "/routines")
    public static let createRoutine   = Endpoint(method: .POST, path: "/routines")
    public static func routine(_ id: String) -> Endpoint {
        Endpoint(method: .GET, path: "/routines/\(id)")
    }

    // Friends
    public static let friends         = Endpoint(method: .GET, path: "/friends")
    public static let friendRequests  = Endpoint(method: .GET, path: "/friends/requests")
    public static let sendFriendRequest = Endpoint(method: .POST, path: "/friends/requests")

    // Leaderboard
    public static func leaderboard(scope: String = "friends", period: String = "week") -> Endpoint {
        Endpoint(method: .GET, path: "/leaderboard?scope=\(scope)&period=\(period)")
    }

    // Nutrition
    public static func nutritionDay(date: String) -> Endpoint {
        Endpoint(method: .GET, path: "/nutrition/day?date=\(date)")
    }
    public static let addMeal         = Endpoint(method: .POST, path: "/nutrition/meals")

    // Medals
    public static let medals          = Endpoint(method: .GET, path: "/medals")
    public static let quests          = Endpoint(method: .GET, path: "/quests")

    // Calendar
    public static func calendar(month: String) -> Endpoint {
        Endpoint(method: .GET, path: "/calendar?month=\(month)")
    }
}
