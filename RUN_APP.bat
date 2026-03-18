@echo off
REM Run Furniture Visualizer Application
REM This script compiles and runs the app with proper classpath

cd /d "%~dp0"

echo Compiling Java files...
set JAVA_HOME=C:\Program Files\Java\jdk-17
set CLASSPATH=lib\*

if not exist target\classes mkdir target\classes

echo.
echo Compiling all Java source files...
"%JAVA_HOME%\bin\javac" -d target\classes -cp "%CLASSPATH%" -encoding UTF-8 ^
    src\main\java\com\teamname\furniviz\accounts\*.java ^
    src\main\java\com\teamname\furniviz\common\util\*.java ^
    src\main\java\com\teamname\furniviz\auth\*.java ^
    src\main\java\com\teamname\furniviz\auth\exceptions\*.java ^
    src\main\java\com\teamname\furniviz\storage\*.java ^
    src\main\java\com\teamname\furniviz\furniture\*.java ^
    src\main\java\com\teamname\furniviz\room\*.java ^
    src\main\java\com\teamname\furniviz\editor2d\*.java ^
    src\main\java\com\teamname\furniviz\renderer3d\*.java ^
    src\main\java\com\teamname\furniviz\portfolio\*.java ^
    src\main\java\com\teamname\furniviz\demo\*.java ^
    src\main\java\com\teamname\furniviz\app\*.java ^
    src\main\java\org\example\*.java 2>&1

if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo Copying resources...
if not exist target\classes\images mkdir target\classes\images
xcopy /Y /Q src\main\resources\images target\classes\images

echo.
echo Running application...
"%JAVA_HOME%\bin\java" -cp "target\classes;%CLASSPATH%" com.teamname.furniviz.app.App

pause

