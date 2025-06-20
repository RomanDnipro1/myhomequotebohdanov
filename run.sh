#!/bin/bash

echo "Starting MyHomeQuote application..."

# Check if JAR file exists
if [ ! -f "target/myhomequotebohdanov-1.0-SNAPSHOT.jar" ]; then
    echo "Error: JAR file not found. Please run build.sh first."
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed. Please install Java first."
    exit 1
fi

# Kill any existing process on port 8080
echo "Checking for existing processes on port 8080..."
pkill -f "myhomequotebohdanov" 2>/dev/null
sleep 2

# Start the application
echo "Starting application on port 8080..."
java -jar target/myhomequotebohdanov-1.0-SNAPSHOT.jar

echo "Application stopped." 