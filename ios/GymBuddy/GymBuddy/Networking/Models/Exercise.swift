import Foundation

public enum Muscle: String, Codable, CaseIterable, Identifiable {
    case chest = "Chest"
    case back = "Back"
    case legs = "Legs"
    case shoulders = "Shoulders"
    case arms = "Arms"
    case core = "Core"
    case cardio = "Cardio"
    public var id: String { rawValue }
}

public enum Equipment: String, Codable, CaseIterable, Identifiable {
    case barbell = "Barbell"
    case dumbbell = "Dumbbell"
    case machine = "Machine"
    case cable = "Cable"
    case bodyweight = "Bodyweight"
    public var id: String { rawValue }
}

public struct Exercise: Codable, Identifiable, Hashable {
    public let id: String
    public var name: String
    public var muscle: Muscle
    public var equipment: Equipment
    public var icon: String
    public var notes: String?
    public var isCustom: Bool
    public var ownerId: String?
}

public struct ExerciseListResponse: Codable {
    public let items: [Exercise]
    public let nextCursor: String?
}
