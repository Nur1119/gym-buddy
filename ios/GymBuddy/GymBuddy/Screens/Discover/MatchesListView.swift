import SwiftUI

public struct MatchesListView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss
    @StateObject private var vm = MatchesViewModel()

    @State private var chatMatch: Match?

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        NavigationStack {
            ZStack {
                theme.bg.ignoresSafeArea()
                ScrollView {
                    VStack(alignment: .leading, spacing: 16) {
                        // New matches strip
                        Text((lang == .ru ? "Новые совпадения" : "New matches").uppercased())
                            .font(.system(size: 12, weight: .bold))
                            .tracking(0.5)
                            .foregroundStyle(theme.textDim)
                            .padding(.horizontal, 18)

                        ScrollView(.horizontal, showsIndicators: false) {
                            HStack(spacing: 10) {
                                // Likes-you card
                                VStack(spacing: 4) {
                                    AppIcon(.bolt, size: 20, color: .white)
                                    Text("14").font(.system(size: 22, weight: .heavy)).foregroundStyle(.white)
                                    Text((lang == .ru ? "Лайков" : "Likes").uppercased())
                                        .font(.system(size: 9, weight: .bold))
                                        .tracking(0.5)
                                        .foregroundStyle(.white.opacity(0.9))
                                }
                                .frame(width: 72, height: 96)
                                .background(theme.gradient)
                                .clipShape(RoundedRectangle(cornerRadius: 14))

                                ForEach(vm.matches) { m in
                                    let c = SampleData.gradientColors(for: m.user.id)
                                    Button {
                                        chatMatch = m
                                    } label: {
                                        ZStack(alignment: .bottom) {
                                            PhotoSlot(color1: Color(hex: c.0), color2: Color(hex: c.1))
                                            LinearGradient(colors: [.clear, .black.opacity(0.7)],
                                                           startPoint: .top, endPoint: .bottom)
                                            Text(m.user.name)
                                                .font(.system(size: 11, weight: .bold))
                                                .foregroundStyle(.white)
                                                .frame(maxWidth: .infinity)
                                                .padding(.bottom, 6)
                                        }
                                        .frame(width: 72, height: 96)
                                        .clipShape(RoundedRectangle(cornerRadius: 14))
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 14).stroke(m.unreadCount > 0 ? theme.p2 : .clear, lineWidth: 2)
                                        )
                                        .overlay(alignment: .topTrailing) {
                                            if m.unreadCount > 0 {
                                                Text("\(m.unreadCount)")
                                                    .font(.system(size: 10, weight: .bold))
                                                    .foregroundStyle(.white)
                                                    .padding(.horizontal, 6).padding(.vertical, 2)
                                                    .background(theme.p2)
                                                    .clipShape(Capsule())
                                                    .offset(x: -4, y: 4)
                                            }
                                        }
                                    }
                                    .buttonStyle(.plain)
                                }
                            }
                            .padding(.horizontal, 18)
                        }

                        // Messages list
                        Text((lang == .ru ? "Сообщения" : "Messages").uppercased())
                            .font(.system(size: 12, weight: .bold))
                            .tracking(0.5)
                            .foregroundStyle(theme.textDim)
                            .padding(.horizontal, 18)
                            .padding(.top, 8)

                        VStack(spacing: 4) {
                            ForEach(vm.matches) { m in
                                Button { chatMatch = m } label: {
                                    matchRow(m)
                                }.buttonStyle(.plain)
                            }
                        }
                        .padding(.horizontal, 18)
                    }
                    .padding(.vertical, 8)
                }
            }
            .navigationTitle(L("matches", lang))
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("back", lang)) { dismiss() }
                        .foregroundStyle(theme.p2)
                }
            }
            .sheet(item: $chatMatch) { match in
                ChatView(match: match)
                    .environmentObject(themeManager)
            }
        }
        .task { await vm.load() }
    }

    @ViewBuilder
    private func matchRow(_ m: Match) -> some View {
        let c = SampleData.gradientColors(for: m.user.id)
        HStack(spacing: 12) {
            ZStack(alignment: .bottomTrailing) {
                PhotoSlot(color1: Color(hex: c.0), color2: Color(hex: c.1))
                    .frame(width: 54, height: 54)
                    .clipShape(Circle())
                Circle().fill(theme.success)
                    .frame(width: 14, height: 14)
                    .overlay(Circle().stroke(theme.bg, lineWidth: 2))
            }
            VStack(alignment: .leading, spacing: 2) {
                HStack {
                    Text(m.user.name).font(.system(size: 15, weight: .bold)).foregroundStyle(theme.text)
                    Spacer()
                    Text("Today").font(.system(size: 11)).foregroundStyle(theme.textDim)
                }
                Text(m.lastMessage?.text ?? "")
                    .font(.system(size: 13, weight: m.unreadCount > 0 ? .semibold : .medium))
                    .foregroundStyle(m.unreadCount > 0 ? theme.text : theme.textMuted)
                    .lineLimit(1)
            }
            if m.unreadCount > 0 {
                Text("\(m.unreadCount)")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundStyle(.white)
                    .padding(.horizontal, 6).padding(.vertical, 2)
                    .background(theme.p2)
                    .clipShape(Capsule())
            }
        }
        .padding(10)
    }
}
