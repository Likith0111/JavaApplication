package com.fullstack.ecommerce.controller;

import com.fullstack.ecommerce.entity.User;
import com.fullstack.ecommerce.exception.ResourceNotFoundException;
import com.fullstack.ecommerce.repository.UserRepository;
import com.fullstack.ecommerce.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST Controller for handling user profile-related HTTP requests.
 * 
 * <p>This controller provides endpoints for authenticated users to access
 * their own profile information. It extracts user identity from the security
 * context and returns user details.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/users}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/users/me} - Get current authenticated user profile</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> All endpoints require authentication.
 * Users can only access their own profile information.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see UserRepository
 * @see UserPrincipal
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    /** Repository for user data access operations */
    private final UserRepository userRepository;

    /**
     * Retrieves the profile information of the currently authenticated user.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/users/me}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "John Doe",
     *   "email": "john@example.com",
     *   "role": "CUSTOMER"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/users/me \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @return Map containing user id, name, email, and role
     * @throws ResourceNotFoundException if user is not found (HTTP 404)
     */
    @GetMapping("/me")
    public Map<String, Object> getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(principal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", principal.getId()));
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().name()
        );
    }
}
