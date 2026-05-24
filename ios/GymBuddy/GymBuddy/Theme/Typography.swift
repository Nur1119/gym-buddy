import SwiftUI

// Typography per DESIGN_TOKENS.md
public enum AppFont {
    public static func titleXL() -> Font { .system(size: 34, weight: .bold) }
    public static func title() -> Font { .system(size: 22, weight: .heavy) }
    public static func heading() -> Font { .system(size: 17, weight: .bold) }
    public static func body() -> Font { .system(size: 15, weight: .semibold) }
    public static func bodyRegular() -> Font { .system(size: 14, weight: .medium) }
    public static func caption() -> Font { .system(size: 12, weight: .semibold) }
    public static func captionSmall() -> Font { .system(size: 11, weight: .semibold) }
    public static func numericMono(size: CGFloat = 18) -> Font { .system(size: size, weight: .bold, design: .monospaced) }
}
