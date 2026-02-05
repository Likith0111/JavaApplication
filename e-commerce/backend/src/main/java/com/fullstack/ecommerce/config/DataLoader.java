package com.fullstack.ecommerce.config;

import com.fullstack.ecommerce.entity.Category;
import com.fullstack.ecommerce.entity.Product;
import com.fullstack.ecommerce.entity.Role;
import com.fullstack.ecommerce.entity.User;
import com.fullstack.ecommerce.repository.CategoryRepository;
import com.fullstack.ecommerce.repository.ProductRepository;
import com.fullstack.ecommerce.repository.UserRepository;
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
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;
        log.info("Loading seed data...");
        userRepository.save(User.builder()
                .name("Admin")
                .email("admin@shop.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build());
        userRepository.save(User.builder()
                .name("Customer")
                .email("customer@shop.com")
                .password(passwordEncoder.encode("customer123"))
                .role(Role.CUSTOMER)
                .build());
        Category electronics = categoryRepository.save(Category.builder().name("Electronics").description("Gadgets and devices").build());
        Category books = categoryRepository.save(Category.builder().name("Books").description("Books and magazines").build());
        productRepository.save(Product.builder().name("Laptop").description("High performance laptop").price(new BigDecimal("999.99")).stock(10).category(electronics).build());
        productRepository.save(Product.builder().name("Phone").description("Smartphone").price(new BigDecimal("499.99")).stock(25).category(electronics).build());
        productRepository.save(Product.builder().name("Java Book").description("Learn Java").price(new BigDecimal("39.99")).stock(100).category(books).build());
        log.info("Seed data loaded.");
    }
}
