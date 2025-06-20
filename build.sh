#!/bin/bash

echo "Building MyHomeQuote application..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed. Please install Maven first."
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