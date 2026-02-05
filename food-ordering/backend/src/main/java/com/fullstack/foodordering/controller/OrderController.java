package com.fullstack.foodordering.controller;

import com.fullstack.foodordering.dto.OrderDto;
import com.fullstack.foodordering.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling order-related HTTP requests.
 * 
 * <p>This controller exposes endpoints for order management in the food ordering
 * platform. It allows customers to place orders from their cart, view their
 * order history, and retrieve specific order details.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/orders}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code POST /api/orders/place} - Place order from cart for a restaurant</li>
 *   <li>{@code GET /api/orders} - Get paginated list of user's orders</li>
 *   <li>{@code GET /api/orders/{id}} - Get order by ID</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Authenticated users can access their own orders.
 * Orders are automatically associated with the authenticated user.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see OrderService
 * @see OrderDto
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    /** Service layer dependency for order operations */
    private final OrderService orderService;

    /**
     * Places an order from the current user's cart for a specific restaurant.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/orders/place}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p>This endpoint processes all items in the user's cart for the specified
     * restaurant, creates an order, and clears those items from the cart.</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "restaurantId": 1
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "orderDate": "2024-01-15T10:30:00Z",
     *   "totalAmount": 25.98,
     *   "status": "PENDING",
     *   "restaurantId": 1,
     *   "items": [
     *     {
     *       "menuItemId": 1,
     *       "menuItemName": "Margherita Pizza",
     *       "quantity": 2,
     *       "price": 12.99
     *     }
     *   ]
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/orders/place \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"restaurantId":1}'
     * </pre>
     * 
     * @param body request body containing restaurantId
     * @return ResponseEntity containing created OrderDto with HTTP 201 status
     */
    @PostMapping("/place")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody Map<String, Long> body) {
        Long restaurantId = body.get("restaurantId");
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(restaurantId));
    }

    /**
     * Retrieves a paginated list of orders for the current authenticated user.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/orders}</p>
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
     *     "id": 1,
     *     "orderDate": "2024-01-15T10:30:00Z",
     *     "totalAmount": 25.98,
     *     "status": "PENDING",
     *     "restaurantId": 1
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET "http://localhost:8080/api/orders?page=0&size=10" \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing list of OrderDto objects
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> getMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderService.getMyOrders(page, size));
    }

    /**
     * Retrieves a specific order by its ID.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/orders/{id}}</p>
     * <p><b>Access:</b> Authenticated users (own orders only)</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Order ID</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "orderDate": "2024-01-15T10:30:00Z",
     *   "totalAmount": 25.98,
     *   "status": "PENDING",
     *   "restaurantId": 1,
     *   "items": [
     *     {
     *       "menuItemId": 1,
     *       "menuItemName": "Margherita Pizza",
     *       "quantity": 2,
     *       "price": 12.99
     *     }
     *   ]
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/orders/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id order ID
     * @return ResponseEntity containing OrderDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }
}
