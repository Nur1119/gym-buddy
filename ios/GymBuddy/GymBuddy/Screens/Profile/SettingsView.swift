import SwiftUI

public struct SettingsView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var auth: AuthViewModel
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(spacing: 0) {
                        // Pro banner
                        HStack(spacing: 12) {
                            ZStack {
                                RoundedRectangle(cornerRadius: 12).fill(Color.white.opacity(0.25))
                                AppIcon(.spark, size: 22, color: .white)
                            }.frame(width: 44, height: 44)
                            VStack(alignment: .leading, spacing: 2) {
                                Text("GymBuddy Pro").font(.system(size: 15, weight: .heavy)).foregroundStyle(.white)
                                Text(lang == .ru ? "Безлимит свайпов, бусты, аналитика" : "Unlimited swipes, boosts, analytics")
                                    .font(.system(size: 12)).foregroundStyle(.white.opacity(0.9))
                            }
                            Spacer()
                            AppIcon(.chevronRight, size: 18, color: .white)
                        }
                        .padding(16)
                        .background(theme.gradient)
                        .clipShape(RoundedRectangle(cornerRadius: 16))
                        .padding(.horizontal, 18)
                        .padding(.bottom, 16)

                        // Preferences
                        sectionTitle(lang == .ru ? "Настройки" : "Preferences")
                        VStack(spacing: 0) {
                            // Theme toggle
                            HStack(spacing: 14) {
                                iconChip(theme.isDark ? .moon : .sun, color: theme.warn)
                                Text(L("appearance", lang)).font(.system(size: 15, weight: .semibold)).foregroundStyle(theme.text)
                                Spacer()
                                Toggle("", isOn: Binding(
                                    get: { theme.isDark },
                                    set: { _ in themeManager.toggleDark() }
                                ))
                                .labelsHidden()
                                .tint(theme.p2)
                            }
                            .padding(.horizontal, 16).padding(.vertical, 14)
                            Divider().overlay(theme.border)

                            // Accent palette
                            HStack(spacing: 14) {
                                iconChip(.spark, color: theme.p3)
                                Text(lang == .ru ? "Палитра" : "Accent")
                                    .font(.system(size: 15, weight: .semibold)).foregroundStyle(theme.text)
                                Spacer()
                                Picker("", selection: $themeManager.accent) {
                                    ForEach(AccentPalette.allCases) { p in
                                        Text(p.displayName).tag(p)
                                    }
                                }
                                .pickerStyle(.menu)
                                .tint(theme.p2)
                            }
                            .padding(.horizontal, 16).padding(.vertical, 14)
                            Divider().overlay(theme.border)

                            // Language
                            Button { themeManager.cycleLanguage() } label: {
                                HStack(spacing: 14) {
                                    iconChip(.language, color: theme.p2)
                                    Text(L("language", lang)).font(.system(size: 15, weight: .semibold)).foregroundStyle(theme.text)
                                    Spacer()
                                    Text(lang == .ru ? "Русский" : "English")
                                        .font(.system(size: 13, weight: .semibold))
                                        .foregroundStyle(theme.textMuted)
                                    AppIcon(.chevronRight, size: 16, color: theme.textDim)
                                }
                                .padding(.horizontal, 16).padding(.vertical, 14)
                            }
                            .buttonStyle(.plain)
                            Divider().overlay(theme.border)

                            row(icon: .dumbbell, label: L("units", lang), value: "kg / cm", color: theme.p1)
                            Divider().overlay(theme.border)
                            row(icon: .bell, label: L("notifications", lang), value: lang == .ru ? "Включены" : "On", color: theme.warn)
                            Divider().overlay(theme.border)
                            row(icon: .chart, label: lang == .ru ? "Аналитика" : "Analytics", value: nil, color: theme.p3)
                        }
                        .background(theme.surface)
                        .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.border, lineWidth: 1))
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                        .padding(.horizontal, 18)

                        // Account
                        sectionTitle(lang == .ru ? "Аккаунт" : "Account")
                        VStack(spacing: 0) {
                            row(icon: .user, label: lang == .ru ? "Профиль" : "Profile", value: nil, color: theme.p2)
                            Divider().overlay(theme.border)
                            row(icon: .medal, label: L("achievements", lang), value: nil, color: theme.warn)
                            Divider().overlay(theme.border)
                            row(icon: .users, label: lang == .ru ? "Реферальная программа" : "Referrals", value: nil, color: theme.p1)
                        }
                        .background(theme.surface)
                        .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.border, lineWidth: 1))
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                        .padding(.horizontal, 18)

                        // Legal
                        sectionTitle(lang == .ru ? "Документы" : "Legal")
                        VStack(spacing: 0) {
                            row(icon: .list, label: L("privacy", lang), value: nil, color: theme.textMuted)
                            Divider().overlay(theme.border)
                            row(icon: .list, label: L("terms", lang), value: nil, color: theme.textMuted)
                        }
                        .background(theme.surface)
                        .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.border, lineWidth: 1))
                        .clipShape(RoundedRectangle(cornerRadius: 14))
                        .padding(.horizontal, 18)

                        Button {
                            auth.logout()
                            dismiss()
                        } label: {
                            Text(L("logout", lang))
                                .font(.system(size: 15, weight: .bold))
                                .foregroundStyle(theme.danger)
                                .padding(.vertical, 10).padding(.horizontal, 20)
                        }
                        .buttonStyle(.plain)
                        .padding(.top, 24)

                        Spacer().frame(height: 30)
                    }
                    .padding(.top, 8)
                }
            }
            .navigationTitle(L("settings", lang))
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("back", lang)) { dismiss() }.foregroundStyle(theme.p2)
                }
            }
        }
    }

    @ViewBuilder
    private func iconChip(_ icon: AppIconName, color: Color) -> some View {
        ZStack {
            RoundedRectangle(cornerRadius: 8).fill(color.opacity(0.13))
            AppIcon(icon, size: 18, color: color)
        }
        .frame(width: 32, height: 32)
    }

    @ViewBuilder
    private func sectionTitle(_ text: String) -> some View {
        HStack {
            Text(text.uppercased())
                .font(.system(size: 11, weight: .bold))
                .tracking(1)
                .foregroundStyle(theme.textDim)
            Spacer()
        }
        .padding(.horizontal, 18)
        .padding(.top, 20)
        .padding(.bottom, 6)
    }

    @ViewBuilder
    private func row(icon: AppIconName, label: String, value: String?, color: Color) -> some View {
        HStack(spacing: 14) {
            iconChip(icon, color: color)
            Text(label).font(.system(size: 15, weight: .semibold)).foregroundStyle(theme.text)
            Spacer()
            if let value = value {
                Text(value).font(.system(size: 13, weight: .semibold)).foregroundStyle(theme.textMuted)
            }
            AppIcon(.chevronRight, size: 16, color: theme.textDim)
        }
        .padding(.horizontal, 16).padding(.vertical, 14)
    }
}
