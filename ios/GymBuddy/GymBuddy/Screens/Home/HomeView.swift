import SwiftUI

public struct HomeView: View {
    @EnvironmentObject var auth: AuthViewModel
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        let user = auth.user ?? .preview

        ZStack {
            theme.bgGradient.ignoresSafeArea()
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    header(user: user, lang: lang)
                    heroPlan(lang: lang).padding(.horizontal, 18).padding(.top, 16)
                    streakCard(lang: lang).padding(.horizontal, 18).padding(.top, 18)
                    SectionHeader(L("achievements", lang), action: L("done", lang) + " →")
                    questsSection(lang: lang).padding(.horizontal, 18)
                    SectionHeader(L("thisWeek", lang))
                    statsRow(user: user, lang: lang).padding(.horizontal, 18)
                    SectionHeader(L("muscleHeatmap", lang))
                    muscleCard(lang: lang).padding(.horizontal, 18)
                    SectionHeader(L("recentRoutines", lang), action: "→")
                    routinesScroll(lang: lang)
                    Spacer().frame(height: 24)
                }
            }
        }
    }

    // MARK: - Sections

    @ViewBuilder
    private func header(user: User, lang: AppLanguage) -> some View {
        HStack(spacing: 10) {
            Avatar(name: user.name, size: 42, color1: theme.p3, color2: theme.p2)
            VStack(alignment: .leading, spacing: 2) {
                Text(L("greeting", lang))
                    .font(.system(size: 12, weight: .medium))
                    .foregroundStyle(theme.textDim)
                Text(user.name)
                    .font(.system(size: 16, weight: .bold))
                    .foregroundStyle(theme.text)
            }
            Spacer()
            HStack(spacing: 8) {
                chip(icon: .flame, value: "\(user.stats.streak)", color: theme.danger)
                chip(icon: .bolt, value: "\(user.stats.coins)", color: theme.warn)
            }
        }
        .padding(.horizontal, 18).padding(.top, 8).padding(.bottom, 4)
    }

    private func chip(icon: AppIconName, value: String, color: Color) -> some View {
        HStack(spacing: 4) {
            AppIcon(icon, size: 14, color: color)
            Text(value).font(.system(size: 13, weight: .bold)).foregroundStyle(theme.text)
        }
        .padding(.horizontal, 10).padding(.vertical, 6)
        .background(theme.surface)
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }

    @ViewBuilder
    private func heroPlan(lang: AppLanguage) -> some View {
        ZStack(alignment: .topLeading) {
            theme.gradient
                .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
                .frame(height: 180)

            // Decorative circles
            Circle().fill(Color.white.opacity(0.12))
                .frame(width: 160, height: 160).offset(x: 230, y: -30)
            Circle().fill(Color.white.opacity(0.08))
                .frame(width: 80, height: 80).offset(x: 260, y: 120)

            VStack(alignment: .leading, spacing: 6) {
                Text(L("todayPlan", lang).uppercased())
                    .font(.system(size: 11, weight: .bold))
                    .tracking(1)
                    .foregroundStyle(.white.opacity(0.85))
                Text("Upper body — Monday")
                    .font(.system(size: 22, weight: .heavy))
                    .foregroundStyle(.white)
                HStack(spacing: 14) {
                    Text("65 min").font(.system(size: 13, weight: .semibold))
                    Text("· 6 ex").font(.system(size: 13, weight: .semibold))
                    Text("· 28 sets").font(.system(size: 13, weight: .semibold))
                }
                .foregroundStyle(.white.opacity(0.95))
                Button {
                    // Start workout
                } label: {
                    HStack(spacing: 8) {
                        AppIcon(.play, size: 14, color: Color(hex: "#0B1020"))
                        Text(L("startWorkout", lang))
                            .font(.system(size: 14, weight: .bold))
                            .foregroundStyle(Color(hex: "#0B1020"))
                    }
                    .padding(.vertical, 10).padding(.horizontal, 18)
                    .background(Color.white)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                }
                .buttonStyle(.plain)
                .padding(.top, 4)
            }
            .padding(18)
        }
        .frame(height: 180)
        .shadow(color: theme.p2.opacity(0.25), radius: 16, x: 0, y: 12)
    }

    @ViewBuilder
    private func streakCard(lang: AppLanguage) -> some View {
        let days = lang == .ru ? ["Пн","Вт","Ср","Чт","Пт","Сб","Вс"] : ["Mo","Tu","We","Th","Fr","Sa","Su"]
        AppCard {
            VStack(alignment: .leading, spacing: 12) {
                HStack {
                    HStack(spacing: 8) {
                        AppIcon(.flame, size: 18, color: theme.danger)
                        Text(L("streak", lang))
                            .font(.system(size: 15, weight: .bold))
                            .foregroundStyle(theme.text)
                        Text("· \(SampleData.streakWeek.count) \(L("streakDays", lang))")
                            .font(.system(size: 13))
                            .foregroundStyle(theme.textMuted)
                    }
                    Spacer()
                    AppIcon(.chevronRight, size: 16, color: theme.textDim)
                }
                HStack(spacing: 4) {
                    ForEach(Array(SampleData.streakWeek.enumerated()), id: \.offset) { idx, v in
                        VStack(spacing: 6) {
                            Text(days[idx])
                                .font(.system(size: 10, weight: .bold))
                                .foregroundStyle(v == 1 ? theme.p2 : theme.textDim)
                                .tracking(0.5)
                            ZStack {
                                if v == 1 {
                                    Circle().fill(theme.gradient)
                                    AppIcon(.check, size: 16, color: .white)
                                } else if v > 0 {
                                    Circle().fill(theme.gradientSoft)
                                    Circle().fill(theme.p2).frame(width: 8, height: 8)
                                } else {
                                    Circle().fill(theme.chip)
                                        .overlay(Circle().stroke(theme.border, lineWidth: 1))
                                }
                            }
                            .frame(width: 32, height: 32)
                        }
                        .frame(maxWidth: .infinity)
                    }
                }
            }
        }
    }

    @ViewBuilder
    private func questsSection(lang: AppLanguage) -> some View {
        VStack(spacing: 10) {
            ForEach(SampleData.quests) { q in
                AppCard(padding: 14) {
                    VStack(alignment: .leading, spacing: 8) {
                        HStack {
                            Text(lang == .ru ? q.textRu : q.text)
                                .font(.system(size: 14, weight: .semibold))
                                .foregroundStyle(theme.text)
                            Spacer()
                            Text("+\(q.xp) XP")
                                .font(.system(size: 11, weight: .bold))
                                .foregroundStyle(theme.p2)
                                .padding(.horizontal, 10).padding(.vertical, 4)
                                .background(theme.gradientSoft)
                                .clipShape(Capsule())
                        }
                        HStack(spacing: 10) {
                            AppProgressBar(value: Double(q.progress), max: Double(q.total), height: 6)
                            Text("\(q.progress)/\(q.total)")
                                .font(.system(size: 12, weight: .semibold))
                                .foregroundStyle(theme.textMuted)
                        }
                    }
                }
            }
        }
    }

    @ViewBuilder
    private func statsRow(user: User, lang: AppLanguage) -> some View {
        HStack(spacing: 10) {
            StatTile(value: "\(user.stats.workoutsThisWeek)", label: L("workout", lang), icon: .dumbbell, color: theme.p2)
            StatTile(value: "4h 20m", label: "Time", icon: .clock, color: theme.p1)
            StatTile(value: "12.4t", label: "Volume", icon: .chart, color: theme.p3)
        }
    }

    @ViewBuilder
    private func muscleCard(lang: AppLanguage) -> some View {
        AppCard {
            HStack(spacing: 14) {
                MuscleSilhouette().frame(width: 110, height: 140)
                VStack(spacing: 10) {
                    muscleRow(lang == .ru ? "Грудь" : "Chest", value: 70, color: theme.danger)
                    muscleRow(lang == .ru ? "Ноги" : "Legs", value: 85, color: theme.danger)
                    muscleRow(lang == .ru ? "Спина" : "Back", value: 45, color: theme.warn)
                    muscleRow(lang == .ru ? "Кор" : "Core", value: 25, color: theme.p1)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
    }

    private func muscleRow(_ label: String, value: Int, color: Color) -> some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack {
                Text(label).font(.system(size: 11, weight: .semibold)).foregroundStyle(theme.textMuted)
                Spacer()
                Text("\(value)%").font(.system(size: 11, weight: .bold)).foregroundStyle(theme.text)
            }
            AppProgressBar(value: Double(value), max: 100, color: color, height: 5)
        }
    }

    @ViewBuilder
    private func routinesScroll(lang: AppLanguage) -> some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 10) {
                ForEach(SampleData.sampleRoutines.prefix(4)) { r in
                    let c = Color(hex: r.color)
                    VStack(alignment: .leading, spacing: 10) {
                        ZStack {
                            RoundedRectangle(cornerRadius: 10).fill(c)
                            AppIcon(.dumbbell, size: 18, color: .white)
                        }
                        .frame(width: 36, height: 36)
                        Text(r.name)
                            .font(.system(size: 14, weight: .bold))
                            .foregroundStyle(theme.text)
                        Text("\(r.totalSets) sets · \(r.estimatedDurationMin) min")
                            .font(.system(size: 11)).foregroundStyle(theme.textMuted)
                    }
                    .padding(14)
                    .frame(width: 180, alignment: .leading)
                    .background(
                        LinearGradient(colors: [c.opacity(0.13), c.opacity(0.06)],
                                       startPoint: .topLeading, endPoint: .bottomTrailing)
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 16, style: .continuous)
                            .stroke(c.opacity(0.2), lineWidth: 1)
                    )
                    .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
                }
            }
            .padding(.horizontal, 18)
        }
    }
}

/// Simplified muscle silhouette — matches the prototype's SVG shape.
private struct MuscleSilhouette: View {
    @Environment(\.appTheme) private var theme

    var body: some View {
        Canvas { ctx, size in
            // Body shape (head + torso outline)
            let w = size.width, h = size.height
            var head = Path()
            head.addEllipse(in: CGRect(x: w * 0.42, y: h * 0.04, width: w * 0.16, height: h * 0.18))
            var torso = Path()
            torso.move(to: CGPoint(x: w * 0.30, y: h * 0.20))
            torso.addLine(to: CGPoint(x: w * 0.70, y: h * 0.20))
            torso.addLine(to: CGPoint(x: w * 0.72, y: h * 0.50))
            torso.addLine(to: CGPoint(x: w * 0.66, y: h * 0.80))
            torso.addLine(to: CGPoint(x: w * 0.60, y: h * 0.95))
            torso.addLine(to: CGPoint(x: w * 0.55, y: h * 0.95))
            torso.addLine(to: CGPoint(x: w * 0.52, y: h * 0.60))
            torso.addLine(to: CGPoint(x: w * 0.48, y: h * 0.60))
            torso.addLine(to: CGPoint(x: w * 0.45, y: h * 0.95))
            torso.addLine(to: CGPoint(x: w * 0.40, y: h * 0.95))
            torso.addLine(to: CGPoint(x: w * 0.34, y: h * 0.80))
            torso.addLine(to: CGPoint(x: w * 0.28, y: h * 0.50))
            torso.closeSubpath()
            ctx.stroke(head, with: .color(theme.border), lineWidth: 1)
            ctx.stroke(torso, with: .color(theme.border), lineWidth: 1)
            ctx.fill(head, with: .color(theme.surface2))
            ctx.fill(torso, with: .color(theme.surface2))

            // Hot muscle markers
            let markers: [(CGFloat, CGFloat, Color)] = [
                (0.50, 0.26, theme.danger),  // chest
                (0.36, 0.22, theme.warn),    // shoulder L
                (0.64, 0.22, theme.warn),    // shoulder R
                (0.30, 0.38, theme.warn),    // arm L
                (0.70, 0.38, theme.warn),    // arm R
                (0.50, 0.44, theme.p1),      // core
                (0.44, 0.68, theme.danger),  // quad L
                (0.56, 0.68, theme.danger),  // quad R
                (0.44, 0.88, theme.p1),      // calf L
                (0.56, 0.88, theme.p1),      // calf R
            ]
            for (x, y, color) in markers {
                let cx = w * x, cy = h * y
                let r: CGFloat = 5
                var dot = Path()
                dot.addEllipse(in: CGRect(x: cx - r, y: cy - r, width: r * 2, height: r * 2))
                ctx.fill(dot, with: .color(color.opacity(0.85)))
            }
        }
    }
}
