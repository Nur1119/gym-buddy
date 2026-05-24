import SwiftUI

public struct MedalsView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        let unlocked = SampleData.medals.filter { $0.unlocked }.count
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(spacing: 16) {
                        // Header summary
                        HStack(spacing: 14) {
                            HStack(alignment: .firstTextBaseline, spacing: 2) {
                                Text("\(unlocked)")
                                    .font(.system(size: 38, weight: .black))
                                    .foregroundStyle(theme.text)
                                Text("/\(SampleData.medals.count)")
                                    .font(.system(size: 18))
                                    .foregroundStyle(theme.textDim)
                            }
                            VStack(alignment: .leading, spacing: 6) {
                                AppProgressBar(value: Double(unlocked), max: Double(SampleData.medals.count), height: 8)
                                Text(lang == .ru ? "Разблокировано медалей" : "Medals unlocked")
                                    .font(.system(size: 11))
                                    .foregroundStyle(theme.textMuted)
                            }
                        }
                        .padding(.horizontal, 18)

                        let columns = [GridItem(.flexible(), spacing: 10), GridItem(.flexible(), spacing: 10)]
                        LazyVGrid(columns: columns, spacing: 10) {
                            ForEach(SampleData.medals) { m in
                                medalCard(m, lang: lang)
                            }
                        }
                        .padding(.horizontal, 18)

                        Spacer().frame(height: 24)
                    }
                    .padding(.vertical, 12)
                }
            }
            .navigationTitle(L("medals", lang))
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("back", lang)) { dismiss() }.foregroundStyle(theme.p2)
                }
            }
        }
    }

    @ViewBuilder
    private func medalCard(_ m: SampleData.Medal, lang: AppLanguage) -> some View {
        AppCard(padding: 16) {
            VStack(spacing: 10) {
                ZStack {
                    Circle().fill(m.unlocked ? AnyShapeStyle(theme.gradient) : AnyShapeStyle(theme.chip))
                    Image(systemName: m.icon)
                        .font(.system(size: 30))
                        .foregroundStyle(.white)
                        .grayscale(m.unlocked ? 0 : 1)
                }
                .frame(width: 60, height: 60)
                .shadow(color: m.unlocked ? theme.p2.opacity(0.2) : .clear, radius: 8, x: 0, y: 6)

                Text(m.name)
                    .font(.system(size: 12, weight: .bold))
                    .foregroundStyle(m.unlocked ? theme.text : theme.textDim)
                    .multilineTextAlignment(.center)

                if !m.unlocked {
                    Image(systemName: "lock.fill")
                        .font(.system(size: 10))
                        .foregroundStyle(theme.textDim)
                }
            }
            .frame(maxWidth: .infinity)
        }
    }
}
