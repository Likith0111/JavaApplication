package com.fullstack.eventbooking.config;

import com.fullstack.eventbooking.entity.Event;
import com.fullstack.eventbooking.entity.Role;
import com.fullstack.eventbooking.entity.User;
import com.fullstack.eventbooking.repository.EventRepository;
import com.fullstack.eventbooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;
        log.info("Loading seed data...");
        userRepository.save(User.builder().name("Admin").email("admin@events.com").password(passwordEncoder.encode("admin123")).role(Role.ADMIN).build());
        userRepository.save(User.builder().name("User").email("user@events.com").password(passwordEncoder.encode("user123")).role(Role.USER).build());
        eventRepository.save(Event.builder().name("Tech Conference 2025").description("Annual tech conference").venue("Convention Center").eventDate(Instant.now().plusSeconds(86400 * 30)).totalSeats(500).availableSeats(500).build());
        eventRepository.save(Event.builder().name("Music Festival").description("Summer music festival").venue("Central Park").eventDate(Instant.now().plusSeconds(86400 * 60)).totalSeats(2000).availableSeats(2000).build());
        log.info("Seed data loaded.");
    }
}
