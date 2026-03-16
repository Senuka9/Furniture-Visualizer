# QUICK START - Run Your Project

## Option 1: Use the Provided RUN_APP.bat
Simply double-click: `RUN_APP.bat`

This will:
1. Compile all Java files
2. Run the application
3. Show the window

## Option 2: Run from IntelliJ IDEA

1. Open IntelliJ IDEA
2. Open the Furniture-Visualizer project folder
3. Click "Run" → "Run App"
4. The application window will open

## Option 3: Run from Command Line

```powershell
cd C:\Users\lap.lk\Desktop\Furniture-Visualizer

# Compile (if needed)
$javaFiles = Get-ChildItem -Recurse -Filter "*.java" src/main/java | ForEach-Object { $_.FullName }
& "C:\Program Files\Java\jdk-17\bin\javac" -d target/classes -cp "lib/*;$HOME\.m2\repository\org\mongodb\mongodb-driver-sync\4.11.1\mongodb-driver-sync-4.11.1.jar;$HOME\.m2\repository\org\mongodb\bson\4.11.1\bson-4.11.1.jar;$HOME\.m2\repository\org\mongodb\mongodb-driver-core\4.11.1\mongodb-driver-core-4.11.1.jar;$HOME\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar" $javaFiles

# Run
& "C:\Program Files\Java\jdk-17\bin\java" -cp "target/classes;lib/*;$HOME\.m2\repository\org\mongodb\mongodb-driver-sync\4.11.1\mongodb-driver-sync-4.11.1.jar;$HOME\.m2\repository\org\mongodb\bson\4.11.1\bson-4.11.1.jar;$HOME\.m2\repository\org\mongodb\mongodb-driver-core\4.11.1\mongodb-driver-core-4.11.1.jar;$HOME\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar" com.teamname.furniviz.app.App
```

---

## How to Test Your Furniture Library Module

1. **Launch the App** (using any method above)
2. **Click "Furniture Library"** button on home page
3. **See the Furniture List** on the left with all items
4. **Filter by Category**:
   - Select "Chairs" → See only 2 chairs
   - Select "Tables" → See only 2 tables
   - Select "All" → See all 9 items
5. **Select an Item** and view its details on the right
6. **Click "Add to Room"** to add to your design
7. **Click "View 3D"** to see 3D preview (when implemented)

---

## Project Structure

```
Furniture-Visualizer/
├── src/main/
│   ├── java/com/teamname/furniviz/
│   │   ├── app/              (Navigator, DesignState, MainFrame)
│   │   ├── furniture/        ← YOUR MODULE
│   │   ├── room/             (Room design, templates)
│   │   ├── auth/             (Login/authentication)
│   │   ├── accounts/         (User management)
│   │   └── ...
│   └── resources/images/     (Furniture images)
├── target/classes/           (Compiled .class files)
├── lib/                       (External JARs)
├── pom.xml                    (Maven config)
├── build.ps1                  (Build script)
├── RUN_APP.bat               (Easy run script)
└── MEMBER3_BUILD_COMPLETE.md (Full documentation)
```

---

## Key Files You Own

**Furniture Module (Your Part):**
- `src/main/java/com/teamname/furniviz/furniture/FurnitureLibraryPanel.java`
- `src/main/java/com/teamname/furniviz/furniture/PropertiesPanel.java`
- `src/main/java/com/teamname/furniviz/furniture/FurnitureController.java`
- `src/main/java/com/teamname/furniviz/furniture/FurnitureDefaults.java`
- `src/main/java/com/teamname/furniviz/furniture/FurnitureItem.java`
- `src/main/java/com/teamname/furniviz/furniture/FurnitureType.java`
- `src/main/java/com/teamname/furniviz/furniture/FurnitureLibraryService.java`

**Images (Your Resources):**
- `src/main/resources/images/*.png` (18 image files)

---

## Status: ✓ READY TO GO

All compilation errors are fixed.
The application compiles and runs without errors.
Your Furniture Library module is fully functional.

Go ahead and run it! 🚀

