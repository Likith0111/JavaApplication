#!/bin/bash

echo "========================================"
echo "Running Tests for All Projects"
echo "========================================"
echo ""

FAILED_TESTS=0

# Test Job Portal Backend
echo "[1/4] Testing Job Portal Backend..."
cd job-portal/backend || exit 1
if mvn test; then
    echo "[PASSED] Job Portal Backend tests passed"
else
    echo "[FAILED] Job Portal Backend tests failed"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi
cd ../..
echo ""

# Test E-Commerce Backend
echo "[2/4] Testing E-Commerce Backend..."
cd e-commerce/backend || exit 1
if mvn test; then
    echo "[PASSED] E-Commerce Backend tests passed"
else
    echo "[FAILED] E-Commerce Backend tests failed"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi
cd ../..
echo ""

# Test Food Ordering Backend
echo "[3/4] Testing Food Ordering Backend..."
cd food-ordering/backend || exit 1
if mvn test; then
    echo "[PASSED] Food Ordering Backend tests passed"
else
    echo "[FAILED] Food Ordering Backend tests failed"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi
cd ../..
echo ""

# Test Event Booking Backend
echo "[4/4] Testing Event Booking Backend..."
cd event-booking/backend || exit 1
if mvn test; then
    echo "[PASSED] Event Booking Backend tests passed"
else
    echo "[FAILED] Event Booking Backend tests failed"
    FAILED_TESTS=$((FAILED_TESTS + 1))
fi
cd ../..
echo ""

echo "========================================"
echo "Test Summary"
echo "========================================"
if [ $FAILED_TESTS -eq 0 ]; then
    echo "[SUCCESS] All tests passed!"
else
    echo "[FAILURE] $FAILED_TESTS project(s) failed tests"
fi
echo ""
