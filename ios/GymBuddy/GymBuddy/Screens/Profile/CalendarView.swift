import SwiftUI

public struct CalendarView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        let days = lang == .ru ? ["Пн","Вт","Ср","Чт","Пт","Сб","Вс"] : ["Mo","Tu","We","Th","Fr","Sa","Su"]
        let workouts: [Int: [Color]] = [
            18: [theme.p2, theme.p3],
            19: [theme.p2],
            20: [theme.p1],
            23: [theme.p2, theme.p3, theme.p1]
        ]

        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(alignment: .leading, spacing: 14) {
                        HStack {
                            Text(lang == .ru ? "Май 2026" : "May 2026")
                                .font(.system(size: 17, weight: .bold)).foregroundStyle(theme.text)
                            Spacer()
                            IconButton(.chevronLeft, size: 32, iconSize: 16)
                            IconButton(.chevronRight, size: 32, iconSize: 16)
                        }
                        .padding(.horizontal, 18)

                        AppCard(padding: 12) {
                            VStack(spacing: 6) {
                                HStack {
                                    ForEach(days, id: \.self) { d in
                                        Text(d).font(.system(size: 10, weight: .bold))
                                            .foregroundStyle(theme.textDim)
                                            .frame(maxWidth: .infinity)
                                    }
                                }
                                let cells = monthCells()
                                ForEach(0..<5, id: \.self) { row in
                                    HStack(spacing: 4) {
                                        ForEach(0..<7, id: \.self) { col in
                                            let idx = row * 7 + col
                                            if idx < cells.count {
                                                let day = cells[idx]
                                                if day > 0 {
                                                    dayCell(day: day, workouts: workouts[day] ?? [])
                                                } else {
                                                    Color.clear.aspectRatio(1, contentMode: .fit).frame(maxWidth: .infinity)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        .padding(.horizontal, 18)

                        SectionHeader(lang == .ru ? "Предстоящие" : "Upcoming")

                        VStack(spacing: 10) {
                            upcomingRow(date: lang == .ru ? "Сегодня · 18:00" : "Today · 6 PM",
                                        name: lang == .ru ? "Верх тела" : "Upper body", color: theme.p3)
                            upcomingRow(date: lang == .ru ? "Завтра · 07:00" : "Tomorrow · 7 AM",
                                        name: lang == .ru ? "Ноги (с Alina)" : "Legs (with Alina)", color: theme.danger)
                            upcomingRow(date: lang == .ru ? "Чт · 19:00" : "Thu · 7 PM",
                                        name: lang == .ru ? "Тяги" : "Pull day", color: theme.p2)
                        }
                        .padding(.horizontal, 18)

                        Spacer().frame(height: 24)
                    }
                    .padding(.vertical, 12)
                }
            }
            .navigationTitle(L("calendar", lang))
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

    private func monthCells() -> [Int] {
        // Pseudo May 2026 view: offset 3 (since week starts Mon and May 1 is Fri).
        var arr = Array(repeating: 0, count: 3)
        for i in 1...31 { arr.append(i) }
        while arr.count < 35 { arr.append(0) }
        return Array(arr.prefix(35))
    }

    @ViewBuilder
    private func dayCell(day: Int, workouts: [Color]) -> some View {
        let today = day == 24
        let hasWorkout = !workouts.isEmpty
        ZStack {
            RoundedRectangle(cornerRadius: 8).fill(hasWorkout ? AnyShapeStyle(theme.gradientSoft) : AnyShapeStyle(Color.clear))
            RoundedRectangle(cornerRadius: 8).stroke(today ? theme.p2 : theme.border, lineWidth: today ? 2 : 1)
            VStack(spacing: 2) {
                Text("\(day)")
                    .font(.system(size: 13, weight: today ? .heavy : .semibold))
                    .foregroundStyle(today ? theme.p2 : (hasWorkout ? theme.text : theme.textDim))
                HStack(spacing: 2) {
                    ForEach(0..<workouts.prefix(3).count, id: \.self) { i in
                        Circle().fill(workouts[i]).frame(width: 4, height: 4)
                    }
                }
            }
        }
        .aspectRatio(1, contentMode: .fit)
        .frame(maxWidth: .infinity)
    }

    @ViewBuilder
    private func upcomingRow(date: String, name: String, color: Color) -> some View {
        AppCard(padding: 14) {
            HStack(spacing: 12) {
                RoundedRectangle(cornerRadius: 3).fill(color).frame(width: 6, height: 36)
                VStack(alignment: .leading, spacing: 2) {
                    Text(date).font(.system(size: 11, weight: .semibold)).foregroundStyle(theme.textMuted)
                    Text(name).font(.system(size: 15, weight: .bold)).foregroundStyle(theme.text)
                }
                Spacer()
                IconButton(.play, size: 32, iconSize: 14, bgColor: color, fgColor: .white)
            }
        }
    }
}
