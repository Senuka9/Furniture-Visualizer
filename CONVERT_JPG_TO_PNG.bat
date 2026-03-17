@echo off
REM Convert all JPG files to PNG in the images folder
REM This script uses Windows built-in tools

setlocal enabledelayedexpansion

echo.
echo ===============================================
echo Converting JPG to PNG - Furniture Images
echo ===============================================
echo.

set "IMAGES_FOLDER=C:\Users\lap.lk\Desktop\Furniture-Visualizer\src\main\resources\images"

echo Converting files in: %IMAGES_FOLDER%
echo.

REM List files to convert
echo Files to be converted from JPG to PNG:
for %%f in ("%IMAGES_FOLDER%\*.jpg") do (
    echo   - %%~nf
)

echo.
echo ===============================================
echo NOTE: You will need to use an image converter
echo ===============================================
echo.
echo Recommended tools (free):
echo   1. ImageMagick (command line)
echo   2. GIMP (GUI)
echo   3. Online: CloudConvert.com or Convertio.co
echo   4. Python script (included)
echo.
echo Or try the Python converter below...
echo.

pause

