#!/bin/bash

echo "Building MyHomeQuote application..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven first."
    exit 1
fi

# Clean and build the project
echo "Cleaning previous build..."
mvn clean

echo "Building project..."
mvn package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo "JAR file created: target/myhomequotebohdanov-1.0-SNAPSHOT.jar"
    echo "WAR file created: target/myhomequotebohdanov-1.0-SNAPSHOT.war"
else
    echo "❌ Build failed!"
    exit 1
fi 