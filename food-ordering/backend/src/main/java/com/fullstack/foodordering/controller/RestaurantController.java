package com.fullstack.foodordering.controller;

import com.fullstack.foodordering.dto.RestaurantDto;
import com.fullstack.foodordering.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling restaurant-related HTTP requests.
 * 
 * <p>This controller exposes endpoints for restaurant management in the food
 * ordering platform. It provides CRUD operations for managing restaurants that
 * customers can order from.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/restaurants}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/restaurants} - Get all restaurants</li>
 *   <li>{@code GET /api/restaurants/{id}} - Get restaurant by ID</li>
 *   <li>{@code POST /api/restaurants} - Create new restaurant</li>
 *   <li>{@code PUT /api/restaurants/{id}} - Update existing restaurant</li>
 *   <li>{@code DELETE /api/restaurants/{id}} - Delete restaurant</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Public read access for browsing restaurants.
 * Create/Update/Delete operations require appropriate permissions.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see RestaurantService
 * @see RestaurantDto
 */
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    /** Service layer dependency for restaurant operations */
    private final RestaurantService restaurantService;

    /**
     * Retrieves a list of all restaurants.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/restaurants}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Pizza Palace",
     *     "description": "Best pizza in town",
     *     "address": "123 Main St"
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/restaurants
     * </pre>
     * 
     * @return ResponseEntity containing list of RestaurantDto objects
     */
    @GetMapping
    public ResponseEntity<List<RestaurantDto>> findAll() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    /**
     * Retrieves a specific restaurant by its ID.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/restaurants/{id}}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Restaurant ID</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Pizza Palace",
     *   "description": "Best pizza in town",
     *   "address": "123 Main St"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/restaurants/1
     * </pre>
     * 
     * @param id restaurant ID
     * @return ResponseEntity containing RestaurantDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    /**
     * Creates a new restaurant in the system.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/restaurants}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Pizza Palace",
     *   "description": "Best pizza in town",
     *   "address": "123 Main St"
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Pizza Palace",
     *   "description": "Best pizza in town",
     *   "address": "123 Main St"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/restaurants \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Pizza Palace","description":"Best pizza","address":"123 Main St"}'
     * </pre>
     * 
     * @param body request body containing restaurant name, description, and address
     * @return ResponseEntity containing created RestaurantDto
     */
    @PostMapping
    public ResponseEntity<RestaurantDto> create(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(restaurantService.create(body.get("name"), body.getOrDefault("description", ""), body.getOrDefault("address", "")));
    }

    /**
     * Updates an existing restaurant.
     * 
     * <p><b>HTTP Method:</b> PUT</p>
     * <p><b>Endpoint:</b> {@code /api/restaurants/{id}}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Restaurant ID to update</li>
     * </ul>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Updated Pizza Palace",
     *   "description": "Updated description",
     *   "address": "456 New St"
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Updated Pizza Palace",
     *   "description": "Updated description",
     *   "address": "456 New St"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X PUT http://localhost:8080/api/restaurants/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Updated Pizza Palace","description":"Updated","address":"456 New St"}'
     * </pre>
     * 
     * @param id restaurant ID to update
     * @param body request body containing updated restaurant details
     * @return ResponseEntity containing updated RestaurantDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(restaurantService.update(id, body.get("name"), body.getOrDefault("description", ""), body.getOrDefault("address", "")));
    }

    /**
     * Deletes a restaurant from the system.
     * 
     * <p><b>HTTP Method:</b> DELETE</p>
     * <p><b>Endpoint:</b> {@code /api/restaurants/{id}}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Restaurant ID to delete</li>
     * </ul>
     * 
     * <p><b>Success Response (204 No Content):</b></p>
     * Empty response body
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X DELETE http://localhost:8080/api/restaurants/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id restaurant ID to delete
     * @return ResponseEntity with no content (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
