import SwiftUI

/// Gradient placeholder matching the prototype's `PhotoSlot` (no real photo).
/// Uses stripe overlay + vignette for the same look.
public struct PhotoSlot: View {
    public let color1: Color
    public let color2: Color
    public var label: String?
    public var cornerRadius: CGFloat = 0

    public init(color1: Color, color2: Color, label: String? = nil, cornerRadius: CGFloat = 0) {
        self.color1 = color1; self.color2 = color2; self.label = label; self.cornerRadius = cornerRadius
    }

    public var body: some View {
        ZStack(alignment: .bottomLeading) {
            LinearGradient(colors: [color1, color2], startPoint: .topLeading, endPoint: .bottomTrailing)

            // 45° stripe pattern
            GeometryReader { geo in
                Canvas { ctx, size in
                    let stripeWidth: CGFloat = 14
                    let total = (size.width + size.height) / stripeWidth
                    for i in 0...Int(total) {
                        if i.isMultiple(of: 2) { continue }
                        var path = Path()
                        let x = CGFloat(i) * stripeWidth - size.height
                        path.move(to: CGPoint(x: x, y: size.height))
                        path.addLine(to: CGPoint(x: x + size.height, y: 0))
                        path.addLine(to: CGPoint(x: x + size.height + stripeWidth, y: 0))
                        path.addLine(to: CGPoint(x: x + stripeWidth, y: size.height))
                        path.closeSubpath()
                        ctx.fill(path, with: .color(Color.white.opacity(0.06)))
                    }
                }
                .frame(width: geo.size.width, height: geo.size.height)
            }

            // Vignette
            RadialGradient(
                colors: [Color.white.opacity(0.18), .clear],
                center: .init(x: 0.5, y: 0.3),
                startRadius: 0,
                endRadius: 220
            )

            if let label = label {
                Text(label)
                    .font(.system(size: 10, weight: .semibold, design: .monospaced))
                    .foregroundStyle(Color.white.opacity(0.85))
                    .padding(.horizontal, 10).padding(.vertical, 6)
                    .background(Color.black.opacity(0.35))
                    .cornerRadius(6)
                    .padding(10)
            }
        }
        .clipShape(RoundedRectangle(cornerRadius: cornerRadius, style: .continuous))
    }
}

public struct Avatar: View {
    public let name: String
    public var size: CGFloat = 40
    public var color1: Color = Color(hex: "#7C5CFF")
    public var color2: Color = Color(hex: "#00C2FF")

    public init(name: String, size: CGFloat = 40,
                color1: Color = Color(hex: "#7C5CFF"),
                color2: Color = Color(hex: "#00C2FF")) {
        self.name = name; self.size = size; self.color1 = color1; self.color2 = color2
    }

    public var body: some View {
        let initial = String(name.prefix(1)).uppercased()
        ZStack {
            LinearGradient(colors: [color1, color2], startPoint: .topLeading, endPoint: .bottomTrailing)
            Text(initial)
                .font(.system(size: size * 0.42, weight: .bold))
                .foregroundStyle(.white)
        }
        .frame(width: size, height: size)
        .clipShape(Circle())
    }
}
