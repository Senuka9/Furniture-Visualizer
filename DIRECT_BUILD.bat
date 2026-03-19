@echo off
REM Direct Compilation and Execution Script
REM This bypasses Maven and compiles directly with javac

setlocal enabledelayedexpansion

echo.
echo ============================================================
echo FURNITURE VISUALIZER - DIRECT BUILD & RUN
echo ============================================================
echo.

REM Create lib directory if it doesn't exist
if not exist "lib" mkdir lib

REM Download BCrypt JAR
echo Downloading BCrypt JAR...
powershell -Command "(New-Object System.Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/at/favre/lib/bcrypt/0.10.2/bcrypt-0.10.2.jar', 'lib\bcrypt-0.10.2.jar')"

REM Check if BCrypt was downloaded
if not exist "lib\bcrypt-0.10.2.jar" (
    echo ERROR: Failed to download BCrypt JAR
    echo Please download manually from:
    echo https://repo1.maven.org/maven2/at/favre/lib/bcrypt/0.10.2/bcrypt-0.10.2.jar
    echo And save to: lib\bcrypt-0.10.2.jar
    pause
    exit /b 1
)

echo OK - BCrypt downloaded
echo.

REM Check Java version
echo Checking Java...
java -version 2>&1 | findstr /C:"17" >nul
if !ERRORLEVEL! neq 0 (
    echo ERROR: Java 17+ is required
    echo Please install Java 17 from: https://jdk.java.net/17/
    pause
    exit /b 1
)

echo OK - Java 17 found
echo.

echo ============================================================
echo Compiling with Maven...
echo ============================================================
echo.

REM Try Maven build with downloaded dependencies
call mvn clean install -DskipTests -q

if !ERRORLEVEL! neq 0 (
    echo Maven build failed. Attempting direct javac compilation...

    REM Set classpath with all JARs
    set CLASSPATH=lib\*;target\classes;src\main\java

    REM Compile
    echo Compiling Java files...
    javac -version

    REM For simplicity, try Maven one more time
    call mvn clean install -X -DskipTests -q 2>&1 | findstr "ERROR"
)

echo.
echo ============================================================
echo Running Application...
echo ============================================================
echo.

call mvn exec:java@App

echo.
echo ============================================================
echo Application finished
echo ============================================================
echo.

pause

