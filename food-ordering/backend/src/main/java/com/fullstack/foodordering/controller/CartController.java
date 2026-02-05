package com.fullstack.foodordering.controller;

import com.fullstack.foodordering.dto.CartItemDto;
import com.fullstack.foodordering.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling shopping cart-related HTTP requests.
 * 
 * <p>This controller exposes endpoints for shopping cart management in the
 * food ordering platform. It allows authenticated users to add menu items,
 * view their cart, and remove items before placing an order.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/cart}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/cart} - Get current user's cart items</li>
 *   <li>{@code POST /api/cart/items} - Add menu item to cart</li>
 *   <li>{@code DELETE /api/cart/items/{id}} - Remove item from cart</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Authenticated users only. Each user has
 * their own isolated cart.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see CartService
 * @see CartItemDto
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    /** Service layer dependency for cart operations */
    private final CartService cartService;

    /**
     * Retrieves all items in the current user's shopping cart.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/cart}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "menuItemId": 5,
     *     "menuItemName": "Margherita Pizza",
     *     "quantity": 2,
     *     "price": 12.99,
     *     "subtotal": 25.98
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/cart \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @return ResponseEntity containing list of CartItemDto objects
     */
    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    /**
     * Adds a menu item to the current user's shopping cart.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/cart/items}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "menuItemId": 5,
     *   "quantity": 2
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "menuItemId": 5,
     *   "menuItemName": "Margherita Pizza",
     *   "quantity": 2,
     *   "price": 12.99,
     *   "subtotal": 25.98
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/cart/items \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"menuItemId":5,"quantity":2}'
     * </pre>
     * 
     * @param body request body containing menuItemId and optional quantity
     * @return ResponseEntity containing created CartItemDto with HTTP 201 status
     */
    @PostMapping("/items")
    public ResponseEntity<CartItemDto> addItem(@RequestBody Map<String, Object> body) {
        Long menuItemId = Long.valueOf(body.get("menuItemId").toString());
        int quantity = body.get("quantity") != null ? ((Number) body.get("quantity")).intValue() : 1;
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addToCart(menuItemId, quantity));
    }

    /**
     * Removes an item from the current user's shopping cart.
     * 
     * <p><b>HTTP Method:</b> DELETE</p>
     * <p><b>Endpoint:</b> {@code /api/cart/items/{id}}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Cart item ID to remove</li>
     * </ul>
     * 
     * <p><b>Success Response (204 No Content):</b></p>
     * Empty response body
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X DELETE http://localhost:8080/api/cart/items/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id cart item ID to remove
     * @return ResponseEntity with no content (204)
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.noContent().build();
    }
}
