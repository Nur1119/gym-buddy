import SwiftUI

public struct AppProgressBar: View {
    public var value: Double
    public var max: Double = 100
    public var color: Color?
    public var height: CGFloat = 8

    @Environment(\.appTheme) private var theme

    public init(value: Double, max: Double = 100, color: Color? = nil, height: CGFloat = 8) {
        self.value = value; self.max = max; self.color = color; self.height = height
    }

    public var body: some View {
        GeometryReader { geo in
            ZStack(alignment: .leading) {
                Capsule().fill(theme.chip).frame(height: height)
                Capsule()
                    .fill(color != nil ? AnyShapeStyle(color!) : AnyShapeStyle(theme.gradient))
                    .frame(width: max > 0 ? geo.size.width * min(1.0, value / max) : 0, height: height)
                    .animation(.easeInOut(duration: 0.3), value: value)
            }
        }
        .frame(height: height)
    }
}
