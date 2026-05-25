import SwiftUI
import UIKit

public struct LoginView: View {
    @EnvironmentObject var auth: AuthViewModel
    @EnvironmentObject var themeManager: ThemeManager
    @Environment(\.appTheme) private var theme

    @State private var email = "alex@example.com"
    @State private var password = "secret123"
    @State private var showRegister = false

    public init() {}

    public var body: some View {
        let lang = themeManager.language
        ZStack {
            theme.bgGradient.ignoresSafeArea()

            ScrollView {
                VStack(spacing: 20) {

                    // Gradient logo
                    Text("GymBuddy")
                        .font(.system(size: 38, weight: .black))
                        .foregroundStyle(theme.gradient)
                        .padding(.top, 60)

                    Text(L("loginTitle", lang))
                        .font(.system(size: 22, weight: .heavy))
                        .foregroundStyle(theme.text)
                        .padding(.top, 4)

                    Text(L("readyToTrain", lang))
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(theme.textMuted)
                        .padding(.bottom, 12)

                    VStack(alignment: .leading, spacing: 14) {
                        labeledField(L("email", lang), $email, keyboard: .emailAddress)
                        labeledField(L("password", lang), $password, isSecure: true)
                    }
                    .padding(.horizontal, 18)

                    if let err = auth.errorMessage {
                        Text(err)
                            .font(.system(size: 12, weight: .semibold))
                            .foregroundStyle(theme.danger)
                            .padding(.horizontal, 18)
                    }

                    GradientButton(L("signIn", lang), icon: .arrowRight) {
                        Task { await auth.login(email: email, password: password) }
                    }
                    .padding(.horizontal, 18)
                    .padding(.top, 6)

                    Button {
                        Task { await auth.loginWithGoogle() }
                    } label: {
                        HStack(spacing: 10) {
                            Text("G")
                                .font(.system(size: 18, weight: .bold))
                                .foregroundStyle(.white)
                                .frame(width: 28, height: 28)
                                .background(Color(red: 0.84, green: 0.18, blue: 0.13))
                                .clipShape(Circle())
                            Text(L("continueWithGoogle", lang))
                                .font(.system(size: 15, weight: .semibold))
                                .foregroundStyle(theme.text)
                        }
                        .frame(maxWidth: .infinity)
                        .padding(.horizontal, 16)
                        .padding(.vertical, 14)
                        .background(theme.surface)
                        .overlay(RoundedRectangle(cornerRadius: 12).stroke(theme.border, lineWidth: 1))
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                    }
                    .buttonStyle(.plain)
                    .padding(.horizontal, 18)
                    .padding(.top, 6)

                    Button {
                        showRegister = true
                    } label: {
                        HStack(spacing: 4) {
                            Text(L("noAccount", lang)).foregroundStyle(theme.textMuted)
                            Text(L("signUp", lang)).foregroundStyle(theme.p2).fontWeight(.semibold)
                        }
                        .font(.system(size: 14, weight: .medium))
                    }
                    .buttonStyle(.plain)
                    .padding(.top, 14)

                    Spacer()
                }
            }
            .scrollDismissesKeyboard(.interactively)
        }
        .sheet(isPresented: $showRegister) {
            RegisterView().environmentObject(auth).environmentObject(themeManager)
        }
    }

    @ViewBuilder
    private func labeledField(_ label: String, _ value: Binding<String>, isSecure: Bool = false, keyboard: UIKeyboardType = .default) -> some View {
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
