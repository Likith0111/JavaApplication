package com.fullstack.jobportal.controller;

import com.fullstack.jobportal.dto.PageResponse;
import com.fullstack.jobportal.dto.UserDto;
import com.fullstack.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling admin-related HTTP requests.
 * 
 * <p>This controller exposes administrative endpoints for managing users and
 * viewing dashboard statistics in the job portal platform. It provides
 * functionality for user management and system overview.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/admin}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/admin/users} - Get paginated list of all users</li>
 *   <li>{@code DELETE /api/admin/users/{id}} - Delete user</li>
 *   <li>{@code GET /api/admin/dashboard} - Get dashboard statistics</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> Admin role required for all endpoints.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see UserService
 * @see UserDto
 * @see PageResponse
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    /** Service layer dependency for user operations */
    private final UserService userService;

    /**
     * Retrieves a paginated list of all users in the system.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/admin/users}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Query Parameters:</b></p>
     * <ul>
     *   <li>{@code page} - Page number (default: 0)</li>
     *   <li>{@code size} - Page size (default: 10)</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "content": [
     *     {
     *       "id": 1,
     *       "name": "John Doe",
     *       "email": "john@example.com",
     *       "role": "CANDIDATE"
     *     }
     *   ],
     *   "page": 0,
     *   "size": 10,
     *   "totalElements": 50,
     *   "totalPages": 5,
     *   "first": true,
     *   "last": false
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET "http://localhost:8080/api/admin/users?page=0&size=10" \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param page page number (0-indexed)
     * @param size number of items per page
     * @return ResponseEntity containing PageResponse with UserDto objects
     */
    @GetMapping("/users")
    public ResponseEntity<PageResponse<UserDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<UserDto> content = userService.getAllUsers(pageable);
        long total = userService.countAllUsers();
        int totalPages = (int) Math.ceil((double) total / size);
        PageResponse<UserDto> response = PageResponse.<UserDto>builder()
                .content(content)
                .page(page)
                .size(size)
                .totalElements(total)
                .totalPages(totalPages)
                .first(page == 0)
                .last(page >= totalPages - 1)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a user from the system.
     * 
     * <p><b>HTTP Method:</b> DELETE</p>
     * <p><b>Endpoint:</b> {@code /api/admin/users/{id}}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - User ID to delete</li>
     * </ul>
     * 
     * <p><b>Success Response (204 No Content):</b></p>
     * Empty response body
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X DELETE http://localhost:8080/api/admin/users/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id user ID to delete
     * @return ResponseEntity with no content (204)
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves dashboard statistics for the admin panel.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/admin/dashboard}</p>
     * <p><b>Access:</b> Admin only</p>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "totalUsers": 150
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/admin/dashboard \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @return ResponseEntity containing dashboard statistics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        long totalUsers = userService.countAllUsers();
        return ResponseEntity.ok(Map.of("totalUsers", totalUsers));
    }
}
