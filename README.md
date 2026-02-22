# Furniture Visualizer - Project Structure

## Overview

This project is organized to support a 6-member team developing a Java Swing-based furniture visualization application. The app allows users to:
1. Create and design rooms in 2D
2. Place furniture items with customizable properties
3. Automatically convert 2D designs to 3D visualization
4. Save/load designs and view portfolio

## Project Setup for Team Members

### Prerequisites
- Java 11+
- IntelliJ IDEA
- Maven (comes with IntelliJ)

### Getting Started

1. Clone/sync the repository
2. Open the project in IntelliJ IDEA (pom.xml will be detected automatically)
3. IntelliJ will download Maven dependencies automatically
4. Each team member works on their assigned package (see `docs/ownership.md`)

### Running Demo Tests

Each team member can test their module independently:

```bash
# From IntelliJ:
- Right-click on DemoLogin.java → Run 'DemoLogin.main()'
- Right-click on DemoRoom.java → Run 'DemoRoom.main()'
- Right-click on Demo2D.java → Run 'Demo2D.main()'
- Right-click on Demo3D.java → Run 'Demo3D.main()'
- Right-click on DemoStorage.java → Run 'DemoStorage.main()'
```

## Folder Structure

```
Furniture-Visualizer/
├── pom.xml                    (Maven configuration - DO NOT EDIT)
├── README.md                  (This file)
├── .gitignore                 (Git ignore file)
├── docs/
│   ├── ownership.md           (Team assignment & module owners)
│   ├── testing/               (Testing documentation)
│   └── screenshots/           (Optional proof/evidence)
└── src/
    ├── main/
    │   ├── java/com/teamname/furniviz/
    │   │   ├── app/               (Member 1)
    │   │   ├── auth/              (Member 1)
    │   │   ├── accounts/          (Member 1 - NEW: User management)
    │   │   ├── room/              (Member 2)
    │   │   ├── furniture/         (Member 3)
    │   │   ├── editor2d/          (Member 4)
    │   │   ├── renderer3d/        (Member 5)
    │   │   ├── storage/           (Member 6)
    │   │   ├── portfolio/         (Member 6)
    │   │   ├── common/            (Integrator - Shared)
    │   │   └── demo/              (All members)
    │   └── resources/
    │       ├── icons/             (UI icons)
    │       └─ data/               (Sample design files)
    └── test/java/                 (Unit tests)
```

## Module Descriptions

### app/ (Member 1)
- **App.java**: Main application entry point
- **MainFrame.java**: Primary application window
- **Navigator.java**: View/screen navigation
- **DesignState.java**: Shared state holder (used by all modules)

### auth/ (Member 1)
- **LoginPanel.java**: Login UI panel

### accounts/ (Member 1) - NEW
- **User.java**: User account data model
- **Role.java**: User roles and permissions
- **UserStore.java**: User data persistence
- **AccountController.java**: Account management logic

### room/ (Member 2)
- **Room.java**: Room data model
- **RoomShape.java**: Room shape definitions (rect, L-shape, etc.)
- **RoomFormPanel.java**: Room creation/editing UI
- **RoomController.java**: Room logic and validation

### furniture/ (Member 3)
- **FurnitureItem.java**: Individual furniture model
- **FurnitureType.java**: Furniture types/categories
- **FurnitureDefaults.java**: Default furniture dimensions/properties
- **FurnitureLibraryPanel.java**: Furniture selection UI
- **PropertiesPanel.java**: Furniture property editor UI
- **FurnitureController.java**: Furniture management logic

### editor2d/ (Member 4)
- **Editor2DPanel.java**: Main 2D editor UI container
- **Canvas2D.java**: Drawing surface for 2D design
- **SelectionManager.java**: Object selection in 2D
- **DragHandler.java**: Drag & drop for furniture placement
- **Grid.java**: Optional grid display for alignment

### renderer3d/ (Member 5)
- **View3DPanel.java**: 3D view UI container
- **Renderer3D.java**: 3D rendering engine
- **Camera.java**: 3D camera control
- **Projection.java**: Perspective/orthographic projection
- **MeshFactory.java**: Creates 3D mesh objects from furniture

### storage/ (Member 6)
- **DesignModel.java**: Design data structure
- **DesignStorage.java**: Save/load designs to disk
- **JsonUtil.java**: JSON serialization utilities

### portfolio/ (Member 6)
- **PortfolioPanel.java**: Gallery/portfolio view

### common/ (Integrator - Shared)
- **ui/UiTheme.java**: Color schemes, fonts, look & feel
- **ui/UiUtil.java**: Common UI utilities
- **ui/MessageDialogs.java**: Common dialog methods
- **util/ValidationUtil.java**: Input validation helpers
- **util/FileUtil.java**: File operations helpers

### demo/ (Each Member)
- Individual demo classes for independent testing:
  - `DemoLogin.java` - Login and account testing
  - `DemoRoom.java` - Room creation testing
  - `Demo2D.java` - 2D editor testing
  - `Demo3D.java` - 3D renderer testing
  - `DemoStorage.java` - Storage and persistence testing

## Development Guidelines

1. **Package Independence**: Keep modules loosely coupled
2. **Communication**: Use `DesignState` as the central state holder
3. **Testing**: Implement demo classes for isolated testing
4. **Code Style**: Follow Java conventions (CamelCase, proper naming)
5. **Documentation**: Add JavaDoc comments to public methods
6. **Git Workflow**: Commit frequently, avoid large commits

## Creating a .gitignore File

Create a `.gitignore` in the project root:

```
# IDE
.idea/
*.iml
out/

# Build
target/
*.class

# OS
.DS_Store
Thumbs.db
```

## Maven Build Commands

```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Package as JAR
mvn clean package

# Run the application (if configured)
mvn exec:java -Dexec.mainClass="com.teamname.furniviz.app.App"
```

## Important Notes

- **DO NOT EDIT** `pom.xml` unless adding dependencies (discuss with team first)
- **DO NOT DELETE** any team member's package without discussion
- **USE CONSISTENT NAMING**: Package names, class names, method names
- **COMMUNICATE**: When creating shared utilities or modifying `common/`
- **RESPECT OWNERSHIP**: Each member owns their assigned modules

---

**Last Updated**: February 21, 2026
