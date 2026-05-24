import Foundation
import SwiftUI

@MainActor
public final class WorkoutViewModel: ObservableObject {
    @Published public var routines: [Routine] = []
    @Published public var workouts: [Workout] = []
    @Published public var stats: WorkoutStats?
    @Published public var isLoading = false

    public init() {}

    public func load() async {
        isLoading = true; defer { isLoading = false }
        async let r = try? await APIClient.shared.routines()
        async let w = try? await APIClient.shared.workouts()
        async let s = try? await APIClient.shared.workoutStats()
        routines = (await r) ?? SampleData.sampleRoutines
        workouts = (await w) ?? []
        stats = await s
    }
}

@MainActor
public final class ExercisesViewModel: ObservableObject {
    @Published public var items: [Exercise] = []
    @Published public var filter: String = "all"
    @Published public var search: String = ""

    public init() {}

    public func load() async {
        do {
            items = try await APIClient.shared.exercises(muscle: filter, search: search)
        } catch {
            items = SampleData.sampleExercises
        }
    }
}

@MainActor
public final class ActiveWorkoutViewModel: ObservableObject {
    @Published public var elapsed: Int = 0
    @Published public var restSeconds: Int = 0
    @Published public var isResting = false
    @Published public var exercises: [(Exercise, [WorkoutSet])] = []

    private var timer: Timer?
    private var restTimer: Timer?

    public init() {
        // Seed with sample workout state
        let ex = SampleData.sampleExercises
        exercises = [
            (ex[1], [WorkoutSet(weight: 50, reps: 10, completed: true), WorkoutSet(weight: 55, reps: 8, completed: true), WorkoutSet(weight: 55, reps: 8)]),
            (ex[2], [WorkoutSet(weight: 0, reps: 10, completed: true), WorkoutSet(weight: 0, reps: 9), WorkoutSet(weight: 0, reps: 8)]),
            (ex[9], [WorkoutSet(weight: 25, reps: 12), WorkoutSet(weight: 25, reps: 12), WorkoutSet(weight: 25, reps: 12)])
        ]
    }

    public func start() {
        timer?.invalidate()
        timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { [weak self] _ in
            guard let self else { return }
            Task { @MainActor in
                self.elapsed += 1
            }
        }
    }

    public func stop() {
        timer?.invalidate(); timer = nil
        restTimer?.invalidate(); restTimer = nil
    }

    public func toggleSet(_ exerciseIndex: Int, _ setIndex: Int) {
        guard exerciseIndex < exercises.count else { return }
        guard setIndex < exercises[exerciseIndex].1.count else { return }
        exercises[exerciseIndex].1[setIndex].completed.toggle()
        if exercises[exerciseIndex].1[setIndex].completed {
            beginRest(exercises[exerciseIndex].1[setIndex].restSec)
        }
    }

    public func beginRest(_ seconds: Int) {
        restSeconds = seconds
        isResting = true
        restTimer?.invalidate()
        restTimer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { [weak self] _ in
            guard let self else { return }
            Task { @MainActor in
                if self.restSeconds > 0 { self.restSeconds -= 1 }
                if self.restSeconds == 0 { self.isResting = false }
            }
        }
    }

    public func skipRest() { restSeconds = 0; isResting = false }

    public var formattedElapsed: String { Self.format(elapsed) }
    public var formattedRest: String { Self.format(restSeconds) }

    static func format(_ s: Int) -> String {
        String(format: "%02d:%02d", s / 60, s % 60)
    }
}
