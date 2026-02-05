package com.fullstack.jobportal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullstack.jobportal.dto.AuthResponse;
import com.fullstack.jobportal.dto.LoginRequest;
import com.fullstack.jobportal.dto.RegisterRequest;
import com.fullstack.jobportal.entity.Role;
import com.fullstack.jobportal.security.JwtAuthenticationFilter;
import com.fullstack.jobportal.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void register_withValidRequest_returns201() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .role(Role.CANDIDATE)
                .build();
        AuthResponse response = AuthResponse.builder()
                .accessToken("token")
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .role(Role.CANDIDATE)
                .build();
        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void login_withValidRequest_returns200() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        AuthResponse response = AuthResponse.builder()
                .accessToken("token")
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .role(Role.CANDIDATE)
                .build();
        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token"));
    }
}
