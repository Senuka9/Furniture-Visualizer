@echo off
REM Furniture Visualizer - Direct Java Compilation and Execution
REM No Maven required - uses javac and java directly

setlocal enabledelayedexpansion

echo.
echo ============================================================
echo FURNITURE VISUALIZER - DIRECT COMPILATION
echo ============================================================
echo.

REM Create target directory for compiled classes
if not exist "target\classes" mkdir "target\classes"

REM Build classpath with all JARs
set CLASSPATH=lib\gson-2.10.1.jar;lib\mongodb-driver-core-4.11.1.jar;lib\mongodb-driver-sync-4.11.1.jar;lib\bcrypt-0.10.2.jar

echo Classpath: %CLASSPATH%
echo.

echo ============================================================
echo Compiling Java source files...
echo ============================================================
echo.

REM Compile all Java files
javac -d target\classes ^
  -cp %CLASSPATH% ^
  --release 17 ^
  -encoding UTF-8 ^
  -sourcepath src\main\java ^
  src\main\java\com\teamname\furniviz\accounts\*.java ^
  src\main\java\com\teamname\furniviz\auth\*.java ^
  src\main\java\com\teamname\furniviz\auth\exceptions\*.java ^
  src\main\java\com\teamname\furniviz\common\util\*.java ^
  src\main\java\com\teamname\furniviz\storage\*.java ^
  src\main\java\com\teamname\furniviz\app\*.java ^
  src\main\java\com\teamname\furniviz\room\*.java ^
  src\main\java\com\teamname\furniviz\furniture\*.java ^
  src\main\java\com\teamname\furniviz\editor2d\*.java ^
  src\main\java\com\teamname\furniviz\renderer3d\*.java ^
  src\main\java\com\teamname\furniviz\demo\*.java ^
  src\main\java\org\example\*.java 2>&1 | findstr /C:"error"

if !ERRORLEVEL! equ 0 (
    echo.
    echo ============================================================
    echo Compilation FAILED - There are errors above
    echo ============================================================
    pause
    exit /b 1
)

echo.
echo ============================================================
echo Compilation complete!
echo ============================================================
echo.

REM Copy resources
echo Copying resources...
xcopy src\main\resources\* target\classes\ /S /Y /Q 2>nul

echo.
echo ============================================================
echo Running Application...
echo ============================================================
echo.

REM Set full classpath including compiled classes
set CLASSPATH=target\classes;lib\*

REM Run the application
java -cp target\classes;lib\* org.example.Main

echo.
echo ============================================================
echo Application finished
echo ============================================================
echo.

pause

