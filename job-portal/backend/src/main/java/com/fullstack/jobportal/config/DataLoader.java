package com.fullstack.jobportal.config;

import com.fullstack.jobportal.entity.Role;
import com.fullstack.jobportal.entity.User;
import com.fullstack.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }
        log.info("Loading seed data...");
        userRepository.save(User.builder()
                .name("Admin User")
                .email("admin@jobportal.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build());
        userRepository.save(User.builder()
                .name("Recruiter One")
                .email("recruiter@jobportal.com")
                .password(passwordEncoder.encode("recruiter123"))
                .role(Role.RECRUITER)
                .build());
        userRepository.save(User.builder()
                .name("Candidate One")
                .email("candidate@jobportal.com")
                .password(passwordEncoder.encode("candidate123"))
                .role(Role.CANDIDATE)
                .build());
        log.info("Seed data loaded.");
    }
}
