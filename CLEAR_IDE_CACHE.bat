@echo off
REM IntelliJ IDEA Cache Invalidation Script
REM This script clears IDE caches so it can reload project configuration

echo.
echo ============================================================
echo CLEARING INTELLIJ IDEA CACHES
echo ============================================================
echo.

REM Define IDEA cache and settings directory
set IDEA_CACHE_DIR=%APPDATA%\JetBrains\IntelliJIdea*\system
set LOCAL_IDEA_CACHE=.\.idea

echo Clearing local .idea caches...

REM Clear workspace cache
if exist "%LOCAL_IDEA_CACHE%\workspace.xml" (
    del "%LOCAL_IDEA_CACHE%\workspace.xml"
    echo Deleted workspace.xml
)

REM Clear compiler cache
if exist "%LOCAL_IDEA_CACHE%\compiler.xml" (
    del "%LOCAL_IDEA_CACHE%\compiler.xml"
    echo Deleted compiler.xml
)

REM Clear misc cache
if exist "%LOCAL_IDEA_CACHE%\misc.xml" (
    del "%LOCAL_IDEA_CACHE%\misc.xml"
    echo Deleted misc.xml
)

echo.
echo ============================================================
echo IMPORTANT NEXT STEPS:
echo ============================================================
echo.
echo 1. CLOSE IntelliJ IDEA completely
echo 2. Wait 5 seconds
echo 3. Open IntelliJ IDEA again
echo 4. It will reload all project configuration
echo 5. Wait for Maven to download dependencies
echo.
echo Then try running the application again!
echo.
echo ============================================================
echo.

pause

