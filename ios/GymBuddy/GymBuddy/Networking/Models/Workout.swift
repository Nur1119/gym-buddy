import Foundation

public struct WorkoutSet: Codable, Hashable {
    public var weight: Double
    public var reps: Int
    public var completed: Bool
    public var restSec: Int

    public init(weight: Double, reps: Int, completed: Bool = false, restSec: Int = 90) {
        self.weight = weight; self.reps = reps; self.completed = completed; self.restSec = restSec
    }
}

public struct WorkoutExercise: Codable, Identifiable, Hashable {
    public var id: String { exerciseId }
    public let exerciseId: String
    public var sets: [WorkoutSet]
}

public struct Workout: Codable, Identifiable, Hashable {
    public let id: String
    public var name: String
    public var startedAt: String?
    public var finishedAt: String?
    public var plannedFor: String?
    public var routineId: String?
    public var exercises: [WorkoutExercise]
    public var totalVolumeKg: Double
    public var xpAwarded: Int
}

public struct WorkoutListResponse: Codable {
    public let items: [Workout]
}

public struct WorkoutStats: Codable {
    public let weeklyVolume: Double
    public let workoutsThisWeek: Int
    public let muscleHeatmap: [String: Double]
}
