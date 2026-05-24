import Foundation

public struct Message: Codable, Identifiable, Hashable {
    public enum Kind: String, Codable {
        case text, image
        case workoutInvite = "workout_invite"
    }

    public let id: String
    public let matchId: String
    public let senderId: String
    public let kind: Kind
    public let text: String?
    public let imageUrl: String?
    public let payload: [String: AnyCodable]?
    public let createdAt: String
    public let readAt: String?
}

public struct MessageListResponse: Codable {
    public let items: [Message]
    public let nextCursor: String?
}

/// Type-erased Codable wrapper for JSON payloads.
public struct AnyCodable: Codable, Hashable {
    public let value: Any

    public init(_ value: Any) { self.value = value }

    public init(from decoder: Decoder) throws {
        let c = try decoder.singleValueContainer()
        if c.decodeNil() { value = NSNull() }
        else if let v = try? c.decode(Bool.self) { value = v }
        else if let v = try? c.decode(Int.self) { value = v }
        else if let v = try? c.decode(Double.self) { value = v }
        else if let v = try? c.decode(String.self) { value = v }
        else if let v = try? c.decode([AnyCodable].self) { value = v.map(\.value) }
        else if let v = try? c.decode([String: AnyCodable].self) { value = v.mapValues(\.value) }
        else { value = NSNull() }
    }

    public func encode(to encoder: Encoder) throws {
        var c = encoder.singleValueContainer()
        switch value {
        case let v as Bool: try c.encode(v)
        case let v as Int: try c.encode(v)
        case let v as Double: try c.encode(v)
        case let v as String: try c.encode(v)
        case let v as [Any]: try c.encode(v.map(AnyCodable.init))
        case let v as [String: Any]: try c.encode(v.mapValues(AnyCodable.init))
        default: try c.encodeNil()
        }
    }

    public func hash(into hasher: inout Hasher) {
        hasher.combine(String(describing: value))
    }

    public static func == (lhs: AnyCodable, rhs: AnyCodable) -> Bool {
        String(describing: lhs.value) == String(describing: rhs.value)
    }
}
