package com.teamname.furniviz.auth;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;

    public LoginFrame() {
        super("Furniture Visualizer - Login");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(
                this::handleLoginSuccess,
                () -> cardLayout.show(cardPanel, "REGISTER")
        );

        registrationPanel = new RegistrationPanel(
                () -> cardLayout.show(cardPanel, "LOGIN"),
                () -> cardLayout.show(cardPanel, "LOGIN")
        );

        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(registrationPanel, "REGISTER");

        add(cardPanel, BorderLayout.CENTER);

        cardLayout.show(cardPanel, "LOGIN");
    }

    private void handleLoginSuccess() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Get the current logged-in user
                com.teamname.furniviz.accounts.User user = Session.getInstance().getCurrentUser();
                
                if (user != null) {
                    System.out.println("[OK] Transitioning to MainFrame for user: " + user.getEmail());
                    
                    // Create and show the main application frame
                    com.teamname.furniviz.app.MainFrame mainFrame = new com.teamname.furniviz.app.MainFrame();
                    mainFrame.setVisible(true);
                    
                    // Close the login frame
                    LoginFrame.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Error: User session not found.",
                        "Login Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Failed to transition to MainFrame: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Error opening main application: " + e.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void showLoginPanel() {
        cardLayout.show(cardPanel, "LOGIN");
        loginPanel.clearForm();
    }

    public void showRegistrationPanel() {
        cardLayout.show(cardPanel, "REGISTER");
        registrationPanel.clearForm();
    }
}
