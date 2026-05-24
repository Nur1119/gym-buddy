import SwiftUI
import Combine

// MARK: - Color hex helper
extension Color {
    public init(hex: String) {
        let s = hex.trimmingCharacters(in: CharacterSet(charactersIn: "#"))
        var v: UInt64 = 0
        Scanner(string: s).scanHexInt64(&v)
        let r, g, b, a: Double
        switch s.count {
        case 6:
            r = Double((v & 0xFF0000) >> 16) / 255
            g = Double((v & 0x00FF00) >> 8) / 255
            b = Double(v & 0x0000FF) / 255
            a = 1
        case 8:
            r = Double((v & 0xFF000000) >> 24) / 255
            g = Double((v & 0x00FF0000) >> 16) / 255
            b = Double((v & 0x0000FF00) >> 8) / 255
            a = Double(v & 0x000000FF) / 255
        default:
            r = 0; g = 0; b = 0; a = 1
        }
        self.init(.sRGB, red: r, green: g, blue: b, opacity: a)
    }
}

// MARK: - Theme model
public enum AppMode: String, CaseIterable, Codable {
    case system, light, dark
}

public enum AccentPalette: String, CaseIterable, Codable, Identifiable {
    case aurora, sunset, neon
    public var id: String { rawValue }
    public var displayName: String {
        switch self {
        case .aurora: return "Aurora"
        case .sunset: return "Sunset"
        case .neon:   return "Neon"
        }
    }
}

public struct AppTheme {
    // base
    public var bg: Color
    public var surface: Color
    public var surface2: Color
    public var surfaceElevated: Color
    public var border: Color
    public var borderStrong: Color
    public var text: Color
    public var textMuted: Color
    public var textDim: Color
    public var overlay: Color
    public var chip: Color
    public var danger: Color
    public var success: Color
    public var warn: Color
    public var isDark: Bool

    // accent
    public var p1: Color
    public var p2: Color
    public var p3: Color
    public var like: Color
    public var nope: Color
    public var superLike: Color
    public var boost: Color

    public var gradient: LinearGradient {
        LinearGradient(
            colors: [p3, p2, p1],
            startPoint: .topLeading,
            endPoint: .bottomTrailing
        )
    }

    public var gradientSoft: LinearGradient {
        LinearGradient(
            colors: [p3.opacity(0.18), p2.opacity(0.18), p1.opacity(0.18)],
            startPoint: .topLeading,
            endPoint: .bottomTrailing
        )
    }

    public var bgGradient: LinearGradient {
        if isDark {
            return LinearGradient(
                colors: [Color(hex: "#1A1F36"), bg],
                startPoint: .top,
                endPoint: .bottom
            )
        } else {
            return LinearGradient(
                colors: [bg, Color(hex: "#EAEEF6")],
                startPoint: .top,
                endPoint: .bottom
            )
        }
    }

    public static func make(isDark: Bool, accent: AccentPalette) -> AppTheme {
        let base: AppTheme = isDark ? .darkBase : .lightBase
        var t = base
        switch accent {
        case .aurora:
            t.p1 = Color(hex: "#3DDC97")
            t.p2 = Color(hex: "#00C2FF")
            t.p3 = Color(hex: "#7C5CFF")
            t.like = Color(hex: "#3DDC97")
            t.nope = Color(hex: "#FF4D6D")
            t.superLike = Color(hex: "#00C2FF")
            t.boost = Color(hex: "#B967FF")
        case .sunset:
            t.p1 = Color(hex: "#FFB020")
            t.p2 = Color(hex: "#FF4D6D")
            t.p3 = Color(hex: "#B967FF")
            t.like = Color(hex: "#19C37D")
            t.nope = Color(hex: "#FF4D6D")
            t.superLike = Color(hex: "#00C2FF")
            t.boost = Color(hex: "#FFB020")
        case .neon:
            t.p1 = Color(hex: "#00E5FF")
            t.p2 = Color(hex: "#39FF14")
            t.p3 = Color(hex: "#FFEB3B")
            t.like = Color(hex: "#39FF14")
            t.nope = Color(hex: "#FF1744")
            t.superLike = Color(hex: "#00E5FF")
            t.boost = Color(hex: "#FFEB3B")
        }
        return t
    }

    static let lightBase = AppTheme(
        bg: Color(hex: "#F4F6FB"),
        surface: Color(hex: "#FFFFFF"),
        surface2: Color(hex: "#F1F4FA"),
        surfaceElevated: Color(hex: "#FFFFFF"),
        border: Color(hex: "#E4E8F0"),
        borderStrong: Color(hex: "#D2D8E3"),
        text: Color(hex: "#0B1020"),
        textMuted: Color(hex: "#5B6478"),
        textDim: Color(hex: "#8B92A8"),
        overlay: Color.black.opacity(0.45),
        chip: Color(hex: "#EEF1F8"),
        danger: Color(hex: "#FF3B5C"),
        success: Color(hex: "#19C37D"),
        warn: Color(hex: "#FFB020"),
        isDark: false,
        p1: Color(hex: "#3DDC97"),
        p2: Color(hex: "#00C2FF"),
        p3: Color(hex: "#7C5CFF"),
        like: Color(hex: "#3DDC97"),
        nope: Color(hex: "#FF4D6D"),
        superLike: Color(hex: "#00C2FF"),
        boost: Color(hex: "#B967FF")
    )

    static let darkBase = AppTheme(
        bg: Color(hex: "#0A0E1A"),
        surface: Color(hex: "#141A2B"),
        surface2: Color(hex: "#1B2238"),
        surfaceElevated: Color(hex: "#1F2740"),
        border: Color(hex: "#262E47"),
        borderStrong: Color(hex: "#39426A"),
        text: Color(hex: "#FFFFFF"),
        textMuted: Color(hex: "#A2ABC5"),
        textDim: Color(hex: "#6A7390"),
        overlay: Color.black.opacity(0.6),
        chip: Color(hex: "#1F2740"),
        danger: Color(hex: "#FF4D6D"),
        success: Color(hex: "#3DDC97"),
        warn: Color(hex: "#FFC857"),
        isDark: true,
        p1: Color(hex: "#3DDC97"),
        p2: Color(hex: "#00C2FF"),
        p3: Color(hex: "#7C5CFF"),
        like: Color(hex: "#3DDC97"),
        nope: Color(hex: "#FF4D6D"),
        superLike: Color(hex: "#00C2FF"),
        boost: Color(hex: "#B967FF")
    )
}

// MARK: - ThemeManager
@MainActor
public final class ThemeManager: ObservableObject {
    @Published public var mode: AppMode {
        didSet { UserDefaults.standard.set(mode.rawValue, forKey: "app.mode") }
    }
    @Published public var accent: AccentPalette {
        didSet { UserDefaults.standard.set(accent.rawValue, forKey: "app.accent") }
    }
    @Published public var language: AppLanguage {
        didSet { UserDefaults.standard.set(language.rawValue, forKey: "app.language") }
    }

    public init() {
        let savedMode = UserDefaults.standard.string(forKey: "app.mode").flatMap(AppMode.init(rawValue:)) ?? .system
        let savedAccent = UserDefaults.standard.string(forKey: "app.accent").flatMap(AccentPalette.init(rawValue:)) ?? .aurora
        let savedLang = UserDefaults.standard.string(forKey: "app.language").flatMap(AppLanguage.init(rawValue:)) ?? .en
        self.mode = savedMode
        self.accent = savedAccent
        self.language = savedLang
    }

    public func theme(systemIsDark: Bool) -> AppTheme {
        let dark: Bool
        switch mode {
        case .system: dark = systemIsDark
        case .light: dark = false
        case .dark: dark = true
        }
        return AppTheme.make(isDark: dark, accent: accent)
    }

    public func toggleDark() {
        switch mode {
        case .system: mode = .dark
        case .dark: mode = .light
        case .light: mode = .dark
        }
    }

    public func cycleLanguage() {
        language = (language == .en) ? .ru : .en
    }
}

// MARK: - Environment helper
private struct ThemeKey: EnvironmentKey {
    static let defaultValue: AppTheme = .lightBase
}

extension EnvironmentValues {
    public var appTheme: AppTheme {
        get { self[ThemeKey.self] }
        set { self[ThemeKey.self] = newValue }
    }
}
