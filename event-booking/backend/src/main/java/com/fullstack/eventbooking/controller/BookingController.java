package com.fullstack.eventbooking.controller;

import com.fullstack.eventbooking.dto.BookingDto;
import com.fullstack.eventbooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling booking-related HTTP requests.
 * 
 * <p>This controller exposes endpoints for event booking management in the
 * event booking platform. It allows authenticated users to book seats for
 * events, view their booking history, and retrieve specific booking details.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/bookings}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code POST /api/bookings} - Create new booking for an event</li>
 *   <li>{@code GET /api/bookings} - Get paginated list of user's bookings</li>
 *   <li>{@code GET /api/bookings/{bookingId}} - Get booking by booking ID</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Authenticated users can access their own bookings.
 * Bookings are automatically associated with the authenticated user.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see BookingService
 * @see BookingDto
 */
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    /** Service layer dependency for booking operations */
    private final BookingService bookingService;

    /**
     * Creates a new booking for an event with specified number of seats.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/bookings}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p>This endpoint creates a booking for the specified event. The booking
     * is automatically associated with the authenticated user and generates
     * a unique booking ID.</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "eventId": 1,
     *   "seats": 2
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
     * <pre>
     * {
     *   "bookingId": "BK-20240115-001",
     *   "eventId": 1,
     *   "eventName": "Concert",
     *   "seats": 2,
     *   "bookingDate": "2024-01-15T10:30:00Z",
     *   "totalAmount": 100.00
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/bookings \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"eventId":1,"seats":2}'
     * </pre>
     * 
     * @param body request body containing eventId and number of seats
     * @return ResponseEntity containing created BookingDto with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<BookingDto> book(@RequestBody Map<String, Object> body) {
        Long eventId = Long.valueOf(body.get("eventId").toString());
        Integer seats = body.get("seats") != null ? ((Number) body.get("seats")).intValue() : 1;
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(eventId, seats));
    }

    /**
     * Retrieves a paginated list of bookings for the current authenticated user.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/bookings}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p><b>Query Parameters:</b></p>
     * <ul>
     *   <li>{@code page} - Page number (default: 0)</li>
     *   <li>{@code size} - Page size (default: 10)</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "bookingId": "BK-20240115-001",
     *     "eventId": 1,
     *     "eventName": "Concert",
     *     "seats": 2,
     *     "bookingDate": "2024-01-15T10:30:00Z",
     *     "totalAmount": 100.00
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET "http://localhost:8080/api/bookings?page=0&size=10" \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing list of BookingDto objects
     */
    @GetMapping
    public ResponseEntity<List<BookingDto>> getMyBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookingService.getMyBookings(page, size));
    }

    /**
     * Retrieves a specific booking by its unique booking ID.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/bookings/{bookingId}}</p>
     * <p><b>Access:</b> Authenticated users (own bookings only)</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code bookingId} - Unique booking identifier (e.g., "BK-20240115-001")</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "bookingId": "BK-20240115-001",
     *   "eventId": 1,
     *   "eventName": "Concert",
     *   "seats": 2,
     *   "bookingDate": "2024-01-15T10:30:00Z",
     *   "totalAmount": 100.00
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/bookings/BK-20240115-001 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param bookingId unique booking identifier
     * @return ResponseEntity containing BookingDto
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getByBookingId(@PathVariable String bookingId) {
        return ResponseEntity.ok(bookingService.getByBookingId(bookingId));
    }
}
