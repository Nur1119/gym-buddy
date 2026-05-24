import SwiftUI

public struct ProfileView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var auth: AuthViewModel
    @Environment(\.appTheme) private var theme

    @State private var showSettings = false
    @State private var showEdit = false
    @State private var showCalendar = false
    @State private var showNutrition = false
    @State private var showMedals = false

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        let user = auth.user ?? .preview

        ZStack {
            theme.bgGradient.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 0) {
                    header(user: user)
                    hero(user: user, lang: lang)
                    widgetsGrid(lang: lang).padding(.horizontal, 18)
                    SectionHeader(L("calendar", lang),
                                  action: lang == .ru ? "Все →" : "View all →",
                                  onAction: { showCalendar = true })
                    calendarMini(lang: lang).padding(.horizontal, 18)
                    Spacer().frame(height: 24)
                }
            }
        }
        .sheet(isPresented: $showSettings) { SettingsView().environmentObject(themeManager).environmentObject(auth) }
        .sheet(isPresented: $showEdit) { EditProfileView().environmentObject(themeManager).environmentObject(auth) }
        .sheet(isPresented: $showCalendar) { CalendarView().environmentObject(themeManager) }
        .sheet(isPresented: $showNutrition) { NutritionView().environmentObject(themeManager) }
        .sheet(isPresented: $showMedals) { MedalsView().environmentObject(themeManager) }
    }

    @ViewBuilder
    private func header(user: User) -> some View {
        HStack {
            HStack(spacing: 10) {
                Avatar(name: user.name, size: 42, color1: theme.p3, color2: theme.p2)
                VStack(alignment: .leading, spacing: 2) {
                    Text("Lv.\(user.stats.level)").font(.system(size: 11, weight: .semibold)).foregroundStyle(theme.textDim)
                    AppProgressBar(value: Double(user.stats.xp), max: Double(user.stats.xpToNext), height: 4)
                        .frame(width: 88)
                }
            }
            Spacer()
            HStack(spacing: 8) {
                HStack(spacing: 4) {
                    AppIcon(.flame, size: 14, color: theme.danger)
                    Text("\(user.stats.streak)").font(.system(size: 13, weight: .bold)).foregroundStyle(theme.text)
                }
                .padding(.horizontal, 10).padding(.vertical, 6)
                .background(theme.surface)
                .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
                .clipShape(RoundedRectangle(cornerRadius: 12))
                IconButton(.cog) { showSettings = true }
            }
        }
        .padding(.horizontal, 18).padding(.top, 4).padding(.bottom, 12)
    }

    @ViewBuilder
    private func hero(user: User, lang: AppLanguage) -> some View {
        VStack(spacing: 12) {
            ZStack(alignment: .bottomTrailing) {
                PhotoSlot(color1: theme.p3, color2: theme.p2, label: "your photo")
                    .frame(width: 110, height: 110)
                    .clipShape(Circle())
                    .overlay(Circle().stroke(theme.surface, lineWidth: 4))
                    .overlay(Circle().stroke(theme.p2, lineWidth: 3))
                ZStack {
                    Circle().fill(theme.gradient)
                    AppIcon(.camera, size: 14, color: .white)
                }
                .frame(width: 32, height: 32)
                .overlay(Circle().stroke(theme.bg, lineWidth: 3))
            }

            Text(user.name).font(.system(size: 22, weight: .heavy)).foregroundStyle(theme.text)
            Text(user.username).font(.system(size: 13)).foregroundStyle(theme.textMuted)

            AppCard(padding: 14) {
                HStack(spacing: 14) {
                    // Hex with level
                    ZStack {
                        HexagonShape().stroke(theme.border, lineWidth: 3)
                        HexagonShape().trim(from: 0, to: 0.74).stroke(theme.gradient, style: StrokeStyle(lineWidth: 3, lineCap: .round, lineJoin: .round))
                            .rotationEffect(.degrees(-90))
                        Text("\(user.stats.level)").font(.system(size: 18, weight: .heavy)).foregroundStyle(theme.text)
                    }
                    .frame(width: 56, height: 56)

                    VStack(alignment: .leading, spacing: 4) {
                        HStack {
                            Text("\(user.stats.xp) / \(user.stats.xpToNext) XP")
                                .font(.system(size: 13, weight: .semibold)).foregroundStyle(theme.text)
                            Spacer()
                            Text("Lv.\(user.stats.level + 1)").font(.system(size: 13, weight: .bold)).foregroundStyle(theme.p2)
                        }
                        AppProgressBar(value: Double(user.stats.xp), max: Double(user.stats.xpToNext), height: 8)
                        HStack(spacing: 4) {
                            AppIcon(.spark, size: 10, color: theme.p2)
                            Text("\(user.stats.totalXp) \(lang == .ru ? "всего XP" : "total XP")")
                                .font(.system(size: 11)).foregroundStyle(theme.textDim)
                        }
                    }
                }
            }
            .padding(.top, 4)
        }
        .padding(.horizontal, 18)
        .padding(.bottom, 16)
    }

    @ViewBuilder
    private func widgetsGrid(lang: AppLanguage) -> some View {
        let columns = [GridItem(.flexible(), spacing: 10), GridItem(.flexible(), spacing: 10), GridItem(.flexible(), spacing: 10)]
        LazyVGrid(columns: columns, spacing: 10) {
            widget(icon: .medal, label: L("medals", lang), color: theme.warn) { showMedals = true }
            widget(icon: .trophy, label: L("ranks", lang), color: theme.p3) {}
            widget(icon: .calendar, label: L("calendar", lang), color: theme.p2) { showCalendar = true }
            widget(icon: .apple, label: L("nutrition", lang), color: theme.p1) { showNutrition = true }
            widget(icon: .chart, label: L("stats", lang), color: theme.danger) {}
            widget(icon: .edit, label: L("editProfile", lang), color: theme.p2) { showEdit = true }
        }
    }

    @ViewBuilder
    private func widget(icon: AppIconName, label: String, color: Color, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            VStack(spacing: 8) {
                ZStack {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(LinearGradient(colors: [color, color.opacity(0.6)], startPoint: .topLeading, endPoint: .bottomTrailing))
                    AppIcon(icon, size: 22, color: .white)
                }
                .frame(width: 44, height: 44)
                .shadow(color: color.opacity(0.2), radius: 6, x: 0, y: 6)
                Text(label).font(.system(size: 12, weight: .bold)).foregroundStyle(theme.text)
            }
            .frame(maxWidth: .infinity)
            .padding(14)
            .background(theme.surface)
            .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.border, lineWidth: 1))
            .clipShape(RoundedRectangle(cornerRadius: 14))
        }
        .buttonStyle(.plain)
    }

    @ViewBuilder
    private func calendarMini(lang: AppLanguage) -> some View {
        let days = lang == .ru ? ["Пн","Вт","Ср","Чт","Пт","Сб","Вс"] : ["Mo","Tu","We","Th","Fr","Sa","Su"]
        AppCard(padding: 14) {
            VStack(spacing: 6) {
                HStack {
                    ForEach(days, id: \.self) { d in
                        Text(d).font(.system(size: 10, weight: .bold)).foregroundStyle(theme.textDim)
                            .frame(maxWidth: .infinity)
                    }
                }
                HStack(spacing: 4) {
                    ForEach([18,19,20,21,22,23,24], id: \.self) { d in
                        let hasWorkout = [18,19,20,23].contains(d)
                        let today = d == 24
                        ZStack {
                            RoundedRectangle(cornerRadius: 8)
                                .fill(hasWorkout ? AnyShapeStyle(theme.gradientSoft) : AnyShapeStyle(Color.clear))
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(today ? theme.p2 : theme.border, lineWidth: today ? 2 : 1)
                            VStack(spacing: 2) {
                                Text("\(d)")
                                    .font(.system(size: 12, weight: today ? .heavy : .semibold))
                                    .foregroundStyle(today ? theme.p2 : (hasWorkout ? theme.text : theme.textDim))
                                if hasWorkout {
                                    Circle().fill(theme.p2).frame(width: 4, height: 4)
                                }
                            }
                        }
                        .aspectRatio(1, contentMode: .fit)
                    }
                }
            }
        }
    }
}

/// Hexagon used for level badge.
struct HexagonShape: Shape {
    func path(in rect: CGRect) -> Path {
        var path = Path()
        let w = rect.width, h = rect.height
        let points: [(CGFloat, CGFloat)] = [
            (w * 0.5, 0), (w, h * 0.27), (w, h * 0.73),
            (w * 0.5, h), (0, h * 0.73), (0, h * 0.27)
        ]
        path.move(to: CGPoint(x: points[0].0, y: points[0].1))
        for p in points.dropFirst() {
            path.addLine(to: CGPoint(x: p.0, y: p.1))
        }
        path.closeSubpath()
        return path
    }
}
