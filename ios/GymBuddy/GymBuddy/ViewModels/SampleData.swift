import Foundation

/// Static sample data used as a backend fallback when the API isn't reachable
/// (so screens remain interactive). Mirrors `project/data.jsx`.
public enum SampleData {

    public static let sampleExercises: [Exercise] = [
        .init(id: "e1", name: "Bench Press", muscle: .chest, equipment: .barbell, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e2", name: "Incline Dumbbell Press", muscle: .chest, equipment: .dumbbell, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e3", name: "Pull-Ups", muscle: .back, equipment: .bodyweight, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e4", name: "Deadlift", muscle: .back, equipment: .barbell, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e5", name: "Squat", muscle: .legs, equipment: .barbell, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e6", name: "Leg Press", muscle: .legs, equipment: .machine, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e7", name: "Overhead Press", muscle: .shoulders, equipment: .barbell, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e8", name: "Lateral Raise", muscle: .shoulders, equipment: .dumbbell, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e9", name: "Bicep Curl", muscle: .arms, equipment: .dumbbell, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e10", name: "Tricep Pushdown", muscle: .arms, equipment: .cable, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e11", name: "Cable Fly", muscle: .chest, equipment: .cable, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e12", name: "Lat Pulldown", muscle: .back, equipment: .cable, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e13", name: "Romanian Deadlift", muscle: .legs, equipment: .barbell, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e14", name: "Plank", muscle: .core, equipment: .bodyweight, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e15", name: "Hanging Leg Raise", muscle: .core, equipment: .bodyweight, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
        .init(id: "e16", name: "Running", muscle: .cardio, equipment: .bodyweight, icon: "barbell", notes: nil, isCustom: false, ownerId: nil),
    ]

    public static let sampleRoutines: [Routine] = [
        Routine(id: "r1", name: "Upper body — Monday",
                exercises: [
                    .init(exerciseId: "e2", sets: 3, targetReps: 10, restSec: 90),
                    .init(exerciseId: "e3", sets: 3, targetReps: 8, restSec: 90),
                    .init(exerciseId: "e11", sets: 3, targetReps: 12, restSec: 60),
                ],
                totalSets: 28, estimatedDurationMin: 65,
                color: "#7C5CFF", ownerId: "me-1", createdAt: ""),
        Routine(id: "r2", name: "Pull day",
                exercises: [
                    .init(exerciseId: "e3", sets: 4, targetReps: 8, restSec: 90),
                    .init(exerciseId: "e4", sets: 4, targetReps: 6, restSec: 120),
                ],
                totalSets: 22, estimatedDurationMin: 55,
                color: "#00C2FF", ownerId: "me-1", createdAt: ""),
        Routine(id: "r3", name: "Leg day crusher",
                exercises: [
                    .init(exerciseId: "e5", sets: 5, targetReps: 6, restSec: 120),
                    .init(exerciseId: "e6", sets: 4, targetReps: 10, restSec: 90),
                ],
                totalSets: 24, estimatedDurationMin: 70,
                color: "#3DDC97", ownerId: "me-1", createdAt: ""),
        Routine(id: "r4", name: "Push day",
                exercises: [
                    .init(exerciseId: "e1", sets: 4, targetReps: 6, restSec: 120),
                    .init(exerciseId: "e2", sets: 3, targetReps: 10, restSec: 90),
                ],
                totalSets: 20, estimatedDurationMin: 50,
                color: "#FFB020", ownerId: "me-1", createdAt: ""),
        Routine(id: "r5", name: "Core finisher",
                exercises: [
                    .init(exerciseId: "e14", sets: 3, targetReps: 45, restSec: 30),
                    .init(exerciseId: "e15", sets: 3, targetReps: 12, restSec: 45),
                ],
                totalSets: 12, estimatedDurationMin: 25,
                color: "#FF4D6D", ownerId: "me-1", createdAt: ""),
    ]

    public static let discoverUsers: [User] = [
        sample(id: "u1", name: "Alina", age: 24, bio: "Powerlifter looking for a serious lifting partner. PRs > small talk.",
               goal: .strength, level: .advanced, gym: "PowerHouse Gym", height: 168, weight: 62,
               interests: ["Powerlifting", "Squat 140kg", "Coffee"]),
        sample(id: "u2", name: "Marcus", age: 27, bio: "Calisthenics & street workout. Park sessions every weekend.",
               goal: .calisthenics, level: .elite, gym: "Central Park", height: 182, weight: 78,
               interests: ["Planche", "Front lever", "Vegan"]),
        sample(id: "u3", name: "Sofia", age: 22, bio: "Yoga + functional. Looking for a balanced training partner.",
               goal: .mobility, level: .intermediate, gym: "Zenith Studio", height: 165, weight: 55,
               interests: ["Yoga", "Pilates", "Plant-based"]),
        sample(id: "u4", name: "Daniel", age: 29, bio: "Bodybuilder, prep for stage. 6x/week. Need someone to push leg day.",
               goal: .hypertrophy, level: .advanced, gym: "Iron Temple", height: 178, weight: 88,
               interests: ["Bodybuilding", "Chicken & rice", "Cardio hater"]),
        sample(id: "u5", name: "Emma", age: 26, bio: "CrossFit coach. WODs at 6am, coffee at 7. Energy off the charts.",
               goal: .crossFit, level: .elite, gym: "BoxFit", height: 170, weight: 64,
               interests: ["Olympic lifts", "Murph", "Trail running"]),
    ]

    public static let sampleMatches: [Match] = [
        Match(id: "m1", user: discoverUsers[0], matchedAt: "2026-05-24T10:00:00Z",
              lastMessage: Message(id: "x1", matchId: "m1", senderId: "u1", kind: .text,
                                   text: "Up for legs tomorrow?", imageUrl: nil, payload: nil,
                                   createdAt: "2026-05-24T18:11:00Z", readAt: nil),
              unreadCount: 2, isUnmatched: false),
        Match(id: "m2", user: discoverUsers[2], matchedAt: "2026-05-23T10:00:00Z",
              lastMessage: Message(id: "x2", matchId: "m2", senderId: "u3", kind: .text,
                                   text: "See you at 7am", imageUrl: nil, payload: nil,
                                   createdAt: "2026-05-24T09:21:00Z", readAt: nil),
              unreadCount: 0, isUnmatched: false),
        Match(id: "m3", user: discoverUsers[4], matchedAt: "2026-05-21T10:00:00Z",
              lastMessage: Message(id: "x3", matchId: "m3", senderId: "u5", kind: .text,
                                   text: "That Murph was brutal", imageUrl: nil, payload: nil,
                                   createdAt: "2026-05-24T10:30:00Z", readAt: nil),
              unreadCount: 5, isUnmatched: false),
    ]

    public static func sampleMessages(for match: Match) -> [Message] {
        let other = match.user.id
        return [
            Message(id: "1", matchId: match.id, senderId: other, kind: .text,
                    text: "Hey! Saw your bench PR, impressive", imageUrl: nil, payload: nil,
                    createdAt: "2026-05-24T14:22:00Z", readAt: nil),
            Message(id: "2", matchId: match.id, senderId: "me", kind: .text,
                    text: "Thanks! Your squat is insane btw", imageUrl: nil, payload: nil,
                    createdAt: "2026-05-24T14:25:00Z", readAt: nil),
            Message(id: "3", matchId: match.id, senderId: other, kind: .text,
                    text: "Wanna train together? I do legs Mondays", imageUrl: nil, payload: nil,
                    createdAt: "2026-05-24T14:26:00Z", readAt: nil),
            Message(id: "4", matchId: match.id, senderId: "me", kind: .text,
                    text: "Let's do it. PowerHouse?", imageUrl: nil, payload: nil,
                    createdAt: "2026-05-24T14:30:00Z", readAt: nil),
        ]
    }

    // MARK: - Helpers
    private static func sample(id: String, name: String, age: Int, bio: String,
                               goal: Goal, level: Level, gym: String,
                               height: Int, weight: Int, interests: [String]) -> User {
        User(id: id, email: "", name: name, username: "@" + name.lowercased(),
             age: age, height: height, weight: weight, bio: bio,
             goal: goal, level: level, gymName: gym, gymLat: nil, gymLng: nil,
             schedule: [1,3,5], interests: interests, photos: [], stats: .preview,
             createdAt: "")
    }

    // MARK: - Color pairs (cards)
    public static func gradientColors(for userId: String) -> (String, String) {
        switch userId {
        case "u1": return ("#FF4D6D", "#7C5CFF")
        case "u2": return ("#3DDC97", "#00C2FF")
        case "u3": return ("#7C5CFF", "#00C2FF")
        case "u4": return ("#00C2FF", "#3DDC97")
        case "u5": return ("#FFB020", "#FF4D6D")
        default:   return ("#7C5CFF", "#00C2FF")
        }
    }

    // MARK: - Quests / streaks
    public static let streakWeek: [Double] = [1, 1, 1, 1, 0.3, 1, 1]

    public struct Quest: Identifiable {
        public let id: String
        public let text: String
        public let textRu: String
        public let progress: Int
        public let total: Int
        public let xp: Int
    }

    public static let quests: [Quest] = [
        .init(id: "q1", text: "Train 3 times this week", textRu: "Тренируйся 3 раза на неделе", progress: 2, total: 3, xp: 250),
        .init(id: "q2", text: "Log 12 sets today", textRu: "Запиши 12 подходов сегодня", progress: 8, total: 12, xp: 80),
        .init(id: "q3", text: "Try a new exercise", textRu: "Попробуй новое упражнение", progress: 0, total: 1, xp: 100),
    ]

    // MARK: - Friends / leaderboard / medals
    public struct Friend: Identifiable {
        public let id: String
        public let name: String
        public let level: Int
        public let streak: Int
        public let online: Bool
        public let color: String
        public let last: String
    }

    public static let friends: [Friend] = [
        .init(id: "f1", name: "Jake T.", level: 64, streak: 88, online: true, color: "#FF4D6D", last: "Bench day"),
        .init(id: "f2", name: "Lena K.", level: 92, streak: 210, online: true, color: "#00C2FF", last: "PR squat 130kg"),
        .init(id: "f3", name: "Max R.", level: 41, streak: 12, online: false, color: "#7C5CFF", last: "5km run"),
        .init(id: "f4", name: "Yuki S.", level: 73, streak: 56, online: false, color: "#3DDC97", last: "Full body"),
        .init(id: "f5", name: "Carlos P.", level: 105, streak: 320, online: true, color: "#FFB020", last: "Hit goal weight"),
    ]

    public struct LeaderboardEntry: Identifiable {
        public let id: Int
        public let rank: Int
        public let name: String
        public let xp: Int
        public let color: String
        public let isMe: Bool
    }

    public static let leaderboard: [LeaderboardEntry] = [
        .init(id: 1, rank: 1, name: "Carlos P.", xp: 184220, color: "#FFD700", isMe: false),
        .init(id: 2, rank: 2, name: "Lena K.", xp: 172110, color: "#C0C0C0", isMe: false),
        .init(id: 3, rank: 3, name: "You", xp: 158913, color: "#CD7F32", isMe: true),
        .init(id: 4, rank: 4, name: "Yuki S.", xp: 141500, color: "#7C5CFF", isMe: false),
        .init(id: 5, rank: 5, name: "Jake T.", xp: 128340, color: "#00C2FF", isMe: false),
        .init(id: 6, rank: 6, name: "Marcus L.", xp: 119800, color: "#3DDC97", isMe: false),
        .init(id: 7, rank: 7, name: "Anya B.", xp: 102450, color: "#FF4D6D", isMe: false),
    ]

    public struct Medal: Identifiable {
        public let id: String
        public let icon: String
        public let name: String
        public let nameRu: String
        public let unlocked: Bool
    }

    public static let medals: [Medal] = [
        .init(id: "m1", icon: "flame.fill", name: "100-day streak", nameRu: "Стрик 100 дней", unlocked: true),
        .init(id: "m2", icon: "dumbbell.fill", name: "First 100kg bench", nameRu: "Первые 100кг в жиме", unlocked: true),
        .init(id: "m3", icon: "trophy.fill", name: "Top 10 weekly", nameRu: "Топ-10 недели", unlocked: true),
        .init(id: "m4", icon: "figure.strengthtraining.traditional", name: "Squat 2× bodyweight", nameRu: "Присед 2× веса", unlocked: false),
        .init(id: "m5", icon: "bolt.fill", name: "1000 sets logged", nameRu: "1000 подходов", unlocked: true),
        .init(id: "m6", icon: "star.fill", name: "Level 100", nameRu: "Уровень 100", unlocked: false),
    ]
}
