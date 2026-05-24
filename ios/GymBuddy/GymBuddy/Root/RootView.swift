import SwiftUI

public struct RootView: View {
    @EnvironmentObject var auth: AuthViewModel
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.colorScheme) private var systemScheme

    public init() {}

    public var body: some View {
        let theme = themeManager.theme(systemIsDark: systemScheme == .dark)

        Group {
            if auth.isAuthenticated {
                TabBarView()
            } else {
                LoginView()
            }
        }
        .environment(\.appTheme, theme)
        .preferredColorScheme(themeManager.mode == .system ? nil : (themeManager.mode == .dark ? .dark : .light))
    }
}
