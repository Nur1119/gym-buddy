import Foundation

public enum WSEvent {
    case message(Message)
    case typing(userId: String)
    case read(messageId: String)
    case unknown
}

/// Lightweight WebSocket wrapper for chat. Reconnects manually; caller drives the loop.
public final class WebSocketClient: NSObject {
    private var task: URLSessionWebSocketTask?
    private let session: URLSession
    private let decoder = JSONDecoder()
    private var onEvent: ((WSEvent) -> Void)?

    public init(session: URLSession = .shared) {
        self.session = session
    }

    public func connect(matchId: String, token: String, onEvent: @escaping (WSEvent) -> Void) {
        self.onEvent = onEvent
        // Convert HTTP base to WS
        let base = APIClient.shared.baseURL.absoluteString
            .replacingOccurrences(of: "https://", with: "wss://")
            .replacingOccurrences(of: "http://", with: "ws://")
        guard let url = URL(string: "\(base)/matches/\(matchId)/stream") else { return }
        var req = URLRequest(url: url)
        req.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        task = session.webSocketTask(with: req)
        task?.resume()
        listen()
    }

    public func disconnect() {
        task?.cancel(with: .goingAway, reason: nil)
        task = nil
    }

    public func send(_ text: String) async throws {
        try await task?.send(.string(text))
    }

    private func listen() {
        task?.receive { [weak self] result in
            guard let self = self else { return }
            switch result {
            case .success(let msg):
                self.handle(msg)
                self.listen()
            case .failure:
                // Caller may reconnect on demand.
                break
            }
        }
    }

    private func handle(_ msg: URLSessionWebSocketTask.Message) {
        let data: Data
        switch msg {
        case .data(let d): data = d
        case .string(let s): data = Data(s.utf8)
        @unknown default: return
        }
        guard let json = try? JSONSerialization.jsonObject(with: data) as? [String: Any],
              let type = json["type"] as? String else { return }
        switch type {
        case "message":
            if let dataObj = json["data"],
               let raw = try? JSONSerialization.data(withJSONObject: dataObj),
               let m = try? decoder.decode(Message.self, from: raw) {
                onEvent?(.message(m))
            }
        case "typing":
            if let uid = json["userId"] as? String { onEvent?(.typing(userId: uid)) }
        case "read":
            if let mid = json["messageId"] as? String { onEvent?(.read(messageId: mid)) }
        default:
            onEvent?(.unknown)
        }
    }
}
