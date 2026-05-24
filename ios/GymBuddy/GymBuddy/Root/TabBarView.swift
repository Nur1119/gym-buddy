import SwiftUI

public struct TabBarView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme

    @State private var tab: AppTab = .home

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        VStack(spacing: 0) {
            ZStack {
                switch tab {
                case .home: HomeView()
                case .workout: WorkoutTrackerView()
                case .discover: DiscoverView()
                case .friends: FriendsView()
                case .profile: ProfileView()
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)

            BottomNavBar(tab: $tab, lang: lang)
        }
        .background(theme.bg.ignoresSafeArea())
    }
}
