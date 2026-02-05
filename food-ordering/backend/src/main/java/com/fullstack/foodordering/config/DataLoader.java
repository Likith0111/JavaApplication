package com.fullstack.foodordering.config;

import com.fullstack.foodordering.entity.*;
import com.fullstack.foodordering.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;
        log.info("Loading seed data...");
        userRepository.save(User.builder().name("Admin").email("admin@food.com").password(passwordEncoder.encode("admin123")).role(Role.ADMIN).build());
        userRepository.save(User.builder().name("User").email("user@food.com").password(passwordEncoder.encode("user123")).role(Role.USER).build());
        Restaurant r1 = restaurantRepository.save(Restaurant.builder().name("Pizza Place").description("Best pizza").address("123 Main St").build());
        Restaurant r2 = restaurantRepository.save(Restaurant.builder().name("Burger Joint").description("Burgers").address("456 Oak Ave").build());
        menuItemRepository.save(MenuItem.builder().name("Margherita").description("Tomato, cheese").price(new BigDecimal("12.99")).restaurant(r1).build());
        menuItemRepository.save(MenuItem.builder().name("Pepperoni").description("Pepperoni, cheese").price(new BigDecimal("14.99")).restaurant(r1).build());
        menuItemRepository.save(MenuItem.builder().name("Cheeseburger").description("Beef, cheese").price(new BigDecimal("9.99")).restaurant(r2).build());
        log.info("Seed data loaded.");
    }
}
