import Foundation
import SwiftUI

@MainActor
public final class DiscoverViewModel: ObservableObject {
    @Published public var stack: [User] = []
    @Published public var isLoading = false
    @Published public var matchedUser: User?
    @Published public var filters: DiscoverFilters = .default

    public init() {}

    public func load() async {
        isLoading = true
        defer { isLoading = false }
        do {
            stack = try await APIClient.shared.discoverFeed()
        } catch {
            // Fall back to demo users so the swipe UI is reachable.
            stack = SampleData.discoverUsers
        }
    }

    public func swipe(direction: SwipeRequest.Direction) async {
        guard let top = stack.first else { return }
        // Optimistically advance.
        stack.removeFirst()
        do {
            let res = try await APIClient.shared.swipe(userId: top.id, direction: direction)
            if res.matched {
                matchedUser = top
            }
        } catch {
            // Sample fallback: ~50% match on like
            if direction == .like && Bool.random() {
                matchedUser = top
            }
        }
        if stack.count < 3 {
            await load()
        }
    }
}
