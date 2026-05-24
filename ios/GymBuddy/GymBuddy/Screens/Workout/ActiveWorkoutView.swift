import SwiftUI

public struct ActiveWorkoutView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss
    @StateObject private var vm = ActiveWorkoutViewModel()

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        ZStack {
            theme.bgGradient.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 12) {
                    // Header timer
                    HStack {
                        Button {
                            vm.stop(); dismiss()
                        } label: {
                            Text(L("back", lang))
                                .font(.system(size: 13, weight: .semibold))
                                .foregroundStyle(theme.text)
                                .padding(.horizontal, 14).padding(.vertical, 8)
                                .background(theme.chip)
                                .clipShape(Capsule())
                        }
                        .buttonStyle(.plain)

                        Spacer()
                        HStack(spacing: 6) {
                            AppIcon(.clock, size: 18, color: theme.p1)
                            Text(vm.formattedElapsed)
                                .font(.system(size: 18, weight: .heavy, design: .monospaced))
                                .foregroundStyle(theme.p1)
                        }
                        Spacer()
                        Button {
                            vm.stop(); dismiss()
                        } label: {
                            Text(L("finishWorkout", lang))
                                .font(.system(size: 13, weight: .bold))
                                .foregroundStyle(.white)
                                .padding(.horizontal, 14).padding(.vertical, 8)
                                .background(theme.success)
                                .clipShape(Capsule())
                        }
                        .buttonStyle(.plain)
                    }
                    .padding(.horizontal, 18)
                    .padding(.top, 4)

                    // Rest banner
                    if vm.isResting && vm.restSeconds > 0 {
                        HStack {
                            VStack(alignment: .leading, spacing: 2) {
                                Text(L("restTimer", lang).uppercased())
                                    .font(.system(size: 11, weight: .bold))
                                    .tracking(1)
                                    .foregroundStyle(.white.opacity(0.85))
                                Text(vm.formattedRest)
                                    .font(.system(size: 28, weight: .heavy, design: .monospaced))
                                    .foregroundStyle(.white)
                            }
                            Spacer()
                            Button { vm.skipRest() } label: {
                                Text("Skip")
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundStyle(.white)
                                    .padding(.horizontal, 14).padding(.vertical, 8)
                                    .background(Color.white.opacity(0.25))
                                    .clipShape(Capsule())
                            }
                            .buttonStyle(.plain)
                        }
                        .padding(14)
                        .background(theme.gradient)
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                        .padding(.horizontal, 18)
                    }

                    // Exercise blocks
                    VStack(spacing: 12) {
                        ForEach(Array(vm.exercises.enumerated()), id: \.offset) { ei, pair in
                            let (ex, sets) = pair
                            AppCard(padding: 14) {
                                VStack(alignment: .leading, spacing: 12) {
                                    HStack(spacing: 10) {
                                        ZStack {
                                            RoundedRectangle(cornerRadius: 8).fill(theme.chip)
                                            AppIcon(.dumbbell, size: 18, color: theme.textMuted)
                                        }
                                        .frame(width: 36, height: 36)
                                        VStack(alignment: .leading, spacing: 2) {
                                            Text(ex.name)
                                                .font(.system(size: 15, weight: .bold))
                                                .foregroundStyle(theme.text)
                                            Text(ex.muscle.rawValue)
                                                .font(.system(size: 11))
                                                .foregroundStyle(theme.textMuted)
                                        }
                                        Spacer()
                                        IconButton(.more, size: 28, iconSize: 14)
                                    }

                                    setTable(ei: ei, sets: sets, lang: lang)

                                    Button {
                                        // TODO: append a set
                                    } label: {
                                        Text("+ \(lang == .ru ? "Подход" : "Set")")
                                            .font(.system(size: 13, weight: .semibold))
                                            .foregroundStyle(theme.textMuted)
                                            .frame(maxWidth: .infinity)
                                            .padding(.vertical, 8)
                                            .overlay(
                                                RoundedRectangle(cornerRadius: 8)
                                                    .stroke(theme.borderStrong, style: StrokeStyle(lineWidth: 1, dash: [4,4]))
                                            )
                                    }
                                    .buttonStyle(.plain)
                                }
                            }
                        }

                        Button {
                            // TODO: add exercise
                        } label: {
                            HStack(spacing: 6) {
                                AppIcon(.plus, size: 16, color: theme.p2)
                                Text(L("addExercise", lang))
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundStyle(theme.p2)
                            }
                            .frame(maxWidth: .infinity)
                            .padding(14)
                            .overlay(
                                RoundedRectangle(cornerRadius: 14)
                                    .stroke(theme.borderStrong, style: StrokeStyle(lineWidth: 1, dash: [4,4]))
                            )
                        }
                        .buttonStyle(.plain)
                    }
                    .padding(.horizontal, 18)

                    Spacer().frame(height: 80)
                }
            }
        }
        .onAppear { vm.start() }
        .onDisappear { vm.stop() }
    }

    @ViewBuilder
    private func setTable(ei: Int, sets: [WorkoutSet], lang: AppLanguage) -> some View {
        VStack(spacing: 6) {
            // Header row
            HStack {
                Text(L("set", lang).uppercased()).frame(width: 36, alignment: .leading)
                Text(L("weight", lang).uppercased()).frame(maxWidth: .infinity, alignment: .leading)
                Text(L("reps", lang).uppercased()).frame(maxWidth: .infinity, alignment: .leading)
                Spacer().frame(width: 30)
            }
            .font(.system(size: 10, weight: .bold))
            .tracking(0.5)
            .foregroundStyle(theme.textDim)
            .padding(.horizontal, 4)

            ForEach(Array(sets.enumerated()), id: \.offset) { si, s in
                HStack {
                    Text("\(si + 1)")
                        .font(.system(size: 14, weight: .bold))
                        .foregroundStyle(s.completed ? theme.p2 : theme.text)
                        .frame(width: 36, alignment: .leading)
                    Text(s.weight > 0 ? "\(Int(s.weight)) kg" : "—")
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundStyle(theme.text)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Text("\(s.reps)")
                        .font(.system(size: 14, weight: .semibold))
                        .foregroundStyle(theme.text)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    Button {
                        vm.toggleSet(ei, si)
                    } label: {
                        ZStack {
                            RoundedRectangle(cornerRadius: 8).fill(s.completed ? theme.p1 : theme.chip)
                            AppIcon(.check, size: 14, color: s.completed ? .white : theme.textDim)
                        }
                        .frame(width: 30, height: 30)
                    }
                    .buttonStyle(.plain)
                }
                .padding(.horizontal, 4).padding(.vertical, 4)
                .background(s.completed ? AnyShapeStyle(theme.gradientSoft) : AnyShapeStyle(Color.clear))
                .clipShape(RoundedRectangle(cornerRadius: 8))
            }
        }
    }
}
