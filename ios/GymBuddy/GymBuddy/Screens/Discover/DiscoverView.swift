import SwiftUI

public struct DiscoverView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @StateObject private var vm = DiscoverViewModel()

    @State private var dragOffset: CGSize = .zero
    @State private var rotation: Double = 0
    @State private var showFilters = false
    @State private var showMatches = false
    @State private var chatTarget: User?

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        ZStack {
            theme.bg.ignoresSafeArea()

            VStack(spacing: 0) {
                // Header
                HStack {
                    IconButton(.sliders) { showFilters = true }
                    Spacer()
                    Text("GymBuddy")
                        .font(.system(size: 22, weight: .heavy))
                        .foregroundStyle(theme.gradient)
                    Spacer()
                    IconButton(.bell, badge: 3) { showMatches = true }
                }
                .padding(.horizontal, 18)
                .padding(.vertical, 8)

                // Cards
                ZStack {
                    if vm.stack.isEmpty {
                        emptyState(lang: lang)
                    } else {
                        ForEach(Array(vm.stack.prefix(3).enumerated().reversed()), id: \.element.id) { idx, user in
                            cardView(idx: idx, user: user)
                        }
                    }
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 8)
                .frame(maxHeight: .infinity)

                // Action buttons row
                HStack(spacing: 14) {
                    actionButton(icon: .rewind, color: theme.warn, size: 44) {}
                    actionButton(icon: .close, color: theme.nope, size: 56) {
                        Task { await commit(.pass) }
                    }
                    actionButton(icon: .star, color: theme.superLike, size: 48, glow: true) {
                        Task { await commit(.superlike) }
                    }
                    actionButton(icon: .heartFill, color: theme.like, size: 56) {
                        Task { await commit(.like) }
                    }
                    actionButton(icon: .bolt, color: theme.boost, size: 44, glow: true) {}
                }
                .padding(.vertical, 14)
                .padding(.horizontal, 18)
            }

            // Match modal
            if let matched = vm.matchedUser {
                MatchModalView(user: matched,
                               onClose: { vm.matchedUser = nil },
                               onChat: {
                                   vm.matchedUser = nil
                                   chatTarget = matched
                               })
                .transition(.opacity)
            }
        }
        .task {
            if vm.stack.isEmpty { await vm.load() }
        }
        .sheet(isPresented: $showFilters) {
            DiscoverFiltersView()
                .environmentObject(themeManager)
        }
        .sheet(isPresented: $showMatches) {
            MatchesListView()
                .environmentObject(themeManager)
        }
        .sheet(item: $chatTarget) { user in
            ChatView(match: Match(id: "live-\(user.id)", user: user, matchedAt: "now",
                                  lastMessage: nil, unreadCount: 0, isUnmatched: false))
                .environmentObject(themeManager)
        }
    }

    @ViewBuilder
    private func cardView(idx: Int, user: User) -> some View {
        let isTop = idx == 0
        let scale = 1.0 - CGFloat(idx) * 0.04
        let yOffset = CGFloat(idx) * 6
        let card = SwipeCardView(user: user, offset: isTop ? dragOffset : .zero, isTop: isTop)
            .scaleEffect(scale)
            .offset(x: isTop ? dragOffset.width : 0,
                    y: yOffset + (isTop ? dragOffset.height : 0))
            .rotationEffect(.degrees(isTop ? rotation : 0))
            .opacity(isTop ? 1.0 : 0.7)
            .animation(.spring(response: 0.4, dampingFraction: 0.7), value: vm.stack)

        if isTop {
            card.gesture(swipeGesture)
        } else {
            card
        }
    }

    // MARK: - Gesture
    private var swipeGesture: some Gesture {
        DragGesture()
            .onChanged { value in
                dragOffset = value.translation
                rotation = Double(value.translation.width / 20)
            }
            .onEnded { value in
                let threshold: CGFloat = 100
                if value.translation.width > threshold {
                    flyOut(direction: 1)
                    Task { await commit(.like) }
                } else if value.translation.width < -threshold {
                    flyOut(direction: -1)
                    Task { await commit(.pass) }
                } else {
                    withAnimation(.spring()) {
                        dragOffset = .zero
                        rotation = 0
                    }
                }
            }
    }

    private func flyOut(direction: CGFloat) {
        withAnimation(.easeOut(duration: 0.25)) {
            dragOffset = CGSize(width: direction * 600, height: 0)
            rotation = Double(direction * 30)
        }
    }

    private func commit(_ dir: SwipeRequest.Direction) async {
        await vm.swipe(direction: dir)
        dragOffset = .zero
        rotation = 0
    }

    // MARK: - UI helpers

    @ViewBuilder
    private func emptyState(lang: AppLanguage) -> some View {
        VStack(spacing: 10) {
            AppIcon(.heart, size: 48, color: theme.textDim)
            Text(lang == .ru ? "Все пересмотрели! Загляни позже." : "You're all caught up! Check back later.")
                .font(.system(size: 14, weight: .semibold))
                .foregroundStyle(theme.textMuted)
                .multilineTextAlignment(.center)
        }
        .padding(20)
    }

    @ViewBuilder
    private func actionButton(icon: AppIconName, color: Color, size: CGFloat,
                              glow: Bool = false, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            ZStack {
                Circle().fill(Color.white)
                AppIcon(icon, size: size * 0.45, color: color)
            }
            .frame(width: size, height: size)
            .shadow(color: glow ? color.opacity(0.55) : Color.black.opacity(0.15),
                    radius: glow ? 18 : 8, x: 0, y: 4)
        }
        .buttonStyle(.plain)
    }
}
