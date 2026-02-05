package com.fullstack.ecommerce.controller;

import com.fullstack.ecommerce.dto.ProductDto;
import com.fullstack.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling product-related HTTP requests.
 * 
 * <p>This controller exposes endpoints for product catalog operations in the
 * e-commerce platform. It provides functionality to browse, search, and manage
 * products with pagination support.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/products}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/products} - Get paginated list of all products</li>
 *   <li>{@code GET /api/products/{id}} - Get product by ID</li>
 *   <li>{@code GET /api/products/category/{categoryId}} - Get products by category</li>
 *   <li>{@code GET /api/products/search} - Search products by name</li>
 *   <li>{@code POST /api/products} - Create new product</li>
 *   <li>{@code PUT /api/products/{id}} - Update existing product</li>
 *   <li>{@code DELETE /api/products/{id}} - Delete product</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Public read access for browsing/searching.
 * Create/Update/Delete operations require appropriate permissions.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see ProductService
 * @see ProductDto
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    /** Service layer dependency for product operations */
    private final ProductService productService;

    /**
     * Retrieves a paginated list of all products.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/products}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Query Parameters:</b></p>
     * <ul>
     *   <li>{@code page} - Page number (default: 0)</li>
     *   <li>{@code size} - Page size (default: 20)</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Laptop",
     *     "description": "High-performance laptop",
     *     "price": 999.99,
     *     "stock": 50,
     *     "categoryId": 1
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET "http://localhost:8080/api/products?page=0&size=20"
     * </pre>
     * 
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing list of ProductDto objects
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    /**
     * Retrieves a specific product by its ID.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/products/{id}}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Product ID</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Laptop",
     *   "description": "High-performance laptop",
     *   "price": 999.99,
     *   "stock": 50,
     *   "categoryId": 1
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/products/1
     * </pre>
     * 
     * @param id product ID
     * @return ResponseEntity containing ProductDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    /**
     * Retrieves products filtered by category ID.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/products/category/{categoryId}}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code categoryId} - Category ID to filter by</li>
     * </ul>
     * 
     * <p><b>Query Parameters:</b></p>
     * <ul>
     *   <li>{@code page} - Page number (default: 0)</li>
     *   <li>{@code size} - Page size (default: 20)</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Laptop",
     *     "price": 999.99,
     *     "categoryId": 1
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET "http://localhost:8080/api/products/category/1?page=0&size=20"
     * </pre>
     * 
     * @param categoryId category ID to filter products
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing list of ProductDto objects
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> findByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.findByCategory(categoryId, pageable));
    }

    /**
     * Searches products by name using a query string.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/products/search}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Query Parameters:</b></p>
     * <ul>
     *   <li>{@code q} - Search query string (required)</li>
     *   <li>{@code page} - Page number (default: 0)</li>
     *   <li>{@code size} - Page size (default: 20)</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Laptop",
     *     "price": 999.99
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET "http://localhost:8080/api/products/search?q=laptop&page=0&size=20"
     * </pre>
     * 
     * @param q search query string
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing list of matching ProductDto objects
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.searchByName(q, pageable));
    }

    /**
     * Creates a new product in the catalog.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/products}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Laptop",
     *   "description": "High-performance laptop",
     *   "price": 999.99,
     *   "stock": 50,
     *   "categoryId": 1
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Laptop",
     *   "description": "High-performance laptop",
     *   "price": 999.99,
     *   "stock": 50,
     *   "categoryId": 1
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/products \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Laptop","price":999.99,"stock":50,"categoryId":1}'
     * </pre>
     * 
     * @param body request body containing product details
     * @return ResponseEntity containing created ProductDto with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String description = (String) body.getOrDefault("description", "");
        BigDecimal price = new BigDecimal(body.get("price").toString());
        Integer stock = body.get("stock") != null ? ((Number) body.get("stock")).intValue() : 0;
        Long categoryId = Long.valueOf(body.get("categoryId").toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(name, description, price, stock, categoryId));
    }

    /**
     * Updates an existing product in the catalog.
     * 
     * <p><b>HTTP Method:</b> PUT</p>
     * <p><b>Endpoint:</b> {@code /api/products/{id}}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Product ID to update</li>
     * </ul>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Updated Laptop",
     *   "description": "Updated description",
     *   "price": 899.99,
     *   "stock": 30
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Updated Laptop",
     *   "description": "Updated description",
     *   "price": 899.99,
     *   "stock": 30,
     *   "categoryId": 1
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X PUT http://localhost:8080/api/products/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Updated Laptop","price":899.99,"stock":30}'
     * </pre>
     * 
     * @param id product ID to update
     * @param body request body containing updated product details
     * @return ResponseEntity containing updated ProductDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String description = (String) body.getOrDefault("description", "");
        BigDecimal price = new BigDecimal(body.get("price").toString());
        Integer stock = body.get("stock") != null ? ((Number) body.get("stock")).intValue() : null;
        return ResponseEntity.ok(productService.update(id, name, description, price, stock));
    }

    /**
     * Deletes a product from the catalog.
     * 
     * <p><b>HTTP Method:</b> DELETE</p>
     * <p><b>Endpoint:</b> {@code /api/products/{id}}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Product ID to delete</li>
     * </ul>
     * 
     * <p><b>Success Response (204 No Content):</b></p>
     * Empty response body
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X DELETE http://localhost:8080/api/products/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id product ID to delete
     * @return ResponseEntity with no content (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
