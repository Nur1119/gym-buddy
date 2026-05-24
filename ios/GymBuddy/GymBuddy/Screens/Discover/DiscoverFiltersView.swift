import SwiftUI

public struct DiscoverFiltersView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    @State private var ageMin: Double = 21
    @State private var ageMax: Double = 35
    @State private var maxDistance: Double = 15
    @State private var goal: String = "All"
    @State private var level: String = "All"
    @State private var scheduleDays: Set<Int> = [1,2,3,4,5]

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(alignment: .leading, spacing: 18) {
                        // Distance
                        AppCard {
                            VStack(alignment: .leading, spacing: 10) {
                                HStack {
                                    AppIcon(.location, size: 18, color: theme.p2)
                                    Text(lang == .ru ? "Расстояние" : "Distance")
                                        .font(.system(size: 14, weight: .bold)).foregroundStyle(theme.text)
                                    Spacer()
                                    Text("\(Int(maxDistance)) km")
                                        .font(.system(size: 14, weight: .bold)).foregroundStyle(theme.p2)
                                }
                                Slider(value: $maxDistance, in: 1...50, step: 1)
                                    .tint(theme.p2)
                            }
                        }

                        // Age range
                        AppCard {
                            VStack(alignment: .leading, spacing: 10) {
                                HStack {
                                    AppIcon(.user, size: 18, color: theme.p3)
                                    Text(L("age", lang))
                                        .font(.system(size: 14, weight: .bold)).foregroundStyle(theme.text)
                                    Spacer()
                                    Text("\(Int(ageMin)) – \(Int(ageMax))")
                                        .font(.system(size: 14, weight: .bold)).foregroundStyle(theme.p3)
                                }
                                Slider(value: $ageMax, in: ageMin...65, step: 1)
                                    .tint(theme.p3)
                            }
                        }

                        // Goal
                        section(title: L("goal", lang)) {
                            FlexibleHStack(items: ["All","Strength","Hypertrophy","Mobility","Calisthenics","CrossFit","Cardio"]) { g in
                                Pill(g, active: goal == g, color: theme.p1) { goal = g }
                            }
                        }

                        // Level
                        section(title: L("level", lang)) {
                            FlexibleHStack(items: ["All","Beginner","Intermediate","Advanced","Elite"]) { l in
                                Pill(l, active: level == l, color: theme.p2) { level = l }
                            }
                        }

                        // Schedule
                        section(title: lang == .ru ? "График" : "Schedule overlap") {
                            let labels = lang == .ru ? ["Пн","Вт","Ср","Чт","Пт","Сб","Вс"] : ["Mo","Tu","We","Th","Fr","Sa","Su"]
                            HStack(spacing: 6) {
                                ForEach(0..<7, id: \.self) { i in
                                    Pill(labels[i], active: scheduleDays.contains(i), color: theme.p2) {
                                        if scheduleDays.contains(i) { scheduleDays.remove(i) }
                                        else { scheduleDays.insert(i) }
                                    }
                                }
                            }
                        }

                        GradientButton(lang == .ru ? "Применить фильтры" : "Apply filters", icon: .check) {
                            dismiss()
                        }
                    }
                    .padding(.horizontal, 18)
                    .padding(.bottom, 24)
                }
            }
            .navigationTitle(L("filters", lang))
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("back", lang)) { dismiss() }.foregroundStyle(theme.p2)
                }
                ToolbarItem(placement: .topBarTrailing) {
                    Button("Reset") {
                        ageMin = 18; ageMax = 65; maxDistance = 15
                        goal = "All"; level = "All"
                        scheduleDays = []
                    }.foregroundStyle(theme.p2)
                }
            }
        }
    }

    @ViewBuilder
    private func section<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(title.uppercased())
                .font(.system(size: 12, weight: .bold))
                .tracking(0.5)
                .foregroundStyle(theme.textDim)
            content()
        }
    }
}

/// Simple wrapping HStack for pills.
struct FlexibleHStack<T: Hashable, Content: View>: View {
    let items: [T]
    let content: (T) -> Content
    var body: some View {
        // Multi-line layout via VStack-of-HStacks fallback.
        let columns = [GridItem(.adaptive(minimum: 80), spacing: 6)]
        LazyVGrid(columns: columns, alignment: .leading, spacing: 6) {
            ForEach(items, id: \.self) { item in
                content(item)
            }
        }
    }
}
