@echo off
REM Smart Home GUI Launcher Script for Windows
REM This script compiles and runs the JavaFX GUI

echo 🏠 Smart Home Automation Simulator
echo ==================================

REM Check if bin directory exists, create if not
if not exist "bin" mkdir bin

echo 📦 Compiling Java files...

REM Try to compile with JavaFX in classpath
javac -d bin src\main\java\com\smarthome\**\*.java 2>nul

if %ERRORLEVEL% EQU 0 (
    echo ✅ Compilation successful!
    echo 🚀 Launching GUI...
    java -cp bin com.smarthome.gui.SmartHomeGUI
) else (
    echo ⚠️  Standard compilation failed.
    echo Please use Maven: mvn clean javafx:run
    echo Or install JavaFX SDK and update this script with the path
    pause
)
