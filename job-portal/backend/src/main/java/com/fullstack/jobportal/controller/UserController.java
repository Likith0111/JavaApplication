package com.fullstack.jobportal.controller;

import com.fullstack.jobportal.dto.UserDto;
import com.fullstack.jobportal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller for handling user profile-related HTTP requests.
 * 
 * <p>This controller provides endpoints for authenticated users to access and
 * manage their profile information. It allows users to view their profile,
 * view other users' profiles, and upload resume files.</p>
 * 
 * <p><b>Base URL:</b> {@code /api/users}</p>
 * 
 * <p><b>Endpoints:</b></p>
 * <ul>
 *   <li>{@code GET /api/users/me} - Get current authenticated user profile</li>
 *   <li>{@code GET /api/users/{id}} - Get user profile by ID</li>
 *   <li>{@code POST /api/users/me/resume} - Upload resume file</li>
 * </ul>
 * 
 * <p><b>Access Requirements:</b> All endpoints require authentication.
 * Users can access their own profile and view other users' public profiles.</p>
 * 
 * <p><b>Response Format:</b> All endpoints return JSON responses with
 * consistent error handling through global exception handlers.</p>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @see UserService
 * @see UserDto
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    /** Service layer dependency for user operations */
    private final UserService userService;

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
     *   "role": "CANDIDATE",
     *   "resumeUrl": "https://example.com/resumes/resume.pdf"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/users/me \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @return ResponseEntity containing UserDto with user profile information
     */
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    /**
     * Retrieves a specific user's profile by their ID.
     * 
     * <p><b>HTTP Method:</b> GET</p>
     * <p><b>Endpoint:</b> {@code /api/users/{id}}</p>
     * <p><b>Access:</b> Authenticated users only</p>
     * 
     * <p><b>Path Parameters:</b></p>
     * <ul>
     *   <li>{@code id} - User ID</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "John Doe",
     *   "email": "john@example.com",
     *   "role": "CANDIDATE",
     *   "resumeUrl": "https://example.com/resumes/resume.pdf"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X GET http://localhost:8080/api/users/1 \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
     * </pre>
     * 
     * @param id user ID
     * @return ResponseEntity containing UserDto with user profile information
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Uploads a resume file for the current authenticated user.
     * 
     * <p><b>HTTP Method:</b> POST</p>
     * <p><b>Endpoint:</b> {@code /api/users/me/resume}</p>
     * <p><b>Access:</b> Authenticated candidates only</p>
     * 
     * <p><b>Request Parameters:</b></p>
     * <ul>
     *   <li>{@code file} - Resume file (multipart/form-data)</li>
     * </ul>
     * 
     * <p><b>Success Response (200 OK):</b></p>
     * <pre>
     * {
     *   "id": 1,
     *   "name": "John Doe",
     *   "email": "john@example.com",
     *   "role": "CANDIDATE",
     *   "resumeUrl": "https://example.com/resumes/resume.pdf"
     * }
     * </pre>
     * 
     * <p><b>Example cURL:</b></p>
     * <pre>
     * curl -X POST http://localhost:8080/api/users/me/resume \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..." \
     *   -F "file=@/path/to/resume.pdf"
     * </pre>
     * 
     * @param file resume file to upload
     * @return ResponseEntity containing updated UserDto with resume URL
     */
    @PostMapping("/me/resume")
    public ResponseEntity<UserDto> uploadResume(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.uploadResume(file));
    }
}
