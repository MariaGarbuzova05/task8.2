package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderProvider passwordEncoderProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user by username: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("User not found: " + username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        System.out.println("User found: " + user.getUsername() + ", role: " + user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    @Transactional
    public User registerUser(User user) {
        System.out.println("\n=== USER SERVICE: REGISTER USER ===");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Raw password: " + user.getPassword());
        System.out.println("Role: " + user.getRole());

        // Проверяем существование пользователя
        boolean userExists = userRepository.existsByUsername(user.getUsername());
        System.out.println("User exists? " + userExists);

        if (userExists) {
            throw new RuntimeException("Username '" + user.getUsername() + "' already exists");
        }

        // Проверяем пароль
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password cannot be empty");
        }

        if (user.getPassword().length() < 6) {
            throw new RuntimeException("Password must be at least 6 characters");
        }

        // Кодируем пароль
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoderProvider.getPasswordEncoder().encode(rawPassword);
        user.setPassword(encodedPassword);
        System.out.println("Password encoded: " + encodedPassword.substring(0, Math.min(20, encodedPassword.length())) + "...");

        // Устанавливаем роль если не установлена
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
            System.out.println("Role set to default: ROLE_USER");
        }

        // Убедимся, что роль имеет префикс ROLE_
        if (!user.getRole().startsWith("ROLE_")) {
            user.setRole("ROLE_" + user.getRole());
        }

        System.out.println("Final role: " + user.getRole());

        // Сохраняем пользователя
        User savedUser = userRepository.save(user);
        System.out.println("User saved with ID: " + savedUser.getId());

        // Проверяем, что пользователь действительно сохранен
        User retrievedUser = userRepository.findByUsername(savedUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve saved user"));

        System.out.println("Retrieved user: " + retrievedUser.getUsername());
        System.out.println("Registration completed successfully!\n");

        return savedUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}