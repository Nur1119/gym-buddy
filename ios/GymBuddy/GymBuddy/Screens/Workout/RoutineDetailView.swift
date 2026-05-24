import SwiftUI

public struct RoutineDetailView: View {
    public let routine: Routine
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    @State private var showActive = false

    public init(routine: Routine) { self.routine = routine }

    public var body: some View {
        let lang = themeManager.language
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(alignment: .leading, spacing: 14) {
                        // Hero
                        AppCard(padding: 18) {
                            VStack(alignment: .leading, spacing: 8) {
                                RoundedRectangle(cornerRadius: 3).fill(Color(hex: routine.color)).frame(width: 50, height: 6)
                                Text(routine.name).font(.system(size: 22, weight: .heavy)).foregroundStyle(theme.text)
                                Text("\(routine.totalSets) sets · \(routine.estimatedDurationMin) min")
                                    .font(.system(size: 13)).foregroundStyle(theme.textMuted)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }

                        // Exercise list
                        VStack(alignment: .leading, spacing: 8) {
                            Text(L("exercises", lang).uppercased())
                                .font(.system(size: 12, weight: .bold))
                                .tracking(0.5)
                                .foregroundStyle(theme.textDim)
                                .padding(.leading, 4)
                            ForEach(routine.exercises, id: \.exerciseId) { re in
                                let ex = SampleData.sampleExercises.first(where: { $0.id == re.exerciseId })
                                HStack(spacing: 12) {
                                    ZStack {
                                        RoundedRectangle(cornerRadius: 10).fill(theme.chip)
                                        AppIcon(.dumbbell, size: 18, color: theme.textMuted)
                                    }
                                    .frame(width: 44, height: 44)
                                    VStack(alignment: .leading, spacing: 2) {
                                        Text(ex?.name ?? "Exercise")
                                            .font(.system(size: 14, weight: .bold))
                                            .foregroundStyle(theme.text)
                                        Text("\(re.sets) sets × \(re.targetReps) · rest \(re.restSec)s")
                                            .font(.system(size: 11))
                                            .foregroundStyle(theme.textMuted)
                                    }
                                    Spacer()
                                }
                                .padding(12)
                                .background(theme.surface)
                                .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.border, lineWidth: 1))
                                .clipShape(RoundedRectangle(cornerRadius: 14))
                            }
                        }

                        GradientButton(L("startWorkout", lang), icon: .play) {
                            showActive = true
                        }
                    }
                    .padding(.horizontal, 18)
                    .padding(.vertical, 12)
                }
            }
            .navigationTitle(L("routines", lang))
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("back", lang)) { dismiss() }.foregroundStyle(theme.p2)
                }
            }
            .fullScreenCover(isPresented: $showActive) {
                ActiveWorkoutView().environmentObject(themeManager)
            }
        }
    }
}
