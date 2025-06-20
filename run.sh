#!/bin/bash

echo "Starting MyHomeQuote application..."

# Check if JAR file exists
if [ ! -f "target/myhomequotebohdanov-1.0-SNAPSHOT.jar" ]; then
    echo "Error: JAR file not found. Please run build.sh first."
    exit 1
fi

# Check if Java is installed and version is 11 or higher
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java 11 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "Error: Java 11 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "Using Java version: $(java -version 2>&1 | head -n 1)"

# Kill any existing process on port 8080
echo "Checking for existing processes on port 8080..."
pkill -f "myhomequotebohdanov" 2>/dev/null
sleep 2

# Start the application
echo "Starting application on port 8080..."
java -jar target/myhomequotebohdanov-1.0-SNAPSHOT.jar

echo "Application stopped." 