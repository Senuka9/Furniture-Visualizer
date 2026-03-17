@echo off
REM Run Furniture Visualizer Application
REM This script compiles and runs the app with proper classpath

cd /d "%~dp0"

echo Compiling Java files...
set JAVA_HOME=C:\Program Files\Java\jdk-17
set CLASSPATH=lib\*;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-sync\4.11.1\mongodb-driver-sync-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\bson\4.11.1\bson-4.11.1.jar;%USERPROFILE%\.m2\repository\org\mongodb\mongodb-driver-core\4.11.1\mongodb-driver-core-4.11.1.jar;%USERPROFILE%\.m2\repository\com\google\code\gson\gson\2.10.1\gson-2.10.1.jar

if not exist target\classes mkdir target\classes

for /r src\main\java %%f in (*.java) do (
    "%JAVA_HOME%\bin\javac" -d target\classes -cp "%CLASSPATH%" "%%f" 2>nul
)

echo Running application...
"%JAVA_HOME%\bin\java" -cp "target\classes;%CLASSPATH%" com.teamname.furniviz.app.App

pause

