@echo off
echo ========================================
echo Running Tests for All Projects
echo ========================================
echo.

set FAILED_TESTS=0

REM Test Job Portal Backend
echo [1/4] Testing Job Portal Backend...
cd job-portal\backend
call mvn test
if %errorlevel% neq 0 (
    echo [FAILED] Job Portal Backend tests failed
    set /a FAILED_TESTS+=1
) else (
    echo [PASSED] Job Portal Backend tests passed
)
cd ..\..
echo.

REM Test E-Commerce Backend
echo [2/4] Testing E-Commerce Backend...
cd e-commerce\backend
call mvn test
if %errorlevel% neq 0 (
    echo [FAILED] E-Commerce Backend tests failed
    set /a FAILED_TESTS+=1
) else (
    echo [PASSED] E-Commerce Backend tests passed
)
cd ..\..
echo.

REM Test Food Ordering Backend
echo [3/4] Testing Food Ordering Backend...
cd food-ordering\backend
call mvn test
if %errorlevel% neq 0 (
    echo [FAILED] Food Ordering Backend tests failed
    set /a FAILED_TESTS+=1
) else (
    echo [PASSED] Food Ordering Backend tests passed
)
cd ..\..
echo.

REM Test Event Booking Backend
echo [4/4] Testing Event Booking Backend...
cd event-booking\backend
call mvn test
if %errorlevel% neq 0 (
    echo [FAILED] Event Booking Backend tests failed
    set /a FAILED_TESTS+=1
) else (
    echo [PASSED] Event Booking Backend tests passed
)
cd ..\..
echo.

echo ========================================
echo Test Summary
echo ========================================
if %FAILED_TESTS% equ 0 (
    echo [SUCCESS] All tests passed!
) else (
    echo [FAILURE] %FAILED_TESTS% project(s) failed tests
)
echo.
pause
