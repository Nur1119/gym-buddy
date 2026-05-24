import SwiftUI

public struct InviteWorkoutView: View {
    public let user: User

    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    @State private var selectedRoutineId: String = "r1"

    public init(user: User) { self.user = user }

    public var body: some View {
        let lang = themeManager.language
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(spacing: 14) {
                        // To
                        AppCard(padding: 14) {
                            HStack(spacing: 10) {
                                let c = SampleData.gradientColors(for: user.id)
                                PhotoSlot(color1: Color(hex: c.0), color2: Color(hex: c.1))
                                    .frame(width: 44, height: 44)
                                    .clipShape(Circle())
                                VStack(alignment: .leading, spacing: 2) {
                                    Text(lang == .ru ? "Кому" : "To")
                                        .font(.system(size: 11, weight: .semibold))
                                        .foregroundStyle(theme.textDim)
                                    Text("\(user.name), \(user.age)")
                                        .font(.system(size: 15, weight: .bold))
                                        .foregroundStyle(theme.text)
                                }
                                Spacer()
                            }
                        }

                        HStack(spacing: 10) {
                            AppCard(padding: 14) {
                                VStack(alignment: .leading, spacing: 6) {
                                    HStack(spacing: 6) {
                                        AppIcon(.calendar, size: 14, color: theme.p2)
                                        Text((lang == .ru ? "Дата" : "Date").uppercased())
                                            .font(.system(size: 11, weight: .semibold))
                                            .foregroundStyle(theme.textDim)
                                    }
                                    Text(lang == .ru ? "Завтра" : "Tomorrow")
                                        .font(.system(size: 16, weight: .bold))
                                        .foregroundStyle(theme.text)
                                    Text("Tue, May 26").font(.system(size: 11)).foregroundStyle(theme.textMuted)
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                            }

                            AppCard(padding: 14) {
                                VStack(alignment: .leading, spacing: 6) {
                                    HStack(spacing: 6) {
                                        AppIcon(.clock, size: 14, color: theme.p1)
                                        Text((lang == .ru ? "Время" : "Time").uppercased())
                                            .font(.system(size: 11, weight: .semibold))
                                            .foregroundStyle(theme.textDim)
                                    }
                                    Text("07:00")
                                        .font(.system(size: 16, weight: .bold))
                                        .foregroundStyle(theme.text)
                                    Text("1h 30m").font(.system(size: 11)).foregroundStyle(theme.textMuted)
                                }
                                .frame(maxWidth: .infinity, alignment: .leading)
                            }
                        }

                        AppCard(padding: 14) {
                            VStack(alignment: .leading, spacing: 6) {
                                HStack(spacing: 6) {
                                    AppIcon(.location, size: 14, color: theme.p3)
                                    Text((lang == .ru ? "Зал" : "Gym").uppercased())
                                        .font(.system(size: 11, weight: .semibold))
                                        .foregroundStyle(theme.textDim)
                                }
                                Text(user.gymName ?? "—")
                                    .font(.system(size: 16, weight: .bold))
                                    .foregroundStyle(theme.text)
                                Text("4.8 ★").font(.system(size: 11)).foregroundStyle(theme.textMuted)
                            }
                            .frame(maxWidth: .infinity, alignment: .leading)
                        }

                        // Routine selection
                        AppCard(padding: 14) {
                            VStack(alignment: .leading, spacing: 10) {
                                HStack(spacing: 6) {
                                    AppIcon(.dumbbell, size: 14, color: theme.p2)
                                    Text(L("sharedWorkout", lang).uppercased())
                                        .font(.system(size: 11, weight: .semibold))
                                        .foregroundStyle(theme.textDim)
                                }
                                ForEach(SampleData.sampleRoutines.prefix(2)) { r in
                                    Button { selectedRoutineId = r.id } label: {
                                        HStack(spacing: 10) {
                                            RoundedRectangle(cornerRadius: 3).fill(Color(hex: r.color)).frame(width: 6, height: 24)
                                            VStack(alignment: .leading, spacing: 2) {
                                                Text(r.name).font(.system(size: 14, weight: .semibold)).foregroundStyle(theme.text)
                                                Text("\(r.totalSets) sets · \(r.estimatedDurationMin)m").font(.system(size: 11)).foregroundStyle(theme.textMuted)
                                            }
                                            Spacer()
                                            ZStack {
                                                Circle().fill(selectedRoutineId == r.id ? theme.p2 : .clear)
                                                Circle().stroke(selectedRoutineId == r.id ? theme.p2 : theme.border, lineWidth: 2)
                                                if selectedRoutineId == r.id {
                                                    AppIcon(.check, size: 12, color: .white)
                                                }
                                            }
                                            .frame(width: 22, height: 22)
                                        }
                                        .padding(.vertical, 8)
                                    }
                                    .buttonStyle(.plain)
                                    Divider().overlay(theme.border)
                                }
                            }
                        }

                        GradientButton(lang == .ru ? "Отправить приглашение" : "Send invite", icon: .send) {
                            dismiss()
                        }
                    }
                    .padding(.horizontal, 18)
                    .padding(.vertical, 12)
                }
            }
            .navigationTitle(L("inviteToGym", lang))
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("cancel", lang)) { dismiss() }.foregroundStyle(theme.p2)
                }
                ToolbarItem(placement: .topBarTrailing) {
                    Button(lang == .ru ? "Отправить" : "Send") { dismiss() }.foregroundStyle(theme.p2).fontWeight(.bold)
                }
            }
        }
    }
}
