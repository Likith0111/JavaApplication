package com.fullstack.foodordering.controller;

import com.fullstack.foodordering.dto.MenuItemDto;
import com.fullstack.foodordering.dto.OrderDto;
import com.fullstack.foodordering.dto.RestaurantDto;
import com.fullstack.foodordering.entity.OrderStatus;
import com.fullstack.foodordering.service.MenuService;
import com.fullstack.foodordering.service.OrderService;
import com.fullstack.foodordering.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling admin-related HTTP requests.
 * 
 * <p>This controller exposes administrative endpoints for managing restaurants,
 * menu items, and orders in the food ordering platform. It provides CRUD
 * operations for restaurant and menu management, as well as order status updates.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/admin}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/admin/restaurants} - List all restaurants</li>
 *   <li>{@code POST /api/admin/restaurants} - Create new restaurant</li>
 *   <li>{@code GET /api/admin/menu/restaurant/{restaurantId}} - List menu items for restaurant</li>
 *   <li>{@code POST /api/admin/menu} - Create new menu item</li>
 *   <li>{@code PATCH /api/admin/orders/{id}/status} - Update order status</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Admin role required for all endpoints.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see RestaurantService
 * @see MenuService
 * @see OrderService
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    /** Service layer dependency for restaurant operations */
    private final RestaurantService restaurantService;
    /** Service layer dependency for menu operations */
    private final MenuService menuService;
    /** Service layer dependency for order operations */
    private final OrderService orderService;

    /**
     * Retrieves a list of all restaurants in the system.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/admin/restaurants}</p>
     * <p><b>Access:</b> Admin only</p>
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
     * curl -X GET http://localhost:8080/api/admin/restaurants \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @return ResponseEntity containing list of RestaurantDto objects
     */
    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantDto>> listRestaurants() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    /**
     * Creates a new restaurant in the system.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/admin/restaurants}</p>
     * <p><b>Access:</b> Admin only</p>
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
     * curl -X POST http://localhost:8080/api/admin/restaurants \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Pizza Palace","description":"Best pizza","address":"123 Main St"}'
     * </pre>
     * 
     * @param body request body containing restaurant name, description, and address
     * @return ResponseEntity containing created RestaurantDto
     */
    @PostMapping("/restaurants")
    public ResponseEntity<RestaurantDto> createRestaurant(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(restaurantService.create(body.get("name"), body.getOrDefault("description", ""), body.getOrDefault("address", "")));
    }

    /**
     * Retrieves all menu items for a specific restaurant.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/admin/menu/restaurant/{restaurantId}}</p>
     * <p><b>Access:</b> Admin only</p>
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
     * curl -X GET http://localhost:8080/api/admin/menu/restaurant/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param restaurantId restaurant ID to retrieve menu items for
     * @return ResponseEntity containing list of MenuItemDto objects
     */
    @GetMapping("/menu/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDto>> listMenu(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuService.getByRestaurant(restaurantId));
    }

    /**
     * Creates a new menu item for a restaurant.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/admin/menu}</p>
     * <p><b>Access:</b> Admin only</p>
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
     * curl -X POST http://localhost:8080/api/admin/menu \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"restaurantId":1,"name":"Margherita Pizza","price":12.99}'
     * </pre>
     * 
     * @param body request body containing menu item details
     * @return ResponseEntity containing created MenuItemDto
     */
    @PostMapping("/menu")
    public ResponseEntity<MenuItemDto> createMenuItem(@RequestBody Map<String, Object> body) {
        Long restaurantId = Long.valueOf(body.get("restaurantId").toString());
        String name = (String) body.get("name");
        String description = (String) body.getOrDefault("description", "");
        BigDecimal price = new BigDecimal(body.get("price").toString());
        return ResponseEntity.ok(menuService.create(restaurantId, name, description, price));
    }

    /**
     * Updates the status of an existing order.
     * 
     * <p><b>HTTP Method:</b> PATCH</p>
     * <p><b>Endpoint:</b> {@code /api/admin/orders/{id}/status}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Order ID</li>
     * </ul>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "status": "PREPARING"
     * }
     * </pre>
     * 
     * <p><b>Valid Status Values:</b> PENDING, CONFIRMED, PREPARING, READY, OUT_FOR_DELIVERY, DELIVERED, CANCELLED</p>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "orderDate": "2024-01-15T10:30:00Z",
     *   "totalAmount": 25.98,
     *   "status": "PREPARING"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X PATCH http://localhost:8080/api/admin/orders/1/status \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"status":"PREPARING"}'
     * </pre>
     * 
     * @param id order ID to update
     * @param body request body containing new status
     * @return ResponseEntity containing updated OrderDto
     */
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        OrderStatus status = OrderStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
