import SwiftUI

public struct GradientButton: View {
    public let title: String
    public var icon: AppIconName?
    public var action: () -> Void

    @Environment(\.appTheme) private var theme

    public init(_ title: String, icon: AppIconName? = nil, action: @escaping () -> Void) {
        self.title = title; self.icon = icon; self.action = action
    }

    public var body: some View {
        Button(action: action) {
            HStack(spacing: 8) {
                if let icon = icon {
                    AppIcon(icon, size: 18, color: .white)
                }
                Text(title)
                    .font(.system(size: 15, weight: .bold))
                    .foregroundStyle(.white)
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 14).padding(.horizontal, 18)
            .background(theme.gradient)
            .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
            .shadow(color: theme.p2.opacity(0.25), radius: 12, x: 0, y: 8)
        }
        .buttonStyle(.plain)
    }
}

public struct OutlinedButton: View {
    public let title: String
    public var action: () -> Void
    @Environment(\.appTheme) private var theme

    public init(_ title: String, action: @escaping () -> Void) {
        self.title = title; self.action = action
    }

    public var body: some View {
        Button(action: action) {
            Text(title)
                .font(.system(size: 15, weight: .semibold))
                .foregroundStyle(theme.text)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 14)
                .background(theme.surface)
                .overlay(
                    RoundedRectangle(cornerRadius: 14, style: .continuous)
                        .stroke(theme.border, lineWidth: 1)
                )
                .clipShape(RoundedRectangle(cornerRadius: 14, style: .continuous))
        }
        .buttonStyle(.plain)
    }
}
