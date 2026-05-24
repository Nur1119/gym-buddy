import SwiftUI

public struct WorkoutTrackerView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var auth: AuthViewModel
    @Environment(\.appTheme) private var theme

    @StateObject private var vm = WorkoutViewModel()
    @State private var showActive = false
    @State private var showExercises = false
    @State private var selectedRoutine: Routine?
    @State private var tab: String = "tracker"

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        let user = auth.user ?? .preview

        ZStack {
            theme.bgGradient.ignoresSafeArea()
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    // Stats header
                    statsHeader(user: user).padding(.horizontal, 18).padding(.top, 8)

                    // Tab pills
                    TabPills(selection: $tab, tabs: [
                        ("tracker", L("tracker", lang)),
                        ("plan", L("myPlan", lang))
                    ])
                    .padding(.horizontal, 18).padding(.top, 16)

                    SectionHeader(L("plannedWorkout", lang))
                    createPlanCard(lang: lang).padding(.horizontal, 18)

                    SectionHeader(lang == .ru ? "Новая тренировка" : "New workout")
                    VStack(spacing: 10) {
                        actionCard(title: L("startEmptyWorkout", lang), icon: .dumbbell, color: theme.p2) {
                            showActive = true
                        }
                        actionCard(title: L("generateWorkout", lang), icon: .spark, color: theme.p1) {}
                    }
                    .padding(.horizontal, 18)

                    HStack {
                        Text(L("routines", lang))
                            .font(.system(size: 17, weight: .bold))
                            .foregroundStyle(theme.text)
                        Spacer()
                        IconButton(.list, size: 32, iconSize: 16) { showExercises = true }
                        IconButton(.plus, size: 32, iconSize: 16) {}
                    }
                    .padding(.horizontal, 18)
                    .padding(.top, 18)
                    .padding(.bottom, 8)

                    Text("\(L("myRoutines", lang)) (\(vm.routines.count))")
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundStyle(theme.textMuted)
                        .padding(.horizontal, 18)
                        .padding(.bottom, 4)

                    VStack(spacing: 10) {
                        ForEach(vm.routines) { r in
                            Button { selectedRoutine = r } label: {
                                routineCard(r)
                            }
                            .buttonStyle(.plain)
                        }
                    }
                    .padding(.horizontal, 18)
                    .padding(.top, 8)

                    Spacer().frame(height: 24)
                }
            }
        }
        .task {
            if vm.routines.isEmpty { await vm.load() }
        }
        .fullScreenCover(isPresented: $showActive) {
            ActiveWorkoutView()
                .environmentObject(themeManager)
        }
        .sheet(isPresented: $showExercises) {
            ExercisesListView()
                .environmentObject(themeManager)
        }
        .sheet(item: $selectedRoutine) { r in
            RoutineDetailView(routine: r)
                .environmentObject(themeManager)
        }
    }

    @ViewBuilder
    private func statsHeader(user: User) -> some View {
        HStack {
            HStack(spacing: 10) {
                Avatar(name: user.name, size: 40, color1: theme.p3, color2: theme.p2)
                VStack(alignment: .leading, spacing: 2) {
                    Text("Lv.\(user.stats.level)")
                        .font(.system(size: 11, weight: .semibold))
                        .foregroundStyle(theme.textDim)
                    AppProgressBar(value: Double(user.stats.xp), max: Double(user.stats.xpToNext), height: 4)
                        .frame(width: 80)
                }
            }
            Spacer()
            HStack(spacing: 8) {
                HStack(spacing: 4) {
                    AppIcon(.flame, size: 14, color: theme.danger)
                    Text("\(user.stats.streak)")
                        .font(.system(size: 13, weight: .bold))
                        .foregroundStyle(theme.text)
                }
                .padding(.horizontal, 10).padding(.vertical, 6)
                .background(theme.surface)
                .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
                .clipShape(RoundedRectangle(cornerRadius: 12))
                IconButton(.plus, size: 32, iconSize: 18, useGradient: true)
            }
        }
    }

    @ViewBuilder
    private func createPlanCard(lang: AppLanguage) -> some View {
        Button {
            // TODO: polish layout to match prototype — open create-routine flow
        } label: {
            HStack {
                HStack(spacing: 14) {
                    ZStack {
                        RoundedRectangle(cornerRadius: 12).fill(theme.gradientSoft)
                        AppIcon(.edit, size: 20, color: theme.p2)
                    }.frame(width: 44, height: 44)
                    Text(L("createPlan", lang))
                        .font(.system(size: 16, weight: .bold))
                        .foregroundStyle(theme.text)
                }
                Spacer()
                AppIcon(.chevronRight, size: 18, color: theme.textDim)
            }
            .padding(18)
            .background(theme.surface)
            .overlay(
                RoundedRectangle(cornerRadius: 16, style: .continuous)
                    .stroke(theme.borderStrong, style: StrokeStyle(lineWidth: 1, dash: [4, 4]))
            )
            .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private func actionCard(title: String, icon: AppIconName, color: Color, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            HStack {
                Text(title)
                    .font(.system(size: 16, weight: .bold))
                    .foregroundStyle(theme.text)
                Spacer()
                ZStack {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(LinearGradient(colors: [color, color.opacity(0.6)],
                                             startPoint: .topLeading, endPoint: .bottomTrailing))
                    AppIcon(icon, size: 22, color: .white)
                }
                .frame(width: 44, height: 44)
                .shadow(color: color.opacity(0.27), radius: 8, x: 0, y: 6)
            }
            .padding(18)
            .background(theme.surface)
            .overlay(RoundedRectangle(cornerRadius: 16, style: .continuous).stroke(theme.border, lineWidth: 1))
            .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private func routineCard(_ r: Routine) -> some View {
        AppCard(padding: 14) {
            VStack(alignment: .leading, spacing: 10) {
                HStack {
                    HStack(spacing: 10) {
                        RoundedRectangle(cornerRadius: 3).fill(Color(hex: r.color)).frame(width: 6, height: 32)
                        VStack(alignment: .leading, spacing: 2) {
                            Text(r.name).font(.system(size: 15, weight: .bold)).foregroundStyle(theme.text)
                            Text("\(r.totalSets) sets · \(r.estimatedDurationMin) min")
                                .font(.system(size: 11)).foregroundStyle(theme.textMuted)
                        }
                    }
                    Spacer()
                    IconButton(.more, size: 28, iconSize: 16)
                }
                VStack(alignment: .leading, spacing: 6) {
                    ForEach(r.exercises.prefix(2), id: \.exerciseId) { re in
                        let ex = SampleData.sampleExercises.first(where: { $0.id == re.exerciseId })
                        HStack(spacing: 8) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 6).fill(theme.chip)
                                AppIcon(.dumbbell, size: 12, color: theme.textMuted)
                            }
                            .frame(width: 24, height: 24)
                            Text(ex?.name ?? "Exercise").font(.system(size: 12, weight: .medium)).foregroundStyle(theme.text)
                            Spacer()
                            Text("\(re.sets) sets").font(.system(size: 11)).foregroundStyle(theme.textDim)
                        }
                    }
                    if r.exercises.count > 2 {
                        Text("+\(r.exercises.count - 2) more")
                            .font(.system(size: 11, weight: .semibold))
                            .foregroundStyle(theme.textDim)
                    }
                }
                .padding(.leading, 16)
            }
        }
    }
}

/// Tracker / My Plan pill tabs.
public struct TabPills: View {
    @Binding var selection: String
    let tabs: [(String, String)]
    @Environment(\.appTheme) private var theme

    public init(selection: Binding<String>, tabs: [(String, String)]) {
        self._selection = selection; self.tabs = tabs
    }

    public var body: some View {
        HStack(spacing: 4) {
            ForEach(tabs, id: \.0) { (key, label) in
                Button { selection = key } label: {
                    Text(label)
                        .font(.system(size: 14, weight: .bold))
                        .foregroundStyle(selection == key ? theme.text : theme.textMuted)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 10)
                        .background(selection == key ? theme.surface : .clear)
                        .clipShape(RoundedRectangle(cornerRadius: 10))
                }
                .buttonStyle(.plain)
            }
        }
        .padding(4)
        .background(theme.surface2)
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }
}
