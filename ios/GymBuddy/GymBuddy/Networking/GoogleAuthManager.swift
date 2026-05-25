import AuthenticationServices
import CryptoKit
import Foundation

// Google OAuth client configuration.
// Replace GOOGLE_CLIENT_ID with your actual iOS client ID from Google Cloud Console.
// The reversed client ID is used as the URL scheme for the OAuth callback.
private let kGoogleClientID = "356246047318-0uhr9mgfr7p6flt7tebsbaqv49l0p8bq.apps.googleusercontent.com"
private let kGoogleReversedClientID = "com.googleusercontent.apps.356246047318-0uhr9mgfr7p6flt7tebsbaqv49l0p8bq"
private let kGoogleTokenEndpoint = "https://oauth2.googleapis.com/token"
private let kGoogleAuthEndpoint = "https://accounts.google.com/o/oauth2/v2/auth"

@MainActor
public final class GoogleAuthManager: NSObject, ASWebAuthenticationPresentationContextProviding {
    public static let shared = GoogleAuthManager()

    private var currentSession: ASWebAuthenticationSession?

    private override init() {}

    public func presentationAnchor(for session: ASWebAuthenticationSession) -> ASPresentationAnchor {
        UIApplication.shared.connectedScenes
            .compactMap { $0 as? UIWindowScene }
            .flatMap { $0.windows }
            .first(where: \.isKeyWindow) ?? ASPresentationAnchor()
    }

    public func signIn() async throws -> String {
        let verifier = generateCodeVerifier()
        let challenge = generateCodeChallenge(from: verifier)
        let state = UUID().uuidString

        var components = URLComponents(string: kGoogleAuthEndpoint)!
        components.queryItems = [
            URLQueryItem(name: "client_id", value: kGoogleClientID),
            URLQueryItem(name: "redirect_uri", value: "\(kGoogleReversedClientID):/oauth2callback"),
            URLQueryItem(name: "response_type", value: "code"),
            URLQueryItem(name: "scope", value: "openid email profile"),
            URLQueryItem(name: "code_challenge", value: challenge),
            URLQueryItem(name: "code_challenge_method", value: "S256"),
            URLQueryItem(name: "state", value: state),
        ]

        guard let authURL = components.url else {
            throw GoogleAuthError.invalidURL
        }

        let callbackScheme = kGoogleReversedClientID
        let code = try await withCheckedThrowingContinuation { (continuation: CheckedContinuation<String, Error>) in
            let session = ASWebAuthenticationSession(url: authURL, callbackURLScheme: callbackScheme) { callbackURL, error in
                if let error = error {
                    continuation.resume(throwing: error)
                    return
                }
                guard let callbackURL = callbackURL,
                      let components = URLComponents(url: callbackURL, resolvingAgainstBaseURL: false),
                      let code = components.queryItems?.first(where: { $0.name == "code" })?.value else {
                    continuation.resume(throwing: GoogleAuthError.noCode)
                    return
                }
                continuation.resume(returning: code)
            }
            session.presentationContextProvider = self
            session.prefersEphemeralWebBrowserSession = true
            self.currentSession = session
            session.start()
        }

        return try await exchangeCodeForIdToken(code: code, verifier: verifier)
    }

    private func exchangeCodeForIdToken(code: String, verifier: String) async throws -> String {
        var request = URLRequest(url: URL(string: kGoogleTokenEndpoint)!)
        request.httpMethod = "POST"
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
        let body = [
            "code": code,
            "client_id": kGoogleClientID,
            "redirect_uri": "\(kGoogleReversedClientID):/oauth2callback",
            "grant_type": "authorization_code",
            "code_verifier": verifier,
        ].map { "\($0.key)=\($0.value.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? $0.value)" }
         .joined(separator: "&")
        request.httpBody = Data(body.utf8)

        let (data, _) = try await URLSession.shared.data(for: request)
        let json = try JSONSerialization.jsonObject(with: data) as? [String: Any]
        guard let idToken = json?["id_token"] as? String else {
            throw GoogleAuthError.noIdToken
        }
        return idToken
    }

    private func generateCodeVerifier() -> String {
        var bytes = [UInt8](repeating: 0, count: 32)
        _ = SecRandomCopyBytes(kSecRandomDefault, bytes.count, &bytes)
        return Data(bytes).base64EncodedString()
            .replacingOccurrences(of: "+", with: "-")
            .replacingOccurrences(of: "/", with: "_")
            .replacingOccurrences(of: "=", with: "")
    }

    private func generateCodeChallenge(from verifier: String) -> String {
        let data = Data(verifier.utf8)
        let hash = SHA256.hash(data: data)
        return Data(hash).base64EncodedString()
            .replacingOccurrences(of: "+", with: "-")
            .replacingOccurrences(of: "/", with: "_")
            .replacingOccurrences(of: "=", with: "")
    }
}

public enum GoogleAuthError: LocalizedError {
    case invalidURL
    case noCode
    case noIdToken
    case cancelled

    public var errorDescription: String? {
        switch self {
        case .invalidURL: return "Invalid auth URL"
        case .noCode: return "No authorization code received"
        case .noIdToken: return "No ID token in response"
        case .cancelled: return "Sign-in was cancelled"
        }
    }
}
