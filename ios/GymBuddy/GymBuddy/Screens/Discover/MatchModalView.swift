import SwiftUI

public struct MatchModalView: View {
    public let user: User
    public var onClose: () -> Void
    public var onChat: () -> Void

    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme

    public init(user: User, onClose: @escaping () -> Void, onChat: @escaping () -> Void) {
        self.user = user; self.onClose = onClose; self.onChat = onChat
    }

    public var body: some View {
        let lang = themeManager.language
        let colors = SampleData.gradientColors(for: user.id)
        ZStack {
            Color.black.opacity(0.85).ignoresSafeArea()

            // Soft glow
            Circle()
                .fill(theme.gradient)
                .frame(width: 300, height: 300)
                .opacity(0.3)
                .blur(radius: 60)

            VStack(spacing: 12) {
                Text(L("itsAMatch", lang))
                    .font(.system(size: 38, weight: .black))
                    .tracking(-1)
                    .foregroundStyle(theme.gradient)

                Text(lang == .ru ? "Вы с \(user.name) понравились друг другу"
                                : "You and \(user.name) liked each other")
                    .font(.system(size: 14))
                    .foregroundStyle(Color.white.opacity(0.75))
                    .padding(.bottom, 16)

                HStack(spacing: 14) {
                    PhotoSlot(color1: theme.p3, color2: theme.p2, cornerRadius: 16)
                        .frame(width: 110, height: 140)
                        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.white, lineWidth: 3))
                        .rotationEffect(.degrees(-8))
                    PhotoSlot(color1: Color(hex: colors.0), color2: Color(hex: colors.1), cornerRadius: 16)
                        .frame(width: 110, height: 140)
                        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.white, lineWidth: 3))
                        .rotationEffect(.degrees(8))
                }
                .padding(.bottom, 24)

                VStack(spacing: 10) {
                    GradientButton(L("sendMessage", lang), icon: .send, action: onChat)
                    Button(action: onClose) {
                        Text(lang == .ru ? "Продолжить свайпы" : "Keep swiping")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundStyle(.white)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 14)
                            .background(Color.white.opacity(0.15))
                            .clipShape(RoundedRectangle(cornerRadius: 14))
                    }
                    .buttonStyle(.plain)
                }
                .frame(maxWidth: 280)
            }
            .padding(30)
        }
    }
}
