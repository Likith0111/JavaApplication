package com.fullstack.ecommerce.dto;

import com.fullstack.ecommerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private Long id;
    private String email;
    private String name;
    private Role role;
}
