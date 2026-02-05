@echo off
echo ========================================
echo Installing Dependencies for All Projects
echo ========================================
echo.

echo Checking prerequisites...
echo.

REM Check Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 17+ and add it to PATH
    echo.
    pause
    exit /b 1
)
echo [OK] Java is installed

REM Check Maven
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Maven 3.8+ and add it to PATH
    echo.
    pause
    exit /b 1
)
echo [OK] Maven is installed

REM Check Node.js
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Node.js is not installed or not in PATH
    echo Please install Node.js 18+ and add it to PATH
    echo.
    pause
    exit /b 1
)
echo [OK] Node.js is installed

REM Check npm
npm -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] npm is not installed or not in PATH
    echo Please install npm and add it to PATH
    echo.
    pause
    exit /b 1
)
echo [OK] npm is installed

echo.
echo ========================================
echo All prerequisites are installed!
echo ========================================
echo.

REM Install Job Portal Backend
echo [1/8] Installing Job Portal Backend dependencies...
cd job-portal\backend
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Job Portal Backend installation failed
    cd ..\..
    pause
    exit /b 1
)
cd ..\..
echo [OK] Job Portal Backend installed

REM Install Job Portal Frontend
echo [2/8] Installing Job Portal Frontend dependencies...
cd job-portal\frontend
call npm install
if %errorlevel% neq 0 (
    echo [ERROR] Job Portal Frontend installation failed
    cd ..\..
    pause
    exit /b 1
)
cd ..\..
echo [OK] Job Portal Frontend installed

REM Install E-Commerce Backend
echo [3/8] Installing E-Commerce Backend dependencies...
cd e-commerce\backend
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] E-Commerce Backend installation failed
    cd ..\..
    pause
    exit /b 1
)
cd ..\..
echo [OK] E-Commerce Backend installed

REM Install E-Commerce Frontend
echo [4/8] Installing E-Commerce Frontend dependencies...
cd e-commerce\frontend
call npm install
if %errorlevel% neq 0 (
    echo [ERROR] E-Commerce Frontend installation failed
    cd ..\..
    pause
    exit /b 1
)
cd ..\..
echo [OK] E-Commerce Frontend installed

REM Install Food Ordering Backend
echo [5/8] Installing Food Ordering Backend dependencies...
cd food-ordering\backend
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Food Ordering Backend installation failed
    cd ..\..
    pause
    exit /b 1
)
cd ..\..
echo [OK] Food Ordering Backend installed

REM Install Food Ordering Frontend
echo [6/8] Installing Food Ordering Frontend dependencies...
cd food-ordering\frontend
call npm install
if %errorlevel% neq 0 (
    echo [ERROR] Food Ordering Frontend installation failed
    cd ..\..
    pause
    exit /b 1
)
cd ..\..
echo [OK] Food Ordering Frontend installed

REM Install Event Booking Backend
echo [7/8] Installing Event Booking Backend dependencies...
cd event-booking\backend
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Event Booking Backend installation failed
    cd ..\..
    pause
    exit /b 1
)
cd ..\..
echo [OK] Event Booking Backend installed

REM Install Event Booking Frontend
echo [8/8] Installing Event Booking Frontend dependencies...
cd event-booking\frontend
call npm install
if %errorlevel% neq 0 (
    echo [ERROR] Event Booking Frontend installation failed
    cd ..\..
    pause
    exit /b 1
)
cd ..\..
echo [OK] Event Booking Frontend installed

echo.
echo ========================================
echo All dependencies installed successfully!
echo ========================================
echo.
echo Next steps:
echo 1. Set up PostgreSQL databases (see SETUP.md)
echo 2. Configure database credentials in application-dev.yml files
echo 3. Run backends: cd ^<project^>\backend ^&^& mvn spring-boot:run -Dspring-boot.run.profiles=dev
echo 4. Run frontends: cd ^<project^>\frontend ^&^& npm start
echo.
pause
