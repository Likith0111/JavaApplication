package com.fullstack.foodordering.controller;

import com.fullstack.foodordering.dto.MenuItemDto;
import com.fullstack.foodordering.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling menu item-related HTTP requests.
 * 
 * <p>This controller exposes endpoints for menu item management in the food
 * ordering platform. It provides CRUD operations for managing restaurant
 * menu items that customers can order.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/menu}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/menu/restaurant/{restaurantId}} - Get menu items by restaurant</li>
 *   <li>{@code GET /api/menu/{id}} - Get menu item by ID</li>
 *   <li>{@code POST /api/menu} - Create new menu item</li>
 *   <li>{@code PUT /api/menu/{id}} - Update existing menu item</li>
 *   <li>{@code DELETE /api/menu/{id}} - Delete menu item</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Public read access for browsing menu items.
 * Create/Update/Delete operations require appropriate permissions.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see MenuService
 * @see MenuItemDto
 */
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class MenuController {

    /** Service layer dependency for menu operations */
    private final MenuService menuService;

    /**
     * Retrieves all menu items for a specific restaurant.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/menu/restaurant/{restaurantId}}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code restaurantId} - Restaurant ID</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Margherita Pizza",
     *     "description": "Classic margherita",
     *     "price": 12.99,
     *     "restaurantId": 1
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/menu/restaurant/1
     * </pre>
     * 
     * @param restaurantId restaurant ID to retrieve menu items for
     * @return ResponseEntity containing list of MenuItemDto objects
     */
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDto>> getByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuService.getByRestaurant(restaurantId));
    }

    /**
     * Retrieves a specific menu item by its ID.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/menu/{id}}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Menu item ID</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Margherita Pizza",
     *   "description": "Classic margherita",
     *   "price": 12.99,
     *   "restaurantId": 1
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/menu/1
     * </pre>
     * 
     * @param id menu item ID
     * @return ResponseEntity containing MenuItemDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getById(id));
    }

    /**
     * Creates a new menu item for a restaurant.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/menu}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "restaurantId": 1,
     *   "name": "Margherita Pizza",
     *   "description": "Classic margherita",
     *   "price": 12.99
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Margherita Pizza",
     *   "description": "Classic margherita",
     *   "price": 12.99,
     *   "restaurantId": 1
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/menu \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"restaurantId":1,"name":"Margherita Pizza","price":12.99}'
     * </pre>
     * 
     * @param body request body containing menu item details
     * @return ResponseEntity containing created MenuItemDto with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<MenuItemDto> create(@RequestBody Map<String, Object> body) {
        Long restaurantId = Long.valueOf(body.get("restaurantId").toString());
        String name = (String) body.get("name");
        String description = (String) body.getOrDefault("description", "");
        BigDecimal price = new BigDecimal(body.get("price").toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(menuService.create(restaurantId, name, description, price));
    }

    /**
     * Updates an existing menu item.
     * 
     * <p><b>HTTP Method:</b> PUT</p>
     * <p><b>Endpoint:</b> {@code /api/menu/{id}}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Menu item ID to update</li>
     * </ul>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Updated Margherita Pizza",
     *   "description": "Updated description",
     *   "price": 14.99
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Updated Margherita Pizza",
     *   "description": "Updated description",
     *   "price": 14.99,
     *   "restaurantId": 1
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X PUT http://localhost:8080/api/menu/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Updated Margherita Pizza","price":14.99}'
     * </pre>
     * 
     * @param id menu item ID to update
     * @param body request body containing updated menu item details
     * @return ResponseEntity containing updated MenuItemDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDto> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String description = (String) body.getOrDefault("description", "");
        BigDecimal price = new BigDecimal(body.get("price").toString());
        return ResponseEntity.ok(menuService.update(id, name, description, price));
    }

    /**
     * Deletes a menu item from the system.
     * 
     * <p><b>HTTP Method:</b> DELETE</p>
     * <p><b>Endpoint:</b> {@code /api/menu/{id}}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Menu item ID to delete</li>
     * </ul>
     * 
     * <p><b>Success Response (204 No Content):</b></p>
     * Empty response body
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X DELETE http://localhost:8080/api/menu/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id menu item ID to delete
     * @return ResponseEntity with no content (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
