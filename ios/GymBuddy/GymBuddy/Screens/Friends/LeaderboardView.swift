import SwiftUI

public struct LeaderboardView: View {
    @Environment(\.appTheme) private var theme

    public init() {}

    public var body: some View {
        let top3 = SampleData.leaderboard.prefix(3)
        let rest = Array(SampleData.leaderboard.dropFirst(3))
        VStack(spacing: 8) {
            // Podium
            HStack(alignment: .bottom, spacing: 10) {
                if top3.count >= 3 { podium(top3[1]) } // 2nd
                if top3.count >= 1 { podium(top3[0]) } // 1st
                if top3.count >= 3 { podium(top3[2]) } // 3rd
            }
            .padding(.vertical, 14)
            .padding(.horizontal, 18)

            // Rest
            ForEach(rest) { u in
                HStack(spacing: 12) {
                    Text("\(u.rank)")
                        .font(.system(size: 14, weight: .heavy))
                        .foregroundStyle(theme.textMuted)
                        .frame(width: 28)
                    Avatar(name: u.name, size: 36,
                           color1: Color(hex: u.color), color2: Color(hex: u.color))
                    Text(u.name).font(.system(size: 14, weight: .bold)).foregroundStyle(theme.text)
                    Spacer()
                    Text("\(u.xp)").font(.system(size: 13, weight: .bold)).foregroundStyle(theme.p2)
                }
                .padding(12)
                .background(u.isMe ? AnyShapeStyle(theme.gradientSoft) : AnyShapeStyle(theme.surface))
                .overlay(RoundedRectangle(cornerRadius: 14).stroke(u.isMe ? theme.p2 : theme.border, lineWidth: 1))
                .clipShape(RoundedRectangle(cornerRadius: 14))
            }
            .padding(.horizontal, 18)
        }
    }

    @ViewBuilder
    private func podium(_ u: SampleData.LeaderboardEntry) -> some View {
        let height: CGFloat = u.rank == 1 ? 110 : (u.rank == 2 ? 90 : 80)
        VStack(spacing: 8) {
            Avatar(name: u.name, size: 56,
                   color1: Color(hex: u.color), color2: Color(hex: u.color))
            Text(u.name).font(.system(size: 12, weight: .bold)).foregroundStyle(theme.text)
            VStack(spacing: 0) {
                Text("\(u.rank)").font(.system(size: 22, weight: .black)).foregroundStyle(.white)
                Text("\(u.xp / 1000)k XP").font(.system(size: 10, weight: .bold)).foregroundStyle(.white.opacity(0.9))
            }
            .frame(width: 70, height: height)
            .background(
                LinearGradient(colors: [Color(hex: u.color), Color(hex: u.color).opacity(0.5)],
                               startPoint: .top, endPoint: .bottom)
            )
            .clipShape(RoundedRectangle(cornerRadius: 10, style: .continuous))
        }
    }
}
