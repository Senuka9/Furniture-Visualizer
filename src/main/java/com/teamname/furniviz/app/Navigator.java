package com.teamname.furniviz.app;

import com.teamname.furniviz.auth.Session;
import com.teamname.furniviz.auth.LoginFrame;
import com.teamname.furniviz.room.RoomFormPanel;
import com.teamname.furniviz.room.RoomController;
import com.teamname.furniviz.room.RoomTemplatesPage;
import com.teamname.furniviz.furniture.FurnitureLibraryPanel;
import com.teamname.furniviz.editor2d.Editor2DPanel;
import com.teamname.furniviz.editor2d.RoomSelectorPanel;
import com.teamname.furniviz.renderer3d.View3DPanel;
import com.teamname.furniviz.portfolio.PortfolioPanel;

import javax.swing.*;
import java.awt.*;

public class Navigator extends JPanel {

    private CardLayout layout;
    private DesignState designState;
    private RoomFormPanel roomFormPanel;
    private RoomTemplatesPage roomTemplatesPage;
    private FurnitureLibraryPanel furnitureLibraryPanel;
    private Editor2DPanel editor2DPanel;
    private RoomSelectorPanel roomSelectorPanel;
    private View3DPanel view3DPanel;
    private PortfolioPanel portfolioPanel;

    public Navigator() {
        layout = new CardLayout();
        setLayout(layout);

        this.designState = new DesignState();

        JPanel homePanel = createHomePanel();
        add(homePanel, "HOME");

        RoomController roomController = new RoomController(designState);
        this.roomFormPanel = new RoomFormPanel(roomController, () -> showHome(), () -> showRooms());
        add(roomFormPanel, "ROOM");

        this.roomTemplatesPage = new RoomTemplatesPage(
                () -> showHome(),
                room -> showEditor2D(room)
        );
        add(roomTemplatesPage, "ROOMS");

        this.furnitureLibraryPanel = new FurnitureLibraryPanel(
                designState,
                () -> showHome(),
                () -> show2D()
        );
        add(furnitureLibraryPanel, "FURNITURE");

        this.editor2DPanel = new Editor2DPanel(
                designState,
                () -> showHome(),
                () -> show3D()
        );
        add(editor2DPanel, "EDITOR_2D");

        this.roomSelectorPanel = new RoomSelectorPanel(
                room -> showEditor2D(room),
                () -> showHome()
        );
        add(roomSelectorPanel, "ROOM_SELECTOR_2D");

        this.view3DPanel = new View3DPanel(
                designState,
                () -> show2D()
        );
        add(view3DPanel, "VIEW_3D");

        this.portfolioPanel = new PortfolioPanel(
                () -> showHome(),
                this::refreshPortfolio
        );
        add(portfolioPanel, "PORTFOLIO");
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // NORTH: User info and logout
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(245, 245, 245));
        
        com.teamname.furniviz.accounts.User currentUser = Session.getInstance().getCurrentUser();
        String userName = currentUser != null ? currentUser.getFullName() : "User";
        JLabel userLabel = new JLabel("Welcome, " + userName);
        userLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(userLabel, BorderLayout.WEST);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutBtn.addActionListener(e -> handleLogout());
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        
        panel.add(headerPanel, BorderLayout.NORTH);

        // CENTER: Title and buttons
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setBackground(new Color(245, 245, 245));
        
        JLabel title = new JLabel("Furniture Visualizer", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        centerPanel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = createButtonPanel();
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(new Color(245, 245, 245));

        JButton roomButton = new JButton("Create Room");
        JButton roomsButton = new JButton("Rooms");
        JButton furnitureButton = new JButton("Furniture Library");
        JButton editor2DButton = new JButton("2D Editor");
        JButton view3DButton = new JButton("3D View");
        JButton portfolioButton = new JButton("Portfolio");

        panel.add(roomButton);
        panel.add(roomsButton);
        panel.add(furnitureButton);
        panel.add(editor2DButton);
        panel.add(view3DButton);
        panel.add(portfolioButton);

        roomButton.addActionListener(e -> showRoom());
        roomsButton.addActionListener(e -> showRooms());
        furnitureButton.addActionListener(e -> showFurniture());
        editor2DButton.addActionListener(e -> showRoomSelector());
        view3DButton.addActionListener(e -> show3D());
        portfolioButton.addActionListener(e -> showPortfolio());

        return panel;
    }

    private void handleLogout() {
        Session.getInstance().logout();
        SwingUtilities.invokeLater(() -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    public DesignState getDesignState() {
        return designState;
    }

    public void showHome() {
        layout.show(this, "HOME");
    }

    public void showRoom() {
        layout.show(this, "ROOM");
    }

    public void showRooms() {
        roomTemplatesPage.refresh();
        layout.show(this, "ROOMS");
    }

    public void showFurniture() {
        layout.show(this, "FURNITURE");
    }

    public void showRoomSelector() {
        roomSelectorPanel.refresh();
        layout.show(this, "ROOM_SELECTOR_2D");
    }

    public void show2D() {
        layout.show(this, "EDITOR_2D");
    }

    public void showEditor2D(com.teamname.furniviz.room.Room room) {
        designState.setRoom(room);
        editor2DPanel.setRoom(room);
        layout.show(this, "EDITOR_2D");
    }

    public void show3D() {
        if (view3DPanel != null) {
            view3DPanel.refresh();
        }
        layout.show(this, "VIEW_3D");
    }

    public void showPortfolio() {
        layout.show(this, "PORTFOLIO");
    }

    public void refreshPortfolio() {
        // Called when portfolio needs to be refreshed
        // (e.g., after loading a design from portfolio)
    }
}
