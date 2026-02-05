package com.fullstack.jobportal.security;

import com.fullstack.jobportal.entity.User;
import com.fullstack.jobportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        if (username.matches("\\d+")) {
            user = userRepository.findById(Long.parseLong(username))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + username));
        } else {
            user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        }
        return UserPrincipal.from(user);
    }
}
