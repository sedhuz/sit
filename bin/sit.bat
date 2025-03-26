@echo off
set SCRIPT_DIR=%~dp0
set JAR_PATH=%SCRIPT_DIR%..\build\libs\sit-vcs.jar
set isDev=false  REM Set to true for debugging

if not exist "%JAR_PATH%" (
    echo Error: Unable to find JAR file at %JAR_PATH%
    exit /b 1
)

if "%isDev%"=="true" (
    echo Running in debug mode...
    java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -jar "%JAR_PATH%" %* # This opens a debugger port that an IDE can connect
) else (
    java -jar "%JAR_PATH%" %*
)
