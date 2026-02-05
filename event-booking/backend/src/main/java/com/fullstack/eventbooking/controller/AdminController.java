package com.fullstack.eventbooking.controller;

import com.fullstack.eventbooking.dto.EventDto;
import com.fullstack.eventbooking.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling admin-related HTTP requests.
 * 
 * <p>This controller exposes administrative endpoints for managing events in the
 * event booking platform. It provides CRUD operations for event management,
 * allowing admins to create, update, and delete events.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/admin}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/admin/events} - List all events</li>
 *   <li>{@code POST /api/admin/events} - Create new event</li>
 *   <li>{@code PUT /api/admin/events/{id}} - Update existing event</li>
 *   <li>{@code DELETE /api/admin/events/{id}} - Delete event</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Admin role required for all endpoints.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see EventService
 * @see EventDto
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    /** Service layer dependency for event operations */
    private final EventService eventService;

    /**
     * Retrieves a list of all events in the system.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/admin/events}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Concert",
     *     "description": "Live music performance",
     *     "venue": "Concert Hall",
     *     "eventDate": "2024-02-15T19:00:00Z",
     *     "totalSeats": 500,
     *     "availableSeats": 450
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/admin/events \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @return ResponseEntity containing list of EventDto objects
     */
    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> listEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    /**
     * Creates a new event in the system.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/admin/events}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Concert",
     *   "description": "Live music performance",
     *   "venue": "Concert Hall",
     *   "eventDate": "2024-02-15T19:00:00Z",
     *   "totalSeats": 500
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Concert",
     *   "description": "Live music performance",
     *   "venue": "Concert Hall",
     *   "eventDate": "2024-02-15T19:00:00Z",
     *   "totalSeats": 500,
     *   "availableSeats": 500
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/admin/events \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Concert","venue":"Concert Hall","eventDate":"2024-02-15T19:00:00Z","totalSeats":500}'
     * </pre>
     * 
     * @param body request body containing event details
     * @return ResponseEntity containing created EventDto with HTTP 201 status
     */
    @PostMapping("/events")
    public ResponseEntity<EventDto> createEvent(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String description = (String) body.getOrDefault("description", "");
        String venue = (String) body.get("venue");
        Instant eventDate = Instant.parse((String) body.get("eventDate"));
        Integer totalSeats = ((Number) body.get("totalSeats")).intValue();
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(name, description, venue, eventDate, totalSeats));
    }

    /**
     * Updates an existing event in the system.
     * 
     * <p><b>HTTP Method:</b> PUT</p>
     * <p><b>Endpoint:</b> {@code /api/admin/events/{id}}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Event ID to update</li>
     * </ul>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Updated Concert",
     *   "description": "Updated description",
     *   "venue": "Updated Venue",
     *   "eventDate": "2024-02-20T19:00:00Z",
     *   "totalSeats": 600
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Updated Concert",
     *   "description": "Updated description",
     *   "venue": "Updated Venue",
     *   "eventDate": "2024-02-20T19:00:00Z",
     *   "totalSeats": 600,
     *   "availableSeats": 550
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X PUT http://localhost:8080/api/admin/events/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Updated Concert","venue":"Updated Venue","eventDate":"2024-02-20T19:00:00Z","totalSeats":600}'
     * </pre>
     * 
     * @param id event ID to update
     * @param body request body containing updated event details
     * @return ResponseEntity containing updated EventDto
     */
    @PutMapping("/events/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String description = (String) body.getOrDefault("description", "");
        String venue = (String) body.get("venue");
        Instant eventDate = body.get("eventDate") != null ? Instant.parse((String) body.get("eventDate")) : null;
        Integer totalSeats = body.get("totalSeats") != null ? ((Number) body.get("totalSeats")).intValue() : null;
        return ResponseEntity.ok(eventService.update(id, name, description, venue, eventDate, totalSeats));
    }

    /**
     * Deletes an event from the system.
     * 
     * <p><b>HTTP Method:</b> DELETE</p>
     * <p><b>Endpoint:</b> {@code /api/admin/events/{id}}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Event ID to delete</li>
     * </ul>
     * 
     * <p><b>Success Response (204 No Content):</b></p>
     * Empty response body
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X DELETE http://localhost:8080/api/admin/events/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id event ID to delete
     * @return ResponseEntity with no content (204)
     */
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
