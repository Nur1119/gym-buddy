import SwiftUI
import UIKit

public struct RegisterView: View {
    @EnvironmentObject var auth: AuthViewModel
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme
    @Environment(\.dismiss) private var dismiss

    @State private var email = ""
    @State private var password = ""
    @State private var confirmPassword = ""
    @State private var name = ""
    @State private var localError: String? = nil

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        NavigationStack {
            ZStack {
                theme.bgGradient.ignoresSafeArea()
                ScrollView {
                    VStack(alignment: .leading, spacing: 16) {
                        Text(L("registerTitle", lang))
                            .font(.system(size: 28, weight: .heavy))
                            .foregroundStyle(theme.text)
                            .padding(.top, 18)

                        field(L("name", lang), $name)
                        field(L("email", lang), $email, keyboard: .emailAddress)
                        field(L("password", lang), $password, isSecure: true)
                        field(L("confirmPassword", lang), $confirmPassword, isSecure: true)

                        let displayError = localError ?? auth.errorMessage
                        if let err = displayError {
                            Text(err)
                                .font(.system(size: 12, weight: .semibold))
                                .foregroundStyle(theme.danger)
                        }

                        GradientButton(L("signUp", lang), icon: .check) {
                            guard password == confirmPassword else {
                                localError = "Passwords do not match"
                                return
                            }
                            localError = nil
                            Task {
                                await auth.register(email: email, password: password,
                                                    name: name, age: 0)
                                if auth.user != nil { dismiss() }
                            }
                        }
                        .padding(.top, 10)
                    }
                    .padding(.horizontal, 18)
                }
                .scrollDismissesKeyboard(.interactively)
            }
            .navigationTitle("")
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button(L("cancel", lang)) { dismiss() }
                        .foregroundStyle(theme.p2)
                }
            }
        }
    }

    @ViewBuilder
    private func field(_ label: String, _ value: Binding<String>, isSecure: Bool = false, keyboard: UIKeyboardType = .default) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            Text(label.uppercased())
                .font(.system(size: 12, weight: .semibold))
                .foregroundStyle(theme.textDim)
                .tracking(0.5)
            Group {
                if isSecure {
                    SecureField("", text: value)
                } else {
                    TextField("", text: value)
                        .keyboardType(keyboard)
                        .textInputAutocapitalization(.never)
                        .autocorrectionDisabled(true)
                }
            }
            .padding(.horizontal, 16).padding(.vertical, 14)
            .background(theme.surface)
            .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .foregroundStyle(theme.text)
        }
    }
}
