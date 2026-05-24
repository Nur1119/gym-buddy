import SwiftUI

public enum AppTab: String, CaseIterable, Identifiable {
    case home, workout, discover, friends, profile
    public var id: String { rawValue }

    public var icon: AppIconName {
        switch self {
        case .home: return .home
        case .workout: return .dumbbell
        case .discover: return .heart
        case .friends: return .users
        case .profile: return .user
        }
    }

    public func label(_ lang: AppLanguage) -> String {
        L(rawValue, lang)
    }
}

public struct BottomNavBar: View {
    @Binding public var tab: AppTab
    public var lang: AppLanguage

    @Environment(\.appTheme) private var theme

    public init(tab: Binding<AppTab>, lang: AppLanguage) {
        self._tab = tab; self.lang = lang
    }

    public var body: some View {
        HStack(spacing: 0) {
            ForEach(AppTab.allCases) { t in
                let active = (t == tab)
                Button {
                    tab = t
                } label: {
                    VStack(spacing: 4) {
                        ZStack {
                            RoundedRectangle(cornerRadius: 14, style: .continuous)
                                .fill(active ? AnyShapeStyle(theme.gradientSoft) : AnyShapeStyle(Color.clear))
                                .frame(width: 44, height: 28)
                            AppIcon(t == .discover && active ? .heartFill : t.icon,
                                    size: 22,
                                    color: active ? theme.p2 : theme.textDim)
                        }
                        Text(t.label(lang))
                            .font(.system(size: 10, weight: .semibold))
                            .foregroundStyle(active ? theme.p2 : theme.textDim)
                    }
                    .frame(maxWidth: .infinity)
                }
                .buttonStyle(.plain)
            }
        }
        .padding(.top, 8)
        .padding(.bottom, 26)
        .padding(.horizontal, 6)
        .background(
            theme.surface
                .overlay(Rectangle().fill(theme.border).frame(height: 1), alignment: .top)
        )
    }
}
