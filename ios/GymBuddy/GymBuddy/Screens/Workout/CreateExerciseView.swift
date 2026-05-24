import SwiftUI

public struct CreateExerciseView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    @State private var name: String = ""
    @State private var muscle: Muscle = .chest
    @State private var equipment: Equipment = .barbell
    @State private var notes: String = ""

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(alignment: .leading, spacing: 14) {
                        // Image picker placeholder
                        Button {
                            // TODO: photo picker
                        } label: {
                            VStack(spacing: 6) {
                                AppIcon(.camera, size: 28, color: theme.textDim)
                                Text(lang == .ru ? "Добавить фото" : "Add photo")
                                    .font(.system(size: 13, weight: .semibold))
                                    .foregroundStyle(theme.textMuted)
                            }
                            .frame(maxWidth: .infinity)
                            .frame(height: 120)
                            .background(theme.surface)
                            .overlay(
                                RoundedRectangle(cornerRadius: 16)
                                    .stroke(theme.borderStrong, style: StrokeStyle(lineWidth: 1, dash: [4,4]))
                            )
                            .clipShape(RoundedRectangle(cornerRadius: 16))
                        }
                        .buttonStyle(.plain)

                        labeledField(L("name", lang), $name,
                                     placeholder: lang == .ru ? "Жим узким хватом" : "Close-grip bench")

                        sectionLabel(lang == .ru ? "Группа мышц" : "Muscle group")
                        FlexibleHStack(items: Muscle.allCases.map { $0 }) { m in
                            Pill(m.rawValue, active: muscle == m, color: theme.p2) { muscle = m }
                        }

                        sectionLabel(lang == .ru ? "Оборудование" : "Equipment")
                        FlexibleHStack(items: Equipment.allCases.map { $0 }) { e in
                            Pill(e.rawValue, active: equipment == e, color: theme.p1) { equipment = e }
                        }

                        sectionLabel(lang == .ru ? "Заметки" : "Notes")
                        TextEditor(text: $notes)
                            .frame(minHeight: 80)
                            .padding(8)
                            .background(theme.surface)
                            .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
                            .clipShape(RoundedRectangle(cornerRadius: 12))
                            .foregroundStyle(theme.text)
                            .scrollContentBackground(.hidden)

                        GradientButton(lang == .ru ? "Создать упражнение" : "Create exercise", icon: .check) {
                            dismiss()
                        }
                    }
                    .padding(.horizontal, 18)
                    .padding(.bottom, 24)
                }
            }
            .navigationTitle(L("createCustom", lang))
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("cancel", lang)) { dismiss() }.foregroundStyle(theme.p2)
                }
                ToolbarItem(placement: .topBarTrailing) {
                    Button(L("save", lang)) { dismiss() }.foregroundStyle(theme.p2).fontWeight(.bold)
                }
            }
        }
    }

    @ViewBuilder
    private func sectionLabel(_ text: String) -> some View {
        Text(text.uppercased())
            .font(.system(size: 12, weight: .bold))
            .tracking(0.5)
            .foregroundStyle(theme.textDim)
            .padding(.leading, 4)
    }

    @ViewBuilder
    private func labeledField(_ label: String, _ value: Binding<String>, placeholder: String = "") -> some View {
        VStack(alignment: .leading, spacing: 6) {
            sectionLabel(label)
            TextField(placeholder, text: value)
                .padding(.horizontal, 16).padding(.vertical, 14)
                .background(theme.surface)
                .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
                .clipShape(RoundedRectangle(cornerRadius: 12))
                .foregroundStyle(theme.text)
        }
    }
}
