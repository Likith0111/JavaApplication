package com.fullstack.jobportal.mapper;

import com.fullstack.jobportal.dto.UserDto;
import com.fullstack.jobportal.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .resumeFileName(user.getResumeFileName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
