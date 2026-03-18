package com.teamname.furniviz.auth;

import com.teamname.furniviz.common.util.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private JLabel errorLabel;

    private AuthService authService;
    private Runnable onLoginSuccess;
    private Runnable onSignUpClick;

    public LoginPanel(Runnable onLoginSuccess, Runnable onSignUpClick) {
        this.onLoginSuccess = onLoginSuccess;
        this.onSignUpClick = onSignUpClick;
        this.authService = new AuthService();

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = createFormPanel();
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(formPanel, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(emailLabel);
        emailField = new JTextField(25);
        emailField.setFont(new Font("Arial", Font.PLAIN, 12));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                validateEmail();
            }
        });
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(10));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(passwordLabel);
        passwordField = new JPasswordField(25);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(20));

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        errorLabel.setVisible(false);
        panel.add(errorLabel);
        panel.add(Box.createVerticalStrut(10));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.addActionListener(e -> handleLogin());
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(15));

        JPanel signUpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signUpPanel.setBackground(Color.WHITE);
        signUpPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JLabel signUpLabel = new JLabel("Don't have an account? ");
        signUpLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        signUpButton = new JButton("Sign Up");
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setForeground(new Color(0, 102, 204));
        signUpButton.setFont(new Font("Arial", Font.BOLD, 11));
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.addActionListener(e -> onSignUpClick.run());
        signUpPanel.add(signUpLabel);
        signUpPanel.add(signUpButton);
        panel.add(signUpPanel);

        return panel;
    }

    private void validateEmail() {
        String email = emailField.getText().trim();
        String error = ValidationUtil.getEmailErrorMessage(email);
        if (error != null && !email.isEmpty()) {
            showError(error);
        } else {
            clearError();
        }
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty()) {
            showError("Email is required");
            return;
        }
        if (password.isEmpty()) {
            showError("Password is required");
            return;
        }

        try {
            loginButton.setEnabled(false);
            loginButton.setText("Logging in...");

            com.teamname.furniviz.accounts.User user = authService.login(email, password);
            Session.getInstance().setUser(user);

            clearError();
            showInfo("Login successful!");

            if (onLoginSuccess != null) {
                SwingUtilities.invokeLater(onLoginSuccess);
            }
        } catch (Exception e) {
            showError(e.getMessage());
        } finally {
            loginButton.setEnabled(true);
            loginButton.setText("Login");
        }
    }

    private void showError(String message) {
        errorLabel.setText("[ERROR] " + message);
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(true);
    }

    private void showInfo(String message) {
        errorLabel.setText("[OK] " + message);
        errorLabel.setForeground(new Color(0, 153, 0));
        errorLabel.setVisible(true);
    }

    private void clearError() {
        errorLabel.setVisible(false);
    }

    public void clearForm() {
        emailField.setText("");
        passwordField.setText("");
        clearError();
    }
}
