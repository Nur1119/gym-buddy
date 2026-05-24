import SwiftUI

public struct SwipeCardView: View {
    public let user: User
    public var offset: CGSize = .zero
    public var isTop: Bool = true

    @Environment(\.appTheme) private var theme

    public init(user: User, offset: CGSize = .zero, isTop: Bool = true) {
        self.user = user; self.offset = offset; self.isTop = isTop
    }

    public var body: some View {
        let colors = SampleData.gradientColors(for: user.id)
        let dx = offset.width
        let showLike = dx > 30
        let showNope = dx < -30

        ZStack(alignment: .bottomLeading) {
            // Photo
            PhotoSlot(color1: Color(hex: colors.0), color2: Color(hex: colors.1), label: "user photo")

            // Photo indicator dots (top)
            VStack {
                HStack(spacing: 4) {
                    ForEach(0..<5, id: \.self) { i in
                        RoundedRectangle(cornerRadius: 1.5)
                            .fill(i == 0 ? Color.white : Color.white.opacity(0.35))
                            .frame(height: 3)
                    }
                }
                .padding(.horizontal, 12)
                .padding(.top, 12)
                Spacer()
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)

            // LIKE / NOPE badges
            if showLike {
                VStack {
                    HStack {
                        Text("LIKE")
                            .font(.system(size: 28, weight: .black))
                            .tracking(2)
                            .foregroundStyle(theme.like)
                            .padding(.horizontal, 12).padding(.vertical, 4)
                            .overlay(RoundedRectangle(cornerRadius: 10).stroke(theme.like, lineWidth: 4))
                            .background(Color.black.opacity(0.2))
                            .rotationEffect(.degrees(-18))
                        Spacer()
                    }
                    .padding(.top, 36).padding(.leading, 20)
                    Spacer()
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
            if showNope {
                VStack {
                    HStack {
                        Spacer()
                        Text("NOPE")
                            .font(.system(size: 28, weight: .black))
                            .tracking(2)
                            .foregroundStyle(theme.nope)
                            .padding(.horizontal, 12).padding(.vertical, 4)
                            .overlay(RoundedRectangle(cornerRadius: 10).stroke(theme.nope, lineWidth: 4))
                            .background(Color.black.opacity(0.2))
                            .rotationEffect(.degrees(18))
                    }
                    .padding(.top, 36).padding(.trailing, 20)
                    Spacer()
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            }

            // Gradient overlay
            VStack {
                Spacer()
                LinearGradient(
                    colors: [Color.clear, Color.black.opacity(0.85)],
                    startPoint: .top, endPoint: .bottom
                )
                .frame(height: 280)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)

            // Info block
            VStack(alignment: .leading, spacing: 8) {
                HStack(alignment: .firstTextBaseline, spacing: 8) {
                    Text(user.name)
                        .font(.system(size: 28, weight: .heavy))
                        .foregroundStyle(.white)
                    Text("\(user.age)")
                        .font(.system(size: 24, weight: .regular))
                        .foregroundStyle(.white.opacity(0.95))
                    Spacer()
                    HStack(spacing: 4) {
                        Circle().fill(Color(hex: "#3DDC97")).frame(width: 6, height: 6)
                        Text("online").font(.system(size: 11, weight: .semibold)).foregroundStyle(.white)
                    }
                    .padding(.horizontal, 8).padding(.vertical, 4)
                    .background(Color.black.opacity(0.4))
                    .clipShape(Capsule())
                }

                HStack(spacing: 6) {
                    infoChip(icon: .dumbbell, text: user.goal.rawValue)
                    infoChip(icon: .location, text: "1.2 km")
                    infoChip(icon: nil, text: user.level.rawValue)
                }

                Text(user.bio)
                    .font(.system(size: 13))
                    .foregroundStyle(.white.opacity(0.95))
                    .lineLimit(3)
            }
            .padding(18)
        }
        .clipShape(RoundedRectangle(cornerRadius: 20, style: .continuous))
        .shadow(color: Color.black.opacity(0.25), radius: 20, x: 0, y: 20)
    }

    @ViewBuilder
    private func infoChip(icon: AppIconName?, text: String) -> some View {
        HStack(spacing: 4) {
            if let icon = icon {
                AppIcon(icon, size: 11, color: .white)
            }
            Text(text)
                .font(.system(size: 11, weight: .semibold))
                .foregroundStyle(.white)
        }
        .padding(.horizontal, 10).padding(.vertical, 5)
        .background(Color.white.opacity(0.18))
        .clipShape(Capsule())
    }
}
