import Foundation

public struct RoutineExercise: Codable, Identifiable, Hashable {
    public var id: String { exerciseId }
    public let exerciseId: String
    public var sets: Int
    public var targetReps: Int
    public var restSec: Int
}

public struct Routine: Codable, Identifiable, Hashable {
    public let id: String
    public var name: String
    public var exercises: [RoutineExercise]
    public var totalSets: Int
    public var estimatedDurationMin: Int
    public var color: String
    public var ownerId: String
    public let createdAt: String
}

public struct RoutineListResponse: Codable {
    public let items: [Routine]
}
