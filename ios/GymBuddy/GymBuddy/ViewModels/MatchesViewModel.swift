import Foundation
import SwiftUI

@MainActor
public final class MatchesViewModel: ObservableObject {
    @Published public var matches: [Match] = []
    @Published public var isLoading = false

    public init() {}

    public func load() async {
        isLoading = true; defer { isLoading = false }
        do {
            matches = try await APIClient.shared.matches()
        } catch {
            matches = SampleData.sampleMatches
        }
    }
}

@MainActor
public final class ChatViewModel: ObservableObject {
    @Published public var messages: [Message] = []
    @Published public var draft: String = ""
    @Published public var isLoading = false

    public let match: Match
    private let ws = WebSocketClient()

    public init(match: Match) {
        self.match = match
    }

    public func load() async {
        isLoading = true; defer { isLoading = false }
        do {
            messages = try await APIClient.shared.messages(matchId: match.id).reversed()
        } catch {
            messages = SampleData.sampleMessages(for: match)
        }
        connect()
    }

    public func connect() {
        guard let token = TokenStore.shared.load() else { return }
        ws.connect(matchId: match.id, token: token) { [weak self] event in
            guard case .message(let m) = event else { return }
            Task { @MainActor in
                self?.messages.append(m)
            }
        }
    }

    public func disconnect() {
        ws.disconnect()
    }

    public func send() async {
        let text = draft.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !text.isEmpty else { return }
        draft = ""
        let local = Message(
            id: UUID().uuidString,
            matchId: match.id,
            senderId: "me",
            kind: .text,
            text: text,
            imageUrl: nil,
            payload: nil,
            createdAt: ISO8601DateFormatter().string(from: Date()),
            readAt: nil
        )
        messages.append(local)
        // Best-effort POST; ignore errors in demo mode.
        struct Body: Encodable { let text: String; let kind: String }
        _ = try? await APIClient.shared.raw(.sendMessage(matchId: match.id),
                                            body: Body(text: text, kind: "text"))
    }
}
