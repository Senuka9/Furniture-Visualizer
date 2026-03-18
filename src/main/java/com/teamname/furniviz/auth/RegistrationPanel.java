package com.teamname.furniviz.auth;

import com.teamname.furniviz.common.util.ValidationUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RegistrationPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JButton signUpButton;
    private JButton backButton;
    private JLabel errorLabel;
    private JLabel passwordStrengthLabel;

    private AuthService authService;
    private Runnable onSignUpSuccess;
    private Runnable onBackClick;

    public RegistrationPanel(Runnable onSignUpSuccess, Runnable onBackClick) {
        this.onSignUpSuccess = onSignUpSuccess;
        this.onBackClick = onBackClick;
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

        JLabel titleLabel = new JLabel("Create Account");
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

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(firstNameLabel);
        firstNameField = new JTextField(25);
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 12));
        firstNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(firstNameField);
        panel.add(Box.createVerticalStrut(10));

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lastNameLabel);
        lastNameField = new JTextField(25);
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 12));
        lastNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(lastNameField);
        panel.add(Box.createVerticalStrut(10));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(passwordLabel);
        passwordField = new JPasswordField(25);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updatePasswordStrength(); }
            public void removeUpdate(DocumentEvent e) { updatePasswordStrength(); }
            public void changedUpdate(DocumentEvent e) { updatePasswordStrength(); }
        });
        panel.add(passwordField);

        passwordStrengthLabel = new JLabel();
        passwordStrengthLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        panel.add(passwordStrengthLabel);
        panel.add(Box.createVerticalStrut(10));

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(confirmLabel);
        confirmPasswordField = new JPasswordField(25);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
        confirmPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(confirmPasswordField);
        panel.add(Box.createVerticalStrut(20));

        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        errorLabel.setVisible(false);
        panel.add(errorLabel);
        panel.add(Box.createVerticalStrut(10));

        signUpButton = new JButton("Create Account");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 12));
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setPreferredSize(new Dimension(150, 35));
        signUpButton.addActionListener(e -> handleSignUp());
        panel.add(signUpButton);
        panel.add(Box.createVerticalStrut(15));

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.setBackground(Color.WHITE);
        backPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        backButton = new JButton("Back to Login");
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(new Color(0, 102, 204));
        backButton.setFont(new Font("Arial", Font.BOLD, 11));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> onBackClick.run());
        backPanel.add(backButton);
        panel.add(backPanel);

        return panel;
    }

    private void updatePasswordStrength() {
        String password = new String(passwordField.getPassword());
        if (password.isEmpty()) {
            passwordStrengthLabel.setText("");
            return;
        }

        int strength = authService.getPasswordStrength(password);
        String strengthText;
        Color color;

        if (strength < 40) {
            strengthText = "Weak";
            color = Color.RED;
        } else if (strength < 70) {
            strengthText = "Medium";
            color = new Color(255, 153, 0);
        } else {
            strengthText = "Strong";
            color = new Color(0, 153, 0);
        }

        passwordStrengthLabel.setText("Password Strength: " + strengthText + " (" + strength + "%)");
        passwordStrengthLabel.setForeground(color);
    }

    private void validateEmail() {
        String email = emailField.getText().trim();
        String error = ValidationUtil.getEmailErrorMessage(email);
        if (error != null && !email.isEmpty()) {
            showError(error);
        }
    }

    private void handleSignUp() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
            firstName.isEmpty() || lastName.isEmpty()) {
            showError("All fields are required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        try {
            signUpButton.setEnabled(false);
            signUpButton.setText("Creating Account...");

            authService.register(email, password, firstName, lastName);

            clearError();
            showInfo("Account created successfully!");

            clearForm();

            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1500);
                    if (onSignUpSuccess != null) {
                        onSignUpSuccess.run();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        } catch (Exception e) {
            showError(e.getMessage());
        } finally {
            signUpButton.setEnabled(true);
            signUpButton.setText("Create Account");
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
        confirmPasswordField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        clearError();
        passwordStrengthLabel.setText("");
    }
}


