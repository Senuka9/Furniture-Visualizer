package com.teamname.furniviz.app;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private Navigator navigator;

    public MainFrame() {
        setTitle("Furniture Visualizer");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        navigator = new Navigator();
        add(navigator, BorderLayout.CENTER);

        navigator.showHome();
    }
}
