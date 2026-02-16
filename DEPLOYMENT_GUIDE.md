# Deployment Guide - Stock Trading Simulation

## Step-by-Step Deployment

### Step 1: Prerequisites
Ensure you have installed:
- Java JDK 17+
- Maven 3.6+
- (Optional) MySQL 8.0+

### Step 2: Extract the Project
```bash
unzip stock-trading-app.zip
cd stock-trading-app
```

### Step 3: Build
```bash
mvn clean install -DskipTests
```

### Step 4: Run
```bash
mvn spring-boot:run
```

### Step 5: Access
Open browser to: http://localhost:8080

Login with:
- Admin: admin1/admin123
- User: user1/user123

## Production Deployment

### Using JAR file:
```bash
java -jar target/stock-trading-app-1.0.0.jar
```

### With MySQL:
1. Create database: `CREATE DATABASE stocktrading;`
2. Update application.properties with MySQL settings
3. Restart application

## System Requirements
- RAM: Minimum 2GB
- Disk: 500MB
- Java: 17+
- Network: Port 8080 available
