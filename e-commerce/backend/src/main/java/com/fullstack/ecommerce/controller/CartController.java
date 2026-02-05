package com.fullstack.ecommerce.controller;

import com.fullstack.ecommerce.dto.CartItemDto;
import com.fullstack.ecommerce.service.CartService;
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
 * e-commerce platform. It allows authenticated users to add items, update
 * quantities, and remove items from their cart.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/cart}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/cart} - Get current user's cart items</li>
 *   <li>{@code POST /api/cart/items} - Add item to cart</li>
 *   <li>{@code PATCH /api/cart/items/{id}} - Update item quantity</li>
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
     *     "productId": 5,
     *     "productName": "Laptop",
     *     "quantity": 2,
     *     "price": 999.99,
     *     "subtotal": 1999.98
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
     * Adds a product to the current user's shopping cart.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/cart/items}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "productId": 5,
     *   "quantity": 2
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "productId": 5,
     *   "productName": "Laptop",
     *   "quantity": 2,
     *   "price": 999.99,
     *   "subtotal": 1999.98
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/cart/items \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"productId":5,"quantity":2}'
     * </pre>
     * 
     * @param body request body containing productId and optional quantity
     * @return ResponseEntity containing created CartItemDto with HTTP 201 status
     */
    @PostMapping("/items")
    public ResponseEntity<CartItemDto> addItem(@RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = body.get("quantity") != null ? ((Number) body.get("quantity")).intValue() : 1;
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addToCart(productId, quantity));
    }

    /**
     * Updates the quantity of a specific cart item.
     * 
     * <p><b>HTTP Method:</b> PATCH</p>
     * <p><b>Endpoint:</b> {@code /api/cart/items/{id}}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Cart item ID</li>
     * </ul>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "quantity": 3
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * Empty response body
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X PATCH http://localhost:8080/api/cart/items/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"quantity":3}'
     * </pre>
     * 
     * @param id cart item ID to update
     * @param body request body containing new quantity
     * @return ResponseEntity with HTTP 200 status
     */
    @PatchMapping("/items/{id}")
    public ResponseEntity<Void> updateQuantity(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        int quantity = body.getOrDefault("quantity", 1);
        cartService.updateQuantity(id, quantity);
        return ResponseEntity.ok().build();
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
