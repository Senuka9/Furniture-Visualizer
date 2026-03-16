@echo off
REM Furniture Images Verification Script
REM This script helps you verify your image setup is correct

setlocal enabledelayedexpansion

echo.
echo ===============================================
echo Furniture Visualizer - Image Setup Verification
echo ===============================================
echo.

REM Set the images folder path
set IMAGES_FOLDER=C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images

echo Checking image folder: %IMAGES_FOLDER%
echo.

REM Check if folder exists
if not exist "%IMAGES_FOLDER%" (
    echo [ERROR] Image folder does NOT exist!
    echo Please create: %IMAGES_FOLDER%
    pause
    exit /b 1
)

echo [OK] Folder exists
echo.

REM Count files
echo ===============================================
echo Checking for required image files...
echo ===============================================
echo.

set "count=0"
for %%f in ("%IMAGES_FOLDER%\*.png") do (
    set /a count+=1
)

echo Total PNG files found: %count%
echo Expected: 18 files (9 furniture + 9 thumbnails)
echo.

if %count% lss 9 (
    echo [WARNING] Less than 9 files found!
    echo You need to download more furniture images.
) else if %count% equ 18 (
    echo [OK] Perfect! All 18 files present!
) else if %count% gtr 18 (
    echo [OK] Found %count% files (more than expected)
)

echo.
echo ===============================================
echo Checking for specific required files...
echo ===============================================
echo.

REM List of required files
set "required[0]=bed.png"
set "required[1]=bookshelf.png"
set "required[2]=coffee_table.png"
set "required[3]=desk.png"
set "required[4]=dining_table.png"
set "required[5]=lamp.png"
set "required[6]=office_chair.png"
set "required[7]=sofa.png"
set "required[8]=wooden_chair.png"

set "missing=0"

for /l %%i in (0,1,8) do (
    if exist "%IMAGES_FOLDER%\!required[%%i]!" (
        echo [OK] !required[%%i]!
    ) else (
        echo [MISSING] !required[%%i]!
        set /a missing+=1
    )
)

echo.
echo ===============================================
echo File Details
echo ===============================================
echo.

echo Folder contents (full listing):
echo.
dir "%IMAGES_FOLDER%\" /b
echo.

echo ===============================================
echo Summary
echo ===============================================
echo.

if %missing% equ 0 (
    echo [SUCCESS] All required furniture images present!
    echo Your furniture library is ready to use.
    echo.
    echo Next steps:
    echo 1. Open IntelliJ IDEA
    echo 2. Build menu ^> Rebuild Project
    echo 3. Run the application
    echo 4. Click "Furniture Library" button
    echo 5. Verify all images display correctly
) else (
    echo [INFO] %missing% image files need to be downloaded
    echo.
    echo Missing images:
    for /l %%i in (0,1,8) do (
        if not exist "%IMAGES_FOLDER%\!required[%%i]!" (
            echo   - !required[%%i]!
        )
    )
    echo.
    echo To download images:
    echo 1. Go to unsplash.com
    echo 2. Search for each furniture item
    echo 3. Download PNG files
    echo 4. Save to: %IMAGES_FOLDER%
)

echo.
echo ===============================================
echo Image Storage Location
echo ===============================================
echo.
echo All images should be placed in:
echo %IMAGES_FOLDER%
echo.

pause

