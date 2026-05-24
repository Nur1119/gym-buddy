import SwiftUI

public struct Pill: View {
    public let title: String
    public var active: Bool = false
    public var color: Color?
    public var action: (() -> Void)?

    @Environment(\.appTheme) private var theme

    public init(_ title: String, active: Bool = false, color: Color? = nil, action: (() -> Void)? = nil) {
        self.title = title; self.active = active; self.color = color; self.action = action
    }

    public var body: some View {
        let activeColor = color ?? theme.p2
        Button {
            action?()
        } label: {
            Text(title)
                .font(.system(size: 13, weight: .semibold))
                .foregroundStyle(active ? Color.white : theme.textMuted)
                .padding(.horizontal, 14)
                .padding(.vertical, 8)
                .background(active ? activeColor : theme.chip)
                .clipShape(Capsule())
        }
        .buttonStyle(.plain)
    }
}

/// Card container matching `Card` from the prototype.
public struct AppCard<Content: View>: View {
    @Environment(\.appTheme) private var theme
    public var padding: CGFloat = 16
    public var cornerRadius: CGFloat = 18
    public let content: () -> Content

    public init(padding: CGFloat = 16, cornerRadius: CGFloat = 18, @ViewBuilder content: @escaping () -> Content) {
        self.padding = padding; self.cornerRadius = cornerRadius; self.content = content
    }

    public var body: some View {
        content()
            .padding(padding)
            .background(theme.surface)
            .clipShape(RoundedRectangle(cornerRadius: cornerRadius, style: .continuous))
            .overlay(
                RoundedRectangle(cornerRadius: cornerRadius, style: .continuous)
                    .stroke(theme.border, lineWidth: 1)
            )
    }
}
