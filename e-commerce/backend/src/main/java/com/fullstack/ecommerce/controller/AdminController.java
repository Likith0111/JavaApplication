package com.fullstack.ecommerce.controller;

import com.fullstack.ecommerce.dto.CategoryDto;
import com.fullstack.ecommerce.dto.ProductDto;
import com.fullstack.ecommerce.service.CategoryService;
import com.fullstack.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling admin-related HTTP requests.
 * 
 * <p>This controller exposes administrative endpoints for managing categories
 * and products in the e-commerce platform. It provides CRUD operations for
 * catalog management and delegates business logic to respective services.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/admin}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/admin/categories} - List all categories</li>
 *   <li>{@code POST /api/admin/categories} - Create new category</li>
 *   <li>{@code GET /api/admin/products} - List all products (paginated)</li>
 *   <li>{@code POST /api/admin/products} - Create new product</li>
 *   <li>{@code PUT /api/admin/products/{id}} - Update existing product</li>
 *   <li>{@code DELETE /api/admin/products/{id}} - Delete product</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Admin role required for all endpoints.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see CategoryService
 * @see ProductService
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    /** Service layer dependency for category operations */
    private final CategoryService categoryService;
    /** Service layer dependency for product operations */
    private final ProductService productService;

    /**
     * Retrieves a list of all categories in the system.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/admin/categories}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Electronics",
     *     "description": "Electronic devices and accessories"
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/admin/categories \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @return ResponseEntity containing list of CategoryDto objects
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> listCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    /**
     * Creates a new product category.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/admin/categories}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Electronics",
     *   "description": "Electronic devices and accessories"
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Electronics",
     *   "description": "Electronic devices and accessories"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/admin/categories \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Electronics","description":"Electronic devices"}'
     * </pre>
     * 
     * @param body request body containing category name and optional description
     * @return ResponseEntity containing created CategoryDto
     */
    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(categoryService.create(body.get("name"), body.getOrDefault("description", "")));
    }

    /**
     * Retrieves a paginated list of all products.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/admin/products}</p>
     * <p><b>Access:</b> Admin only</p>
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
     * curl -X GET "http://localhost:8080/api/admin/products?page=0&size=20" \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing list of ProductDto objects
     */
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    /**
     * Creates a new product in the catalog.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/admin/products}</p>
     * <p><b>Access:</b> Admin only</p>
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
     * curl -X POST http://localhost:8080/api/admin/products \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Laptop","price":999.99,"stock":50,"categoryId":1}'
     * </pre>
     * 
     * @param body request body containing product details
     * @return ResponseEntity containing created ProductDto
     */
    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        String description = (String) body.getOrDefault("description", "");
        BigDecimal price = new BigDecimal(body.get("price").toString());
        Integer stock = body.get("stock") != null ? ((Number) body.get("stock")).intValue() : 0;
        Long categoryId = Long.valueOf(body.get("categoryId").toString());
        return ResponseEntity.ok(productService.create(name, description, price, stock, categoryId));
    }

    /**
     * Updates an existing product in the catalog.
     * 
     * <p><b>HTTP Method:</b> PUT</p>
     * <p><b>Endpoint:</b> {@code /api/admin/products/{id}}</p>
     * <p><b>Access:</b> Admin only</p>
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
     * curl -X PUT http://localhost:8080/api/admin/products/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Updated Laptop","price":899.99,"stock":30}'
     * </pre>
     * 
     * @param id product ID to update
     * @param body request body containing updated product details
     * @return ResponseEntity containing updated ProductDto
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody Map<String, Object> body) {
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
     * <p><b>Endpoint:</b> {@code /api/admin/products/{id}}</p>
     * <p><b>Access:</b> Admin only</p>
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
     * curl -X DELETE http://localhost:8080/api/admin/products/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id product ID to delete
     * @return ResponseEntity with no content (204)
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
