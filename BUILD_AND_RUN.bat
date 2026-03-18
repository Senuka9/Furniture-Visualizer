@echo off
REM Build and run Furniture Visualizer with proper resource handling

setlocal enabledelayedexpansion

cd /d "%~dp0"

echo ================================================
echo Building Furniture Visualizer
echo ================================================
echo.

REM Set up paths
set JAVA_HOME=C:\Program Files\Java\jdk-17
set SRC_DIR=%cd%\src\main\java
set RESOURCES_DIR=%cd%\src\main\resources
set TARGET_CLASSES=%cd%\target\classes
set LIB_DIR=%cd%\lib

REM Create target directory
if not exist "%TARGET_CLASSES%" mkdir "%TARGET_CLASSES%"

echo Step 1: Compiling Java files...
cd "%SRC_DIR%"

"%JAVA_HOME%\bin\javac" ^
  -d "%TARGET_CLASSES%" ^
  -cp "%LIB_DIR%\*;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-sync\4.11.1\mongodb-driver-sync-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\bson\4.11.1\bson-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-core\4.11.1\mongodb-driver-core-4.11.1.jar;%USERPROFILE%\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar" ^
  com\teamname\furniviz\app\*.java ^
  com\teamname\furniviz\room\*.java ^
  com\teamname\furniviz\furniture\*.java ^
  com\teamname\furniviz\auth\*.java ^
  com\teamname\furniviz\accounts\*.java ^
  com\teamname\furniviz\common\util\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Step 2: Copying resources...

REM Copy images to target
if not exist "%TARGET_CLASSES%\images" mkdir "%TARGET_CLASSES%\images"
xcopy "%RESOURCES_DIR%\images\*.png" "%TARGET_CLASSES%\images\" /Y /Q

echo.
echo Step 3: Running application...
echo.

cd /d "%~dp0"

"%JAVA_HOME%\bin\java" ^
  -cp "%TARGET_CLASSES%;%LIB_DIR%\*;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-sync\4.11.1\mongodb-driver-sync-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\bson\4.11.1\bson-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-core\4.11.1\mongodb-driver-core-4.11.1.jar;%USERPROFILE%\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar" ^
  com.teamname.furniviz.app.App

pause

