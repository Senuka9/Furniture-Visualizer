for /r src\main\java %%f in (*.java) do (
    "%JAVA_HOME%\bin\javac" -d target\classes -cp "%CLASSPATH%" "%%f" 2>nul
@echo off
REM Run Furniture Visualizer Application
REM This script compiles and runs the app with proper classpath

cd /d "%~dp0"

set CLASSPATH=lib\*
set JAVA_HOME=C:\Program Files\Java\jdk-17
set CLASSPATH=lib\*;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-sync\4.11.1\mongodb-driver-sync-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\bson\4.11.1\bson-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-core\4.11.1\mongodb-driver-core-4.11.1.jar;%USERPROFILE%\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar

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
for /r src\main\java %%f in (*.java) do (
    "%JAVA_HOME%\bin\javac" -d target\classes -cp "%CLASSPATH%" "%%f" 2>nul
echo.
echo Copying resources...
if not exist target\classes\images mkdir target\classes\images
xcopy /Y /Q src\main\resources\images target\classes\images

echo.
)

echo Running application...
"%JAVA_HOME%\bin\java" -cp "target\classes;%CLASSPATH%" com.teamname.furniviz.app.App

pause

