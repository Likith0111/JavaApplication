package com.fullstack.jobportal.dto;

import com.fullstack.jobportal.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private String resumeFileName;
    private Instant createdAt;
}
