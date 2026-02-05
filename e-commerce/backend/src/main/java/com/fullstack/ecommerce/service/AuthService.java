package com.fullstack.ecommerce.service;

import com.fullstack.ecommerce.dto.AuthResponse;
import com.fullstack.ecommerce.dto.LoginRequest;
import com.fullstack.ecommerce.dto.RegisterRequest;
import com.fullstack.ecommerce.entity.User;
import com.fullstack.ecommerce.exception.BadRequestException;
import com.fullstack.ecommerce.repository.UserRepository;
import com.fullstack.ecommerce.security.JwtTokenProvider;
import com.fullstack.ecommerce.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class handling authentication operations for the e-commerce platform.
 * 
 * <p>This service provides comprehensive user authentication functionality including
 * user registration and login. It implements secure practices such as password
 * encryption, JWT token generation, and transaction management.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>User registration with email uniqueness validation</li>
 *   <li>Secure password encryption using BCrypt</li>
 *   <li>JWT-based stateless authentication</li>
 *   <li>Automatic authentication after registration</li>
 *   <li>Transaction management for data consistency</li>
 * </ul>
 * 
 * <p><b>Security Considerations:</b></p>
 * <ul>
 *   <li>Passwords are never stored in plain text</li>
 *   <li>BCrypt hashing with salt for password encryption</li>
 *   <li>JWT tokens expire after configured duration</li>
 *   <li>Email-based unique user identification</li>
 * </ul>
 * 
 * @author Full-Stack Java Portfolio
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    /** Repository for user data persistence operations */
    private final UserRepository userRepository;
    
    /** Encoder for secure password hashing using BCrypt algorithm */
    private final PasswordEncoder passwordEncoder;
    
    /** Manager for handling Spring Security authentication processes */
    private final AuthenticationManager authenticationManager;
    
    /** Provider for generating and validating JWT tokens */
    private final JwtTokenProvider tokenProvider;

    /**
     * Registers a new user in the e-commerce platform.
     * 
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Validates email uniqueness to prevent duplicate accounts</li>
     *   <li>Encrypts the user's password using BCrypt hashing</li>
     *   <li>Persists the user entity to the database</li>
     *   <li>Automatically authenticates the new user</li>
     *   <li>Generates a JWT token for immediate session access</li>
     * </ol>
     * 
     * <p><b>Transaction Management:</b> This method is transactional, ensuring
     * that all database operations either complete successfully or roll back
     * entirely, maintaining data consistency.</p>
     * 
     * @param request the registration request containing user details
     *                (name, email, password, and role)
     * @return {@link AuthResponse} containing the JWT access token and user details
     * @throws BadRequestException if the email is already registered in the system
     * 
     * @see RegisterRequest
     * @see AuthResponse
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validate email uniqueness - prevent duplicate accounts
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        
        // Build user entity with encrypted password
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // BCrypt encryption
                .role(request.getRole())
                .build();
        
        // Persist user to database
        user = userRepository.save(user);
        
        // Automatically authenticate the newly registered user
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        
        // Generate JWT token for immediate access
        String token = tokenProvider.generateToken(auth);
        
        // Extract user principal from authentication object
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        
        // Build and return authentication response with token and user details
        return AuthResponse.builder()
                .accessToken(token)
                .id(principal.getId())
                .email(principal.getEmail())
                .name(user.getName())
                .role(principal.getRole())
                .build();
    }

    /**
     * Authenticates an existing user and provides access credentials.
     * 
     * <p>This method handles user login by:</p>
     * <ol>
     *   <li>Validating credentials against stored encrypted passwords</li>
     *   <li>Creating an authentication context if credentials are valid</li>
     *   <li>Generating a fresh JWT token for the session</li>
     *   <li>Retrieving complete user details from the database</li>
     *   <li>Returning user information along with the access token</li>
     * </ol>
     * 
     * <p><b>Security Flow:</b></p>
     * <pre>
     * Client Credentials → Authentication Manager → Password Verification 
     *   → JWT Generation → Return Token + User Info
     * </pre>
     * 
     * <p><b>Error Handling:</b> If authentication fails due to invalid
     * credentials, Spring Security will automatically throw an
     * {@code AuthenticationException} which is handled by the global
     * exception handler.</p>
     * 
     * @param request the login request containing email and password
     * @return {@link AuthResponse} containing the JWT access token and user details
     * @throws org.springframework.security.core.AuthenticationException if credentials are invalid
     * 
     * @see LoginRequest
     * @see AuthResponse
     * @see JwtTokenProvider
     */
    public AuthResponse login(LoginRequest request) {
        // Authenticate user credentials using Spring Security
        // This validates the password against the encrypted version in database
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        
        // Generate JWT token for authenticated session
        String token = tokenProvider.generateToken(auth);
        
        // Extract authenticated user's principal information
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        
        // Fetch complete user details from database
        User user = userRepository.findById(principal.getId()).orElseThrow();
        
        // Construct and return response with token and user information
        return AuthResponse.builder()
                .accessToken(token)
                .id(principal.getId())
                .email(principal.getEmail())
                .name(user.getName())
                .role(principal.getRole())
                .build();
    }
}
