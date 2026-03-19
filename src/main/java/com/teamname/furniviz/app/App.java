package com.teamname.furniviz.app;

import com.teamname.furniviz.auth.LoginFrame;
import com.teamname.furniviz.auth.Session;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (Session.getInstance().isLoggedIn()) {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            } else {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}
