import Foundation

public enum APIError: LocalizedError {
    case badURL
    case http(status: Int, message: String?)
    case decoding(Error)
    case transport(Error)
    case noToken

    public var errorDescription: String? {
        switch self {
        case .badURL: return "Bad URL"
        case .http(let s, let m): return "HTTP \(s): \(m ?? "")"
        case .decoding(let e): return "Decode: \(e.localizedDescription)"
        case .transport(let e): return e.localizedDescription
        case .noToken: return "Not authenticated"
        }
    }
}

private struct APIErrorBody: Decodable {
    struct ErrorInner: Decodable { let code: String?; let message: String? }
    let error: ErrorInner
}

public final class APIClient {
    public static let shared = APIClient()

    public let baseURL: URL
    private let session: URLSession
    private let encoder: JSONEncoder
    private let decoder: JSONDecoder

    public init(baseURL: URL? = nil, session: URLSession = .shared) {
        if let baseURL = baseURL {
            self.baseURL = baseURL
        } else if let s = Bundle.main.object(forInfoDictionaryKey: "API_BASE_URL") as? String,
                  let url = URL(string: s) {
            self.baseURL = url
        } else {
            self.baseURL = URL(string: "http://localhost:3000/api/v1")!
        }
        self.session = session
        self.encoder = JSONEncoder()
        self.encoder.keyEncodingStrategy = .useDefaultKeys
        self.decoder = JSONDecoder()
        self.decoder.keyDecodingStrategy = .useDefaultKeys
    }

    // MARK: - Core request
    public func send<T: Decodable>(
        _ endpoint: Endpoint,
        body: Encodable? = nil,
        authenticated: Bool = true
    ) async throws -> T {
        let data = try await raw(endpoint, body: body, authenticated: authenticated)
        if T.self == EmptyResponse.self {
            return EmptyResponse() as! T
        }
        do {
            return try decoder.decode(T.self, from: data)
        } catch {
            throw APIError.decoding(error)
        }
    }

    public func raw(
        _ endpoint: Endpoint,
        body: Encodable? = nil,
        authenticated: Bool = true
    ) async throws -> Data {
        guard let url = URL(string: endpoint.path, relativeTo: baseURL) else {
            throw APIError.badURL
        }
        var req = URLRequest(url: url)
        req.httpMethod = endpoint.method.rawValue
        req.setValue("application/json", forHTTPHeaderField: "Content-Type")
        req.setValue("application/json", forHTTPHeaderField: "Accept")
        if authenticated {
            guard let token = TokenStore.shared.load() else {
                throw APIError.noToken
            }
            req.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        }
        if let body = body {
            req.httpBody = try encoder.encode(AnyEncodable(body))
        }

        let (data, resp): (Data, URLResponse)
        do {
            (data, resp) = try await session.data(for: req)
        } catch {
            throw APIError.transport(error)
        }
        guard let http = resp as? HTTPURLResponse else {
            throw APIError.http(status: -1, message: nil)
        }
        if !(200..<300).contains(http.statusCode) {
            let msg = (try? JSONDecoder().decode(APIErrorBody.self, from: data))?.error.message
            throw APIError.http(status: http.statusCode, message: msg)
        }
        return data
    }

    // MARK: - Auth convenience
    public func register(email: String, password: String, name: String, age: Int) async throws -> AuthResponse {
        struct Body: Encodable { let email: String; let password: String; let name: String; let age: Int }
        let res: AuthResponse = try await send(.register, body: Body(email: email, password: password, name: name, age: age), authenticated: false)
        TokenStore.shared.save(res.token)
        return res
    }

    public func login(email: String, password: String) async throws -> AuthResponse {
        struct Body: Encodable { let email: String; let password: String }
        let res: AuthResponse = try await send(.login, body: Body(email: email, password: password), authenticated: false)
        TokenStore.shared.save(res.token)
        return res
    }

    public func loginWithGoogle(idToken: String) async throws -> AuthResponse {
        struct Body: Encodable { let idToken: String }
        let res: AuthResponse = try await send(.googleAuth, body: Body(idToken: idToken), authenticated: false)
        TokenStore.shared.save(res.token)
        return res
    }

    public func currentUser() async throws -> User {
        try await send(.me)
    }

    public func logout() {
        TokenStore.shared.clear()
    }

    // MARK: - Discover
    public func discoverFeed(limit: Int = 10) async throws -> [User] {
        let res: DiscoverFeed = try await send(.discoverFeed(limit: limit))
        return res.items
    }

    public func swipe(userId: String, direction: SwipeRequest.Direction) async throws -> SwipeResponse {
        try await send(.discoverSwipe, body: SwipeRequest(targetUserId: userId, direction: direction))
    }

    public func filters() async throws -> DiscoverFilters {
        try await send(.discoverFilters)
    }

    public func updateFilters(_ filters: DiscoverFilters) async throws -> DiscoverFilters {
        try await send(.discoverFiltersPut, body: filters)
    }

    // MARK: - Matches
    public func matches() async throws -> [Match] {
        let res: MatchListResponse = try await send(.matches)
        return res.items
    }

    // MARK: - Messages
    public func messages(matchId: String) async throws -> [Message] {
        let res: MessageListResponse = try await send(.messages(matchId: matchId))
        return res.items
    }

    // MARK: - Workouts
    public func workouts() async throws -> [Workout] {
        let res: WorkoutListResponse = try await send(.workouts)
        return res.items
    }

    public func workoutStats() async throws -> WorkoutStats {
        try await send(.workoutStats)
    }

    // MARK: - Exercises
    public func exercises(muscle: String? = nil, search: String? = nil) async throws -> [Exercise] {
        let res: ExerciseListResponse = try await send(.exercises(muscle: muscle, search: search))
        return res.items
    }

    // MARK: - Routines
    public func routines() async throws -> [Routine] {
        let res: RoutineListResponse = try await send(.routines)
        return res.items
    }
}

public struct EmptyResponse: Decodable {}

/// Helper to encode any Encodable as a heterogeneous value.
struct AnyEncodable: Encodable {
    private let _encode: (Encoder) throws -> Void
    init<T: Encodable>(_ value: T) { _encode = value.encode }
    func encode(to encoder: Encoder) throws { try _encode(encoder) }
}
