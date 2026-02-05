package com.fullstack.jobportal.service;

import com.fullstack.jobportal.dto.AuthResponse;
import com.fullstack.jobportal.dto.LoginRequest;
import com.fullstack.jobportal.dto.RegisterRequest;
import com.fullstack.jobportal.entity.Role;
import com.fullstack.jobportal.entity.User;
import com.fullstack.jobportal.exception.BadRequestException;
import com.fullstack.jobportal.repository.UserRepository;
import com.fullstack.jobportal.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider tokenProvider;
    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private User savedUser;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .role(Role.CANDIDATE)
                .build();
        savedUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("encoded")
                .role(Role.CANDIDATE)
                .build();
        authentication = new UsernamePasswordAuthenticationToken(
                new com.fullstack.jobportal.security.UserPrincipal(1L, "test@example.com", "encoded", Role.CANDIDATE),
                null,
                null
        );
    }

    @Test
    void register_whenEmailNotExists_returnsAuthResponse() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getRole()).isEqualTo(Role.CANDIDATE);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_whenEmailExists_throwsBadRequestException() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void login_whenValidCredentials_returnsAuthResponse() {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("jwt-token");
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        AuthResponse response = authService.login(request);

        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getName()).isEqualTo("Test User");
    }
}
