package com.teamname.furniviz.app;

import javax.swing.*;
import java.awt.*;

public class Navigator extends JPanel {

    private CardLayout layout;

    public Navigator() {
        layout = new CardLayout();
        setLayout(layout);

        add(new HomePanel(this), "HOME");
    }

    public void showHome() {
        layout.show(this, "HOME");
    }

    // Later you will add:
    // showRoom()
    // show2D()
    // show3D()
    // showPortfolio()
}
