#!/bin/bash

echo "========================================"
echo "Installing Dependencies for All Projects"
echo "========================================"
echo ""

echo "Checking prerequisites..."
echo ""

# Check Java
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java is not installed or not in PATH"
    echo "Please install Java 17+ and add it to PATH"
    echo ""
    exit 1
fi
echo "[OK] Java is installed"

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven is not installed or not in PATH"
    echo "Please install Maven 3.8+ and add it to PATH"
    echo ""
    exit 1
fi
echo "[OK] Maven is installed"

# Check Node.js
if ! command -v node &> /dev/null; then
    echo "[ERROR] Node.js is not installed or not in PATH"
    echo "Please install Node.js 18+ and add it to PATH"
    echo ""
    exit 1
fi
echo "[OK] Node.js is installed"

# Check npm
if ! command -v npm &> /dev/null; then
    echo "[ERROR] npm is not installed or not in PATH"
    echo "Please install npm and add it to PATH"
    echo ""
    exit 1
fi
echo "[OK] npm is installed"

echo ""
echo "========================================"
echo "All prerequisites are installed!"
echo "========================================"
echo ""

# Function to handle errors
handle_error() {
    echo "[ERROR] $1"
    exit 1
}

# Install Job Portal Backend
echo "[1/8] Installing Job Portal Backend dependencies..."
cd job-portal/backend || handle_error "Failed to navigate to job-portal/backend"
mvn clean install -DskipTests || handle_error "Job Portal Backend installation failed"
cd ../..
echo "[OK] Job Portal Backend installed"

# Install Job Portal Frontend
echo "[2/8] Installing Job Portal Frontend dependencies..."
cd job-portal/frontend || handle_error "Failed to navigate to job-portal/frontend"
npm install || handle_error "Job Portal Frontend installation failed"
cd ../..
echo "[OK] Job Portal Frontend installed"

# Install E-Commerce Backend
echo "[3/8] Installing E-Commerce Backend dependencies..."
cd e-commerce/backend || handle_error "Failed to navigate to e-commerce/backend"
mvn clean install -DskipTests || handle_error "E-Commerce Backend installation failed"
cd ../..
echo "[OK] E-Commerce Backend installed"

# Install E-Commerce Frontend
echo "[4/8] Installing E-Commerce Frontend dependencies..."
cd e-commerce/frontend || handle_error "Failed to navigate to e-commerce/frontend"
npm install || handle_error "E-Commerce Frontend installation failed"
cd ../..
echo "[OK] E-Commerce Frontend installed"

# Install Food Ordering Backend
echo "[5/8] Installing Food Ordering Backend dependencies..."
cd food-ordering/backend || handle_error "Failed to navigate to food-ordering/backend"
mvn clean install -DskipTests || handle_error "Food Ordering Backend installation failed"
cd ../..
echo "[OK] Food Ordering Backend installed"

# Install Food Ordering Frontend
echo "[6/8] Installing Food Ordering Frontend dependencies..."
cd food-ordering/frontend || handle_error "Failed to navigate to food-ordering/frontend"
npm install || handle_error "Food Ordering Frontend installation failed"
cd ../..
echo "[OK] Food Ordering Frontend installed"

# Install Event Booking Backend
echo "[7/8] Installing Event Booking Backend dependencies..."
cd event-booking/backend || handle_error "Failed to navigate to event-booking/backend"
mvn clean install -DskipTests || handle_error "Event Booking Backend installation failed"
cd ../..
echo "[OK] Event Booking Backend installed"

# Install Event Booking Frontend
echo "[8/8] Installing Event Booking Frontend dependencies..."
cd event-booking/frontend || handle_error "Failed to navigate to event-booking/frontend"
npm install || handle_error "Event Booking Frontend installation failed"
cd ../..
echo "[OK] Event Booking Frontend installed"

echo ""
echo "========================================"
echo "All dependencies installed successfully!"
echo "========================================"
echo ""
echo "Next steps:"
echo "1. Set up PostgreSQL databases (see SETUP.md)"
echo "2. Configure database credentials in application-dev.yml files"
echo "3. Run backends: cd <project>/backend && mvn spring-boot:run -Dspring-boot.run.profiles=dev"
echo "4. Run frontends: cd <project>/frontend && npm start"
echo ""
