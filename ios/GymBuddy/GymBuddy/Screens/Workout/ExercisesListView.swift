import SwiftUI

public struct ExercisesListView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss
    @StateObject private var vm = ExercisesViewModel()

    @State private var showCreate = false

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(spacing: 12) {
                        // Search
                        HStack(spacing: 10) {
                            AppIcon(.search, size: 18, color: theme.textDim)
                            TextField(L("search", lang), text: $vm.search)
                                .foregroundStyle(theme.text)
                                .onSubmit { Task { await vm.load() } }
                        }
                        .padding(.horizontal, 14).padding(.vertical, 12)
                        .background(theme.surface)
                        .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                        .padding(.horizontal, 18)

                        // Muscle filter pills
                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 8) {
                                Pill("All", active: vm.filter == "all", color: theme.p2) {
                                    vm.filter = "all"; Task { await vm.load() }
                                }
                                ForEach(Muscle.allCases) { m in
                                    Pill(m.rawValue, active: vm.filter == m.rawValue, color: theme.p2) {
                                        vm.filter = m.rawValue; Task { await vm.load() }
                                    }
                                }
                            }
                            .padding(.horizontal, 18)
                        }

                        // List
                        VStack(spacing: 8) {
                            ForEach(filteredItems()) { ex in
                                exerciseRow(ex)
                            }
                        }
                        .padding(.horizontal, 18)

                        Spacer().frame(height: 24)
                    }
                    .padding(.vertical, 12)
                }
            }
            .navigationTitle(L("exercises", lang))
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("back", lang)) { dismiss() }.foregroundStyle(theme.p2)
                }
                ToolbarItem(placement: .topBarTrailing) {
                    IconButton(.plus, size: 36, iconSize: 18, useGradient: true) {
                        showCreate = true
                    }
                }
            }
            .sheet(isPresented: $showCreate) {
                CreateExerciseView().environmentObject(themeManager)
            }
        }
        .task { if vm.items.isEmpty { await vm.load() } }
    }

    private func filteredItems() -> [Exercise] {
        vm.items.filter { ex in
            (vm.filter == "all" || ex.muscle.rawValue == vm.filter) &&
            (vm.search.isEmpty || ex.name.lowercased().contains(vm.search.lowercased()))
        }
    }

    @ViewBuilder
    private func exerciseRow(_ ex: Exercise) -> some View {
        HStack(spacing: 12) {
            ZStack {
                RoundedRectangle(cornerRadius: 10).fill(theme.chip)
                AppIcon(.dumbbell, size: 18, color: theme.textMuted)
            }
            .frame(width: 44, height: 44)

            VStack(alignment: .leading, spacing: 2) {
                Text(ex.name).font(.system(size: 14, weight: .bold)).foregroundStyle(theme.text)
                Text("\(ex.muscle.rawValue) · \(ex.equipment.rawValue)")
                    .font(.system(size: 11)).foregroundStyle(theme.textMuted)
            }
            Spacer()
            AppIcon(.chevronRight, size: 16, color: theme.textDim)
        }
        .padding(12)
        .background(theme.surface)
        .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.border, lineWidth: 1))
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }
}
