import SwiftUI

public struct ChatView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss
    @StateObject private var vm: ChatViewModel

    @State private var showInvite = false

    public init(match: Match) {
        _vm = StateObject(wrappedValue: ChatViewModel(match: match))
    }

    public var body: some View {
        let lang = themeManager.language
        VStack(spacing: 0) {
            // Chat header
            chatHeader(lang: lang)

            // Messages
            ScrollViewReader { proxy in
                ScrollView {
                    LazyVStack(spacing: 8) {
                        Text("\(L("matchedOn", lang)) · \(vm.match.user.gymName ?? "")")
                            .font(.system(size: 11, weight: .semibold))
                            .tracking(0.5)
                            .foregroundStyle(theme.textDim)
                            .padding(.vertical, 8)

                        ForEach(vm.messages) { m in
                            messageBubble(m)
                                .id(m.id)
                        }

                        // Suggested workout invite card
                        suggestedWorkoutCard(lang: lang)
                    }
                    .padding(.horizontal, 14)
                    .padding(.top, 14)
                }
                .onChange(of: vm.messages.count) { _, _ in
                    if let last = vm.messages.last {
                        withAnimation { proxy.scrollTo(last.id, anchor: .bottom) }
                    }
                }
            }

            // Input
            HStack(spacing: 8) {
                IconButton(.image)
                TextField(L("typeMessage", lang), text: $vm.draft, axis: .horizontal)
                    .padding(.horizontal, 16).padding(.vertical, 12)
                    .background(theme.bg)
                    .overlay(RoundedRectangle(cornerRadius: 22).stroke(theme.border, lineWidth: 1))
                    .clipShape(RoundedRectangle(cornerRadius: 22))
                    .foregroundStyle(theme.text)
                    .onSubmit { Task { await vm.send() } }
                IconButton(.send, useGradient: true) {
                    Task { await vm.send() }
                }
            }
            .padding(.horizontal, 14)
            .padding(.vertical, 8)
            .background(theme.surface)
            .overlay(Rectangle().fill(theme.border).frame(height: 1), alignment: .top)
        }
        .background(theme.bg.ignoresSafeArea())
        .task { await vm.load() }
        .onDisappear { vm.disconnect() }
        .sheet(isPresented: $showInvite) {
            InviteWorkoutView(user: vm.match.user)
                .environmentObject(themeManager)
        }
    }

    @ViewBuilder
    private func chatHeader(lang: AppLanguage) -> some View {
        let c = SampleData.gradientColors(for: vm.match.user.id)
        HStack(spacing: 10) {
            IconButton(.arrowLeft) { dismiss() }
            PhotoSlot(color1: Color(hex: c.0), color2: Color(hex: c.1))
                .frame(width: 36, height: 36)
                .clipShape(Circle())
            VStack(alignment: .leading, spacing: 2) {
                Text(vm.match.user.name).font(.system(size: 15, weight: .bold)).foregroundStyle(theme.text)
                Text(L("online", lang)).font(.system(size: 11, weight: .semibold)).foregroundStyle(theme.success)
            }
            Spacer()
            IconButton(.dumbbell, fgColor: theme.p2) { showInvite = true }
            IconButton(.more)
        }
        .padding(.horizontal, 14)
        .padding(.vertical, 8)
        .background(theme.surface)
        .overlay(Rectangle().fill(theme.border).frame(height: 1), alignment: .bottom)
    }

    @ViewBuilder
    private func messageBubble(_ m: Message) -> some View {
        let mine = m.senderId == "me"
        HStack {
            if mine { Spacer(minLength: 40) }
            VStack(alignment: mine ? .trailing : .leading, spacing: 2) {
                Text(m.text ?? "")
                    .font(.system(size: 14))
                    .foregroundStyle(mine ? .white : theme.text)
                    .padding(.horizontal, 14).padding(.vertical, 10)
                    .background(mine ? AnyShapeStyle(theme.gradient) : AnyShapeStyle(theme.surface))
                    .overlay(
                        RoundedRectangle(cornerRadius: 18)
                            .stroke(mine ? Color.clear : theme.border, lineWidth: 1)
                    )
                    .clipShape(BubbleShape(mine: mine))
                Text(timestamp(m.createdAt))
                    .font(.system(size: 10))
                    .foregroundStyle(theme.textDim)
            }
            if !mine { Spacer(minLength: 40) }
        }
    }

    @ViewBuilder
    private func suggestedWorkoutCard(lang: AppLanguage) -> some View {
        HStack(spacing: 10) {
            AppIcon(.dumbbell, size: 20, color: theme.p2)
            VStack(alignment: .leading, spacing: 2) {
                Text(L("suggestWorkout", lang)).font(.system(size: 13, weight: .bold)).foregroundStyle(theme.text)
                Text(lang == .ru ? "Назначь совместную тренировку" : "Schedule a workout together")
                    .font(.system(size: 11)).foregroundStyle(theme.textMuted)
            }
            Spacer()
            Button {
                showInvite = true
            } label: {
                Text(L("add", lang))
                    .font(.system(size: 12, weight: .bold))
                    .foregroundStyle(.white)
                    .padding(.horizontal, 12).padding(.vertical, 6)
                    .background(theme.p2)
                    .clipShape(RoundedRectangle(cornerRadius: 10))
            }
            .buttonStyle(.plain)
        }
        .padding(12)
        .background(theme.gradientSoft)
        .overlay(RoundedRectangle(cornerRadius: 14).stroke(theme.p2.opacity(0.27), lineWidth: 1))
        .clipShape(RoundedRectangle(cornerRadius: 14))
    }

    private func timestamp(_ iso: String) -> String {
        // Naive — slice HH:mm out of ISO if present
        let comps = iso.split(separator: "T")
        if comps.count == 2 { return String(comps[1].prefix(5)) }
        return iso
    }
}

private struct BubbleShape: Shape {
    var mine: Bool
    func path(in rect: CGRect) -> Path {
        let r: CGFloat = 18
        let small: CGFloat = 4
        let tl: CGFloat = r
        let tr: CGFloat = r
        let br: CGFloat = mine ? small : r
        let bl: CGFloat = mine ? r : small
        return Path(roundedCornersIn: rect, tl: tl, tr: tr, br: br, bl: bl)
    }
}

extension Path {
    init(roundedCornersIn rect: CGRect, tl: CGFloat, tr: CGFloat, br: CGFloat, bl: CGFloat) {
        self.init()
        move(to: CGPoint(x: rect.minX + tl, y: rect.minY))
        addLine(to: CGPoint(x: rect.maxX - tr, y: rect.minY))
        addQuadCurve(to: CGPoint(x: rect.maxX, y: rect.minY + tr), control: CGPoint(x: rect.maxX, y: rect.minY))
        addLine(to: CGPoint(x: rect.maxX, y: rect.maxY - br))
        addQuadCurve(to: CGPoint(x: rect.maxX - br, y: rect.maxY), control: CGPoint(x: rect.maxX, y: rect.maxY))
        addLine(to: CGPoint(x: rect.minX + bl, y: rect.maxY))
        addQuadCurve(to: CGPoint(x: rect.minX, y: rect.maxY - bl), control: CGPoint(x: rect.minX, y: rect.maxY))
        addLine(to: CGPoint(x: rect.minX, y: rect.minY + tl))
        addQuadCurve(to: CGPoint(x: rect.minX + tl, y: rect.minY), control: CGPoint(x: rect.minX, y: rect.minY))
        closeSubpath()
    }
}
