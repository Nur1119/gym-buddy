import SwiftUI

public struct StatTile: View {
    public let value: String
    public let label: String
    public var icon: AppIconName?
    public var color: Color?

    @Environment(\.appTheme) private var theme

    public init(value: String, label: String, icon: AppIconName? = nil, color: Color? = nil) {
        self.value = value; self.label = label; self.icon = icon; self.color = color
    }

    public var body: some View {
        AppCard(padding: 14, cornerRadius: 14) {
            VStack(alignment: .leading, spacing: 4) {
                HStack(spacing: 6) {
                    if let icon = icon {
                        AppIcon(icon, size: 14, color: color ?? theme.p2)
                    }
                    Text(label.uppercased())
                        .font(.system(size: 11, weight: .semibold))
                        .foregroundStyle(theme.textDim)
                        .tracking(0.3)
                }
                Text(value)
                    .font(.system(size: 22, weight: .heavy))
                    .foregroundStyle(theme.text)
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        }
    }
}
