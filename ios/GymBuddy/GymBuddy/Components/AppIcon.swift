import SwiftUI

/// SF Symbols mapping for the icon names used in the prototype.
public enum AppIconName: String {
    case home, dumbbell, flame, spark, heart, heartFill = "heart-fill"
    case user, users, cog, plus, check, close
    case chevronRight = "chevron-right"
    case chevronLeft = "chevron-left"
    case chevronDown = "chevron-down"
    case arrowRight = "arrow-right"
    case arrowLeft = "arrow-left"
    case search, calendar, bell, filter, bolt, rewind, star, send
    case apple, medal, trophy, chart, play, pause, image, camera
    case edit, language, moon, sun, location, clock, list, grid, more
    case globe, sliders

    public var sfSymbol: String {
        switch self {
        case .home: return "house.fill"
        case .dumbbell: return "dumbbell.fill"
        case .flame: return "flame.fill"
        case .spark: return "sparkles"
        case .heart: return "heart"
        case .heartFill: return "heart.fill"
        case .user: return "person.fill"
        case .users: return "person.2.fill"
        case .cog: return "gearshape.fill"
        case .plus: return "plus"
        case .check: return "checkmark"
        case .close: return "xmark"
        case .chevronRight: return "chevron.right"
        case .chevronLeft: return "chevron.left"
        case .chevronDown: return "chevron.down"
        case .arrowRight: return "arrow.right"
        case .arrowLeft: return "arrow.left"
        case .search: return "magnifyingglass"
        case .calendar: return "calendar"
        case .bell: return "bell.fill"
        case .filter: return "line.3.horizontal.decrease.circle"
        case .bolt: return "bolt.fill"
        case .rewind: return "arrow.uturn.backward"
        case .star: return "star.fill"
        case .send: return "paperplane.fill"
        case .apple: return "applelogo"
        case .medal: return "medal.fill"
        case .trophy: return "trophy.fill"
        case .chart: return "chart.bar.fill"
        case .play: return "play.fill"
        case .pause: return "pause.fill"
        case .image: return "photo"
        case .camera: return "camera.fill"
        case .edit: return "pencil"
        case .language: return "globe"
        case .moon: return "moon.fill"
        case .sun: return "sun.max.fill"
        case .location: return "location.fill"
        case .clock: return "clock"
        case .list: return "list.bullet"
        case .grid: return "square.grid.2x2.fill"
        case .more: return "ellipsis"
        case .globe: return "globe"
        case .sliders: return "slider.horizontal.3"
        }
    }
}

public struct AppIcon: View {
    public let name: AppIconName
    public var size: CGFloat = 22
    public var color: Color = .primary

    public init(_ name: AppIconName, size: CGFloat = 22, color: Color = .primary) {
        self.name = name; self.size = size; self.color = color
    }

    public var body: some View {
        Image(systemName: name.sfSymbol)
            .font(.system(size: size * 0.85, weight: .semibold))
            .foregroundStyle(color)
            .frame(width: size, height: size)
    }
}
