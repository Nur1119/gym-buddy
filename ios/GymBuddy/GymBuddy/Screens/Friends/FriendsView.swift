import SwiftUI

public struct FriendsView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme

    @State private var tab: String = "friends"

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        ZStack {
            theme.bgGradient.ignoresSafeArea()
            ScrollView {
                VStack(spacing: 14) {
                    TabPills(selection: $tab, tabs: [
                        ("friends", L("friends", lang)),
                        ("requests", L("requests", lang)),
                        ("lb", L("leaderboard", lang))
                    ])
                    .padding(.horizontal, 18)
                    .padding(.top, 8)

                    switch tab {
                    case "friends": friendsTab(lang: lang)
                    case "lb": LeaderboardView()
                    case "requests": requestsTab(lang: lang)
                    default: EmptyView()
                    }

                    Spacer().frame(height: 24)
                }
            }
        }
    }

    @ViewBuilder
    private func friendsTab(lang: AppLanguage) -> some View {
        let online = SampleData.friends.filter { $0.online }.count
        VStack(spacing: 8) {
            HStack {
                Text("\(L("online", lang)) · \(online)")
                    .font(.system(size: 11, weight: .bold))
                    .tracking(0.5)
                    .foregroundStyle(theme.textDim)
                Spacer()
            }
            .padding(.horizontal, 18)
            .padding(.top, 4)

            VStack(spacing: 8) {
                ForEach(SampleData.friends) { f in
                    HStack(spacing: 12) {
                        ZStack(alignment: .bottomTrailing) {
                            Avatar(name: f.name, size: 48,
                                   color1: Color(hex: f.color), color2: Color(hex: f.color))
                            if f.online {
                                Circle().fill(theme.success)
                                    .frame(width: 12, height: 12)
                                    .overlay(Circle().stroke(theme.surface, lineWidth: 2))
                            }
                        }
                        VStack(alignment: .leading, spacing: 2) {
                            Text(f.name).font(.system(size: 14, weight: .bold)).foregroundStyle(theme.text)
                            HStack(spacing: 6) {
                                Text("Lv.\(f.level)")
                                Text("·")
                                AppIcon(.flame, size: 10, color: theme.danger)
                                Text("\(f.streak)")
                                Text("·")
                                Text(f.last)
                            }
                            .font(.system(size: 11)).foregroundStyle(theme.textMuted)
                        }
                        Spacer()
                        IconButton(.send, size: 32, iconSize: 14)
                    }
                    .padding(12)
                    .background(theme.surface)
                    .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.border, lineWidth: 1))
                    .clipShape(RoundedRectangle(cornerRadius: 14))
                }
            }
            .padding(.horizontal, 18)
        }
    }

    @ViewBuilder
    private func requestsTab(lang: AppLanguage) -> some View {
        VStack(spacing: 8) {
            AppIcon(.users, size: 42, color: theme.textDim)
            Text(lang == .ru ? "Нет новых заявок" : "No new requests")
                .font(.system(size: 14, weight: .semibold))
                .foregroundStyle(theme.textMuted)
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 80)
    }
}
