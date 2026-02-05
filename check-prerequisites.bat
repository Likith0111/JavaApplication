@echo off
echo ========================================
echo Checking Prerequisites
echo ========================================
echo.

set MISSING_TOOLS=0

REM Check Java
echo Checking Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [X] Java is NOT installed or not in PATH
    set /a MISSING_TOOLS+=1
) else (
    echo [OK] Java is installed
    java -version 2>&1 | findstr /C:"version"
)
echo.

REM Check Maven
echo Checking Maven...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [X] Maven is NOT installed or not in PATH
    set /a MISSING_TOOLS+=1
) else (
    echo [OK] Maven is installed
    mvn -version 2>&1 | findstr /C:"Apache Maven"
)
echo.

REM Check Node.js
echo Checking Node.js...
node -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [X] Node.js is NOT installed or not in PATH
    set /a MISSING_TOOLS+=1
) else (
    echo [OK] Node.js is installed
    node -v
)
echo.

REM Check npm
echo Checking npm...
npm -v >nul 2>&1
if %errorlevel% neq 0 (
    echo [X] npm is NOT installed or not in PATH
    set /a MISSING_TOOLS+=1
) else (
    echo [OK] npm is installed
    npm -v
)
echo.

REM Check PostgreSQL
echo Checking PostgreSQL...
psql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [X] PostgreSQL is NOT installed or not in PATH
    echo    (Optional - can use Docker instead)
) else (
    echo [OK] PostgreSQL is installed
    psql --version
)
echo.

REM Check Docker
echo Checking Docker...
docker --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [X] Docker is NOT installed or not in PATH
    echo    (Optional - needed only for Docker deployment)
) else (
    echo [OK] Docker is installed
    docker --version
)
echo.

echo ========================================
echo Summary
echo ========================================
if %MISSING_TOOLS% equ 0 (
    echo [SUCCESS] All required tools are installed!
    echo You can now run: install-all.bat
) else (
    echo [FAILURE] %MISSING_TOOLS% required tool(s) are missing
    echo.
    echo NEXT STEPS:
    echo 1. Install missing tools (see SETUP.md for instructions)
    echo 2. Add them to your system PATH
    echo 3. Restart your terminal/IDE
    echo 4. Run this script again to verify
    echo.
    echo OR use Docker instead:
    echo 1. Install Docker Desktop
    echo 2. Run: docker-compose up --build
)
echo.
pause
