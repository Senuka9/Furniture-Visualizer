param(
    [string]$Action = "build"
)

$projectRoot = Split-Path -Parent $MyInvocation.MyCommandPath
$srcMain = "$projectRoot\src\main\java"
$libDir = "$projectRoot\lib"
$targetClasses = "$projectRoot\target\classes"

function Write-Success {
    param([string]$message)
    Write-Host "✓ $message" -ForegroundColor Green
}

function Write-Error-Custom {
    param([string]$message)
    Write-Host "✗ $message" -ForegroundColor Red
}

switch($Action) {
    "clean" {
        Write-Host "Cleaning..."
        if (Test-Path $targetClasses) {
            Remove-Item -Recurse -Force $targetClasses
        }
        Write-Success "Clean complete"
    }

    "build" {
        Write-Host "Building Furniture Visualizer..."

        # Create target directory
        if (-not (Test-Path $targetClasses)) {
            New-Item -ItemType Directory -Force -Path $targetClasses | Out-Null
        }

        # Compile Java files - compile multiple packages together to resolve dependencies
        Write-Host "  Compiling Java source files..."
        cd $srcMain

        try {
            javac -cp "$libDir\*" `
                  -d $targetClasses `
                  -encoding UTF-8 `
                  com/teamname/furniviz/app/*.java `
                  com/teamname/furniviz/room/*.java `
                  com/teamname/furniviz/furniture/*.java `
                  com/teamname/furniviz/auth/*.java `
                  com/teamname/furniviz/accounts/*.java `
                  com/teamname/furniviz/common/util/*.java `
                  com/teamname/furniviz/storage/*.java `
                  com/teamname/furniviz/demo/*.java `
                  2>&1

            if ($LASTEXITCODE -eq 0) {
                Write-Success "Compilation successful"
            } else {
                Write-Error-Custom "Compilation failed (exit code: $LASTEXITCODE)"
                exit 1
            }
        } catch {
            Write-Error-Custom "Compilation error: $_"
            exit 1
        }

        # Copy resources
        Write-Host "  Copying resources..."
        try {
            Copy-Item "$projectRoot\src\main\resources\*" "$targetClasses\" -Recurse -Force -ErrorAction SilentlyContinue
            Write-Success "Resources copied"
        } catch {
            Write-Error-Custom "Failed to copy resources: $_"
        }

        Write-Success "Build complete"
    }

    "run" {
        Write-Host "Starting Furniture Visualizer..."
        cd $projectRoot

        if (-not (Test-Path "$targetClasses\com\teamname\furniviz\app\App.class")) {
            Write-Error-Custom "Application not built. Run: .\build.ps1 build"
            exit 1
        }

        java -cp "$targetClasses;$libDir\*" com.teamname.furniviz.app.App
    }

    "clean-build-run" {
        & $MyInvocation.MyCommand.Path -Action clean
        & $MyInvocation.MyCommand.Path -Action build
        & $MyInvocation.MyCommand.Path -Action run
    }

    default {
        Write-Host "Usage: .\build.ps1 [clean|build|run|clean-build-run]" -ForegroundColor Yellow
        exit 1
    }
}
