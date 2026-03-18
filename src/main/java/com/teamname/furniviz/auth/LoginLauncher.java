package com.teamname.furniviz.auth;

import javax.swing.SwingUtilities;

public class LoginLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

