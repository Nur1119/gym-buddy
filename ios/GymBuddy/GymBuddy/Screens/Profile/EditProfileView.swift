import SwiftUI
import UIKit

public struct EditProfileView: View {
    @EnvironmentObject var themeManager: ThemeManager
    @EnvironmentObject var auth: AuthViewModel
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    @State private var name: String = ""
    @State private var age: String = ""
    @State private var height: String = ""
    @State private var weight: String = ""
    @State private var bio: String = ""

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(alignment: .leading, spacing: 14) {
                        photoGrid()
                            .padding(.bottom, 4)

                        field(L("name", lang), $name)

                        HStack(spacing: 10) {
                            field(L("age", lang), $age, keyboard: .numberPad)
                            field("\(L("height", lang)) (cm)", $height, keyboard: .numberPad)
                            field("\(L("weightLabel", lang)) (kg)", $weight, keyboard: .numberPad)
                        }

                        field(L("bio", lang), $bio, multiline: true)

                        Text(L("goal", lang).uppercased())
                            .font(.system(size: 12, weight: .bold)).tracking(0.5).foregroundStyle(theme.textDim)
                            .padding(.leading, 4)
                        FlexibleHStack(items: Goal.allCases.map { $0 }) { g in
                            Pill(g.rawValue, active: g == .hypertrophy, color: theme.p2)
                        }

                        Text((lang == .ru ? "Интересы" : "Interests").uppercased())
                            .font(.system(size: 12, weight: .bold)).tracking(0.5).foregroundStyle(theme.textDim)
                            .padding(.leading, 4)
                        FlexibleHStack(items: ["Powerlifting","Bodybuilding","Calisthenics","Running","Yoga","CrossFit","HIIT"]) { item in
                            Pill(item, active: ["Powerlifting","Bodybuilding","Calisthenics"].contains(item), color: theme.p3)
                        }
                    }
                    .padding(.horizontal, 18)
                    .padding(.vertical, 12)
                }
            }
            .navigationTitle(L("editProfile", lang))
            .navigationBarTitleDisplayMode(.large)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) { Button(L("cancel", lang)) { dismiss() }.foregroundStyle(theme.p2) }
                ToolbarItem(placement: .topBarTrailing) { Button(L("save", lang)) { dismiss() }.foregroundStyle(theme.p2).fontWeight(.bold) }
            }
            .onAppear {
                let u = auth.user ?? .preview
                name = u.name
                age = "\(u.age)"; height = "\(u.height)"; weight = "\(u.weight)"; bio = u.bio
            }
        }
    }

    @ViewBuilder
    private func photoGrid() -> some View {
        let columns = [GridItem(.flexible(), spacing: 8), GridItem(.flexible(), spacing: 8), GridItem(.flexible(), spacing: 8)]
        LazyVGrid(columns: columns, spacing: 8) {
            ForEach(0..<6, id: \.self) { i in
                if i < 2 {
                    PhotoSlot(color1: theme.p3, color2: theme.p2, label: "photo \(i+1)", cornerRadius: 12)
                        .aspectRatio(3/4, contentMode: .fit)
                        .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
                } else {
                    Button {
                        // TODO: photo picker
                    } label: {
                        ZStack {
                            RoundedRectangle(cornerRadius: 12).fill(theme.surface)
                            AppIcon(.plus, size: 20, color: theme.textDim)
                        }
                        .aspectRatio(3/4, contentMode: .fit)
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(theme.borderStrong, style: StrokeStyle(lineWidth: 1, dash: [4,4]))
                        )
                    }
                    .buttonStyle(.plain)
                }
            }
        }
    }

    @ViewBuilder
    private func field(_ label: String, _ value: Binding<String>, multiline: Bool = false, keyboard: UIKeyboardType = .default) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(label.uppercased())
                .font(.system(size: 12, weight: .bold))
                .tracking(0.5)
                .foregroundStyle(theme.textDim)
                .padding(.leading, 4)
            if multiline {
                TextEditor(text: value)
                    .frame(minHeight: 80)
                    .padding(8)
                    .scrollContentBackground(.hidden)
                    .background(theme.surface)
                    .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                    .foregroundStyle(theme.text)
            } else {
                TextField("", text: value)
                    .keyboardType(keyboard)
                    .padding(.horizontal, 16).padding(.vertical, 14)
                    .background(theme.surface)
                    .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                    .foregroundStyle(theme.text)
            }
        }
    }
}
