#!/bin/bash

# Smart Home GUI Launcher Script
# This script compiles and runs the JavaFX GUI

echo "🏠 Smart Home Automation Simulator"
echo "=================================="
echo ""
echo "Select UI version:"
echo "1) Floor Plan UI (New - Interactive floor plan)"
echo "2) List View UI (Original - Device list)"
echo ""
read -p "Enter choice [1-2]: " choice

case $choice in
    1)
        GUI_CLASS="com.smarthome.gui.FloorPlanGUI"
        echo "Selected: Floor Plan UI"
        ;;
    2)
        GUI_CLASS="com.smarthome.gui.SmartHomeGUI"
        echo "Selected: List View UI"
        ;;
    *)
        GUI_CLASS="com.smarthome.gui.FloorPlanGUI"
        echo "Invalid choice. Defaulting to Floor Plan UI"
        ;;
esac
echo ""

# Check if bin directory exists, create if not
if [ ! -d "bin" ]; then
    mkdir bin
fi

echo "📦 Compiling Java files..."

# Try to compile with JavaFX in classpath
javac -d bin src/main/java/com/smarthome/**/*.java 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo "🚀 Launching GUI..."
    java -cp bin $GUI_CLASS
else
    echo "⚠️  Standard compilation failed. Trying with JavaFX modules..."
    
    # Try with module path (adjust path as needed)
    JAVAFX_PATH="/usr/share/openjfx/lib"
    
    if [ -d "$JAVAFX_PATH" ]; then
        javac --module-path $JAVAFX_PATH --add-modules javafx.controls \
              -d bin src/main/java/com/smarthome/**/*.java
        
        if [ $? -eq 0 ]; then
            echo "✅ Compilation successful!"
            echo "🚀 Launching GUI..."
            java --module-path $JAVAFX_PATH --add-modules javafx.controls \
                 -cp bin $GUI_CLASS
        else
            echo "❌ Compilation failed!"
            echo "Please install JavaFX or use Maven: mvn clean javafx:run"
        fi
    else
        echo "❌ JavaFX not found at $JAVAFX_PATH"
        echo "Please either:"
        echo "  1. Install JavaFX and update JAVAFX_PATH in this script"
        echo "  2. Use Maven: mvn clean javafx:run"
    fi
fi
