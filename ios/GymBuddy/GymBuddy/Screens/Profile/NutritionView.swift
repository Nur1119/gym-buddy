import SwiftUI

public struct NutritionView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        let macros: [(String, Int, Int, Color)] = [
            (L("protein", lang), 142, 180, theme.danger),
            (L("carbs", lang), 220, 280, theme.warn),
            (L("fats", lang), 58, 72, theme.p2)
        ]
        let meals: [(String, Int, [String], String, String, Bool)] = [
            (L("breakfast", lang), 520, ["Oats", "2× eggs", "Banana"], "08:00", "fork.knife", false),
            (L("lunch", lang), 680, ["Chicken", "Rice", "Salad"], "13:00", "fork.knife", false),
            (L("snack", lang), 220, ["Protein shake", "Apple"], "16:30", "leaf.fill", false),
            (L("dinner", lang), 400, ["—"], "19:30", "fork.knife", true)
        ]
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(spacing: 14) {
                        // Calories ring
                        AppCard(padding: 20) {
                            HStack(spacing: 20) {
                                CalorieRing()
                                    .frame(width: 100, height: 100)
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(L("calories", lang).uppercased())
                                        .font(.system(size: 11, weight: .semibold))
                                        .tracking(0.5)
                                        .foregroundStyle(theme.textDim)
                                    Text("1820 / 2400")
                                        .font(.system(size: 24, weight: .heavy))
                                        .foregroundStyle(theme.text)
                                    Text("580 \(lang == .ru ? "осталось" : "remaining")")
                                        .font(.system(size: 12)).foregroundStyle(theme.textMuted)
                                }
                                Spacer()
                            }
                        }

                        // Macros
                        HStack(spacing: 10) {
                            ForEach(Array(macros.enumerated()), id: \.offset) { _, m in
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(m.0.uppercased())
                                        .font(.system(size: 11, weight: .semibold))
                                        .tracking(0.3)
                                        .foregroundStyle(theme.textDim)
                                    HStack(alignment: .firstTextBaseline, spacing: 2) {
                                        Text("\(m.1)").font(.system(size: 18, weight: .heavy)).foregroundStyle(theme.text)
                                        Text("g").font(.system(size: 11, weight: .semibold)).foregroundStyle(theme.textMuted)
                                    }
                                    AppProgressBar(value: Double(m.1), max: Double(m.2), color: m.3, height: 4)
                                    Text("/\(m.2)g").font(.system(size: 10)).foregroundStyle(theme.textDim)
                                }
                                .padding(12)
                                .frame(maxWidth: .infinity, alignment: .leading)
                                .background(theme.surface)
                                .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.border, lineWidth: 1))
                                .clipShape(RoundedRectangle(cornerRadius: 14))
                            }
                        }

                        // Meals
                        SectionHeader(lang == .ru ? "Приёмы пищи" : "Meals")
                            .padding(.horizontal, -18)

                        VStack(spacing: 10) {
                            ForEach(Array(meals.enumerated()), id: \.offset) { _, m in
                                HStack(spacing: 12) {
                                    Image(systemName: m.4)
                                        .font(.system(size: 24)).foregroundStyle(theme.text)
                                        .frame(width: 32)
                                    VStack(alignment: .leading, spacing: 2) {
                                        HStack(spacing: 8) {
                                            Text(m.0).font(.system(size: 15, weight: .bold)).foregroundStyle(theme.text)
                                            Text(m.3).font(.system(size: 11)).foregroundStyle(theme.textDim)
                                        }
                                        Text(m.2.joined(separator: " · "))
                                            .font(.system(size: 12)).foregroundStyle(theme.textMuted)
                                    }
                                    Spacer()
                                    Text("\(m.1) kcal")
                                        .font(.system(size: 14, weight: .bold))
                                        .foregroundStyle(m.5 ? theme.textDim : theme.text)
                                }
                                .padding(14)
                                .background(theme.surface)
                                .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.border, lineWidth: 1))
                                .clipShape(RoundedRectangle(cornerRadius: 14))
                                .opacity(m.5 ? 0.6 : 1)
                            }
                        }

                        Spacer().frame(height: 24)
                    }
                    .padding(.horizontal, 18)
                    .padding(.vertical, 12)
                }
            }
            .navigationTitle(L("nutrition", lang))
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("back", lang)) { dismiss() }.foregroundStyle(theme.p2)
                }
                ToolbarItem(placement: .topBarTrailing) {
                    IconButton(.plus, size: 36, iconSize: 18, useGradient: true)
                }
            }
        }
    }
}

private struct CalorieRing: View {
    @Environment(\.appTheme) private var theme

    var body: some View {
        ZStack {
            Circle().stroke(theme.chip, lineWidth: 10)
            Circle()
                .trim(from: 0, to: 0.77)
                .stroke(theme.gradient, style: StrokeStyle(lineWidth: 10, lineCap: .round))
                .rotationEffect(.degrees(-90))
            VStack(spacing: 0) {
                Text("1820").font(.system(size: 22, weight: .heavy)).foregroundStyle(theme.text)
                Text("kcal").font(.system(size: 10, weight: .semibold)).tracking(0.5).foregroundStyle(theme.textDim)
            }
        }
    }
}
