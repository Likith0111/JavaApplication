package com.fullstack.ecommerce.controller;

import com.fullstack.ecommerce.dto.CategoryDto;
import com.fullstack.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling category-related HTTP requests.
 * 
 * <p>This controller exposes endpoints for product category management in the
 * e-commerce platform. It provides CRUD operations for organizing products
 * into categories.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/categories}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/categories} - Get all categories</li>
 *   <li>{@code GET /api/categories/{id}} - Get category by ID</li>
 *   <li>{@code POST /api/categories} - Create new category</li>
 *   <li>{@code PUT /api/categories/{id}} - Update existing category</li>
 *   <li>{@code DELETE /api/categories/{id}} - Delete category</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Public read access for browsing categories.
 * Create/Update/Delete operations require appropriate permissions.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see CategoryService
 * @see CategoryDto
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    /** Service layer dependency for category operations */
    private final CategoryService categoryService;

    /**
     * Retrieves a list of all product categories.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/categories}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * [
     *   {
     *     "id": 1,
     *     "name": "Electronics",
     *     "description": "Electronic devices and accessories"
     *   },
     *   {
     *     "id": 2,
     *     "name": "Clothing",
     *     "description": "Apparel and fashion items"
     *   }
     * ]
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/categories
     * </pre>
     * 
     * @return ResponseEntity containing list of CategoryDto objects
     */
    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    /**
     * Retrieves a specific category by its ID.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/categories/{id}}</p>
     * <p><b>Access:</b> Public</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Category ID</li>
     * </ul>
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
     * curl -X GET http://localhost:8080/api/categories/1
     * </pre>
     * 
     * @param id category ID
     * @return ResponseEntity containing CategoryDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    /**
     * Creates a new product category.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/categories}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Electronics",
     *   "description": "Electronic devices and accessories"
     * }
     * </pre>
     * 
     * <p><b>Success Response (201 Created):</b></p>
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
     * curl -X POST http://localhost:8080/api/categories \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Electronics","description":"Electronic devices"}'
     * </pre>
     * 
     * @param body request body containing category name and optional description
     * @return ResponseEntity containing created CategoryDto with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String description = body.getOrDefault("description", "");
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(name, description));
    }

    /**
     * Updates an existing product category.
     * 
     * <p><b>HTTP Method:</b> PUT</p>
     * <p><b>Endpoint:</b> {@code /api/categories/{id}}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Category ID to update</li>
     * </ul>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "Updated Electronics",
     *   "description": "Updated description"
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Updated Electronics",
     *   "description": "Updated description"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X PUT http://localhost:8080/api/categories/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"Updated Electronics","description":"Updated description"}'
     * </pre>
     * 
     * @param id category ID to update
     * @param body request body containing updated category details
     * @return ResponseEntity containing updated CategoryDto
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(categoryService.update(id, body.get("name"), body.getOrDefault("description", "")));
    }

    /**
     * Deletes a product category.
     * 
     * <p><b>HTTP Method:</b> DELETE</p>
     * <p><b>Endpoint:</b> {@code /api/categories/{id}}</p>
     * <p><b>Access:</b> Requires appropriate permissions</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - Category ID to delete</li>
     * </ul>
     * 
     * <p><b>Success Response (204 No Content):</b></p>
     * Empty response body
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X DELETE http://localhost:8080/api/categories/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id category ID to delete
     * @return ResponseEntity with no content (204)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
