import SwiftUI

public struct ScreenHeader<Left: View, Right: View>: View {
    public let title: String
    public var large: Bool = false
    public let left: () -> Left
    public let right: () -> Right

    @Environment(\.appTheme) private var theme

    public init(_ title: String,
                large: Bool = false,
                @ViewBuilder left: @escaping () -> Left,
                @ViewBuilder right: @escaping () -> Right) {
        self.title = title; self.large = large
        self.left = left; self.right = right
    }

    public var body: some View {
        HStack(spacing: 10) {
            left().frame(minWidth: 32, alignment: .leading)
            if large {
                Text(title)
                    .font(.system(size: 22, weight: .bold))
                    .foregroundStyle(theme.text)
                    .frame(maxWidth: .infinity, alignment: .leading)
            } else {
                Text(title)
                    .font(.system(size: 17, weight: .bold))
                    .foregroundStyle(theme.text)
                    .frame(maxWidth: .infinity, alignment: .center)
            }
            right().frame(minWidth: 32, alignment: .trailing)
        }
        .padding(.horizontal, 18)
        .padding(.top, 4)
        .padding(.bottom, large ? 12 : 10)
    }
}

extension ScreenHeader where Left == EmptyView {
    public init(_ title: String, large: Bool = false, @ViewBuilder right: @escaping () -> Right) {
        self.init(title, large: large, left: { EmptyView() }, right: right)
    }
}

extension ScreenHeader where Right == EmptyView {
    public init(_ title: String, large: Bool = false, @ViewBuilder left: @escaping () -> Left) {
        self.init(title, large: large, left: left, right: { EmptyView() })
    }
}

extension ScreenHeader where Left == EmptyView, Right == EmptyView {
    public init(_ title: String, large: Bool = false) {
        self.init(title, large: large, left: { EmptyView() }, right: { EmptyView() })
    }
}

/// Circular icon button (matches IconBtn from prototype).
public struct IconButton: View {
    public let icon: AppIconName
    public var size: CGFloat = 36
    public var iconSize: CGFloat = 20
    public var bgColor: Color?
    public var fgColor: Color?
    public var useGradient: Bool = false
    public var badge: Int?
    public var action: () -> Void

    @Environment(\.appTheme) private var theme

    public init(_ icon: AppIconName,
                size: CGFloat = 36, iconSize: CGFloat = 20,
                bgColor: Color? = nil, fgColor: Color? = nil,
                useGradient: Bool = false, badge: Int? = nil,
                action: @escaping () -> Void = {}) {
        self.icon = icon; self.size = size; self.iconSize = iconSize
        self.bgColor = bgColor; self.fgColor = fgColor
        self.useGradient = useGradient; self.badge = badge; self.action = action
    }

    public var body: some View {
        Button(action: action) {
            ZStack {
                if useGradient {
                    Circle().fill(theme.gradient)
                } else {
                    Circle().fill(bgColor ?? theme.chip)
                }
                AppIcon(icon, size: iconSize, color: useGradient ? .white : (fgColor ?? theme.text))
            }
            .frame(width: size, height: size)
            .overlay(alignment: .topTrailing) {
                if let badge = badge {
                    Text("\(badge)")
                        .font(.system(size: 10, weight: .bold))
                        .foregroundStyle(.white)
                        .frame(minWidth: 18, minHeight: 18)
                        .padding(.horizontal, 4)
                        .background(theme.danger)
                        .clipShape(Capsule())
                        .overlay(Capsule().stroke(theme.surface, lineWidth: 2))
                        .offset(x: 4, y: -4)
                }
            }
        }
        .buttonStyle(.plain)
    }
}

/// Section header (title + optional action) — matches SectionHeader from prototype.
public struct SectionHeader: View {
    public let title: String
    public var action: String?
    public var onAction: (() -> Void)?

    @Environment(\.appTheme) private var theme

    public init(_ title: String, action: String? = nil, onAction: (() -> Void)? = nil) {
        self.title = title; self.action = action; self.onAction = onAction
    }

    public var body: some View {
        HStack(alignment: .firstTextBaseline) {
            Text(title)
                .font(.system(size: 17, weight: .bold))
                .foregroundStyle(theme.text)
            Spacer()
            if let action = action {
                Button(action: { onAction?() }) {
                    Text(action)
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundStyle(theme.p2)
                }
                .buttonStyle(.plain)
            }
        }
        .padding(.horizontal, 18)
        .padding(.top, 18)
        .padding(.bottom, 10)
    }
}
