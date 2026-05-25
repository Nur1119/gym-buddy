import AuthenticationServices
import Foundation
import SwiftUI

@MainActor
public final class AuthViewModel: ObservableObject {
    @Published public var user: User?
    @Published public var isLoading = false
    @Published public var errorMessage: String?

    public var isAuthenticated: Bool { user != nil }

    public init() {
        // If we have a stored token, attempt to fetch current user.
        if TokenStore.shared.load() != nil {
            Task { await refreshUser() }
        }
    }

    public func login(email: String, password: String) async {
        isLoading = true; errorMessage = nil
        defer { isLoading = false }
        do {
            let res = try await APIClient.shared.login(email: email, password: password)
            user = res.user
        } catch {
            errorMessage = error.localizedDescription
            // For demo: drop in preview user so UI is reachable when backend is down.
            #if DEBUG
            user = .preview
            #endif
        }
    }

    public func register(email: String, password: String, name: String, age: Int) async {
        isLoading = true; errorMessage = nil
        defer { isLoading = false }
        do {
            let res = try await APIClient.shared.register(email: email, password: password, name: name, age: age)
            user = res.user
        } catch {
            errorMessage = error.localizedDescription
            #if DEBUG
            user = .preview
            #endif
        }
    }

    public func loginWithGoogle() async {
        isLoading = true; errorMessage = nil
        defer { isLoading = false }
        do {
            let idToken = try await GoogleAuthManager.shared.signIn()
            let res = try await APIClient.shared.loginWithGoogle(idToken: idToken)
            user = res.user
        } catch {
            if (error as? ASWebAuthenticationSessionError)?.code == .canceledLogin {
                return
            }
            errorMessage = error.localizedDescription
        }
    }

    public func refreshUser() async {
        do {
            user = try await APIClient.shared.currentUser()
        } catch {
            // ignore — user must re-login
        }
    }

    public func logout() {
        APIClient.shared.logout()
        user = nil
    }
}
