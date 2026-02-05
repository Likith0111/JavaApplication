package com.fullstack.eventbooking.controller;

import com.fullstack.eventbooking.dto.AuthResponse;
import com.fullstack.eventbooking.dto.LoginRequest;
import com.fullstack.eventbooking.dto.RegisterRequest;
import com.fullstack.eventbooking.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for handling authentication-related HTTP requests.
 * 
 * <p>This controller exposes public endpoints for user registration and login
 * in the event booking platform. It serves as the entry point for authentication
 * operations and delegates business logic to the {@link AuthService}.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/auth}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code POST /api/auth/register} - Register new user account</li>
 *   <li>{@code POST /api/auth/login} - Authenticate existing user</li>
 * </ul>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * <p><b>Validation:</b> Input validation is performed automatically using
 * Bean Validation annotations (@Valid). Invalid requests return HTTP 400
 * with detailed error messages.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see AuthService
 * @see AuthResponse
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    /** Service layer dependency for authentication operations */
    private final AuthService authService;

    /**
     * Registers a new user in the system.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/auth/register}</p>
     * <p><b>Access:</b> Public (no authentication required)</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "name": "John Doe",
     *   "email": "john@example.com",
     *   "password": "SecurePass123",
     *   "role": "CUSTOMER"
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "accessToken": "eyJhbGciOiJIUzI1NiIs...",
     *   "tokenType": "Bearer",
     *   "id": 1,
     *   "email": "john@example.com",
     *   "name": "John Doe",
     *   "role": "CUSTOMER"
     * }
     * </pre>
     * 
     * @param request validated registration request with user details
     * @return ResponseEntity containing AuthResponse with JWT token
     * @throws BadRequestException if email already exists (HTTP 400)
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Authenticates an existing user and provides access token.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/auth/login}</p>
     * <p><b>Access:</b> Public (no authentication required)</p>
     * 
     * <p><b>Request Body Example:</b></p>
     * <pre>
     * {
     *   "email": "john@example.com",
     *   "password": "SecurePass123"
     * }
     * </pre>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "accessToken": "eyJhbGciOiJIUzI1NiIs...",
     *   "tokenType": "Bearer",
     *   "id": 1,
     *   "email": "john@example.com",
     *   "name": "John Doe",
     *   "role": "CUSTOMER"
     * }
     * </pre>
     * 
     * <p><b>Usage:</b> Include the returned token in subsequent requests:</p>
     * <pre>
     * Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
     * </pre>
     * 
     * @param request validated login request with credentials
     * @return ResponseEntity containing AuthResponse with JWT token
     * @throws AuthenticationException if credentials are invalid (HTTP 401)
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
