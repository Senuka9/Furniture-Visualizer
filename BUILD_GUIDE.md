# Build & Run Guide - Furniture Visualizer

## Project Setup

### Prerequisites
- Java 17+ (current: Java 21.0.9 LTS)
- Maven (optional - project compiles with javac)

### Quick Start

#### 1. Build the Project
```powershell
cd C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\java

# Compile all Java files
$files = Get-ChildItem -Recurse -Filter "*.java" -File | ForEach-Object { $_.FullName }
javac -cp "C:\Users\lap.lk\Desktop\Furniture-Visualizer\lib\*" `
       -d "C:\Users\lap.lk\Desktop\Furniture-Visualizer\target\classes" $files
```

#### 2. Copy Resources
```powershell
Copy-Item "C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\*" `
          "C:\Users\lap.lk\Desktop\Furniture-Visualizer\target\classes\" -Recurse -Force
```

#### 3. Run the Application
```powershell
cd C:\Users\lap.lk\Desktop\Furniture-Visualizer
java -cp "target\classes;lib\*" com.teamname.furniviz.app.App
```

## Build Scripts

### Using build.ps1 (PowerShell)
To enable building via script, populate `build.ps1` with:

```powershell
param(
    [string]$Action = "build"
)

$projectRoot = Split-Path -Parent $MyInvocation.MyCommandPath
$srcMain = "$projectRoot\src\main\java"
$libDir = "$projectRoot\lib"
$targetClasses = "$projectRoot\target\classes"

switch($Action) {
    "clean" {
        Remove-Item -Recurse -Force $targetClasses -ErrorAction SilentlyContinue
        Write-Host "✓ Clean complete"
    }
    
    "build" {
        # Create target directory
        New-Item -ItemType Directory -Force -Path $targetClasses | Out-Null
        
        # Compile
        Write-Host "Compiling..."
        cd $srcMain
        $files = Get-ChildItem -Recurse -Filter "*.java" -File | ForEach-Object { $_.FullName }
        javac -cp "$libDir\*" -d $targetClasses $files
        
        if ($?) { Write-Host "✓ Compilation successful" }
        else { Write-Host "✗ Compilation failed"; exit 1 }
        
        # Copy resources
        Write-Host "Copying resources..."
        Copy-Item "$projectRoot\src\main\resources\*" "$targetClasses\" -Recurse -Force
        Write-Host "✓ Build complete"
    }
    
    "run" {
        Write-Host "Starting Furniture Visualizer..."
        cd $projectRoot
        java -cp "target\classes;lib\*" com.teamname.furniviz.app.App
    }
    
    "clean-build-run" {
        & $MyInvocation.MyCommand.Path -Action clean
        & $MyInvocation.MyCommand.Path -Action build
        & $MyInvocation.MyCommand.Path -Action run
    }
    
    default {
        Write-Host "Usage: .\build.ps1 [clean|build|run|clean-build-run]"
    }
}
```

Usage:
```powershell
.\build.ps1 build          # Compile only
.\build.ps1 run            # Run compiled app
.\build.ps1 clean-build-run # Clean, compile, and run
```

### Using build.bat (Command Prompt)
```batch
@echo off
setlocal enabledelayedexpansion

set JAVA_HOME=C:\Program Files\Java\jdk-21
set CLASSPATH=lib\*
set SRC_DIR=src\main\java
set TARGET_DIR=target\classes

if "%1"=="clean" (
    echo Cleaning...
    rmdir /s /q %TARGET_DIR%
    echo Done.
) else if "%1"=="build" (
    echo Building...
    mkdir %TARGET_DIR%
    cd %SRC_DIR%
    for /r %%f in (*.java) do (
        javac -cp "%CLASSPATH%" -d "%TARGET_DIR%" "%%f"
    )
    cd ..\..\..\
    xcopy src\main\resources %TARGET_DIR% /s /y
    echo Build complete.
) else if "%1"=="run" (
    echo Running Furniture Visualizer...
    java -cp "%TARGET_DIR%;%CLASSPATH%" com.teamname.furniviz.app.App
) else (
    echo Usage: build.bat [clean^|build^|run]
)
```

## Project Structure

```
Furniture-Visualizer/
├── src/
│   └── main/
│       ├── java/
│       │   ├── com/teamname/furniviz/
│       │   │   ├── app/              (Member 1: Navigation, MainFrame)
│       │   │   ├── auth/             (Member 1: Login)
│       │   │   ├── accounts/         (Member 1: User management)
│       │   │   ├── room/             (Member 2: Room creation & editing)
│       │   │   ├── furniture/        (Member 3: Furniture Library) ⭐
│       │   │   ├── editor2d/         (Member 4: 2D room editor)
│       │   │   ├── renderer3d/       (Member 5: 3D rendering)
│       │   │   ├── storage/          (Member 6: Persistence)
│       │   │   ├── portfolio/        (Member 6: Portfolio gallery)
│       │   │   ├── common/           (Shared utilities)
│       │   │   └── demo/             (Testing & demos)
│       │   └── org/example/Main.java
│       └── resources/
│           └── images/               (Furniture item images)
├── target/
│   └── classes/                      (Compiled .class files)
├── lib/                              (External dependencies)
│   ├── gson-2.10.1.jar
│   ├── mongodb-driver-core-4.11.1.jar
│   └── mongodb-driver-sync-4.11.1.jar
├── docs/
│   ├── ownership.md                  (Team responsibility matrix)
│   ├── FURNITURE_MODULE.md           (Furniture module documentation)
│   └── BUILD_GUIDE.md                (This file)
├── pom.xml                           (Maven configuration)
├── build.ps1                         (PowerShell build script)
└── build.bat                         (Batch build script)
```

## Troubleshooting

### Issue: "cannot find symbol" errors during compilation
**Solution**: Ensure all files in the same package are compiled together:
```powershell
$files = Get-ChildItem -Recurse -Filter "*.java" -File | ForEach-Object { $_.FullName }
javac -cp "lib\*" -d "target\classes" $files
```

### Issue: Application shows "No Image Available" for furniture
**Solution**: Ensure resources are copied to target/classes:
```powershell
Copy-Item "src\main\resources\*" "target\classes\" -Recurse -Force
```

### Issue: ClassNotFoundException when running
**Solution**: Check classpath includes both target/classes and lib/* :
```powershell
java -cp "target\classes;lib\*" com.teamname.furniviz.app.App
```

### Issue: MongoDB connection errors
**Solution**: MongoDB is optional. Application uses in-memory RoomRepository by default. To use MongoDB:
1. Install MongoDB locally or use cloud instance
2. Update connection string in storage module

## Running Individual Modules

Each member can test their module independently:

```powershell
# Test Room Module (Member 2)
java -cp "target\classes;lib\*" com.teamname.furniviz.demo.DemoRoom

# Test Furniture Module (Member 3)
java -cp "target\classes;lib\*" com.teamname.furniviz.demo.DemoFurnitureTools

# Test 2D Editor (Member 4)
java -cp "target\classes;lib\*" com.teamname.furniviz.demo.Demo2D

# Test 3D Renderer (Member 5)
java -cp "target\classes;lib\*" com.teamname.furniviz.demo.Demo3D

# Test Storage (Member 6)
java -cp "target\classes;lib\*" com.teamname.furniviz.demo.DemoStorage
```

## Creating Custom Build Profiles

### Development Build (No Optimization)
```powershell
javac -g -cp "lib\*" -d "target\classes" -encoding UTF-8 $files
```

### Production Build (Optimized)
```powershell
javac -O -cp "lib\*" -d "target\classes" -encoding UTF-8 $files
```

### Debug Build (With Debug Info)
```powershell
javac -g:lines,vars,source -cp "lib\*" -d "target\classes" -encoding UTF-8 $files
```

## CI/CD Considerations

For automated builds (GitHub Actions, Jenkins, etc.):

```yaml
# Example GitHub Actions workflow
name: Build and Test
on: [push, pull_request]
jobs:
  build:
    runs-on: windows-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '21'
      - run: .\build.ps1 build
      - run: .\build.ps1 run
```

---

**Maintained by**: Member 1 (Application Core)  
**Last Updated**: March 15, 2026

