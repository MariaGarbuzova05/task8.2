package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoderProvider passwordEncoderProvider;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoderProvider passwordEncoderProvider) {
        this.userRepository = userRepository;
        this.passwordEncoderProvider = passwordEncoderProvider;
    }

    @Override
    public void run(String... args) throws Exception {
        // Создаем администратора по умолчанию
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoderProvider.getPasswordEncoder().encode("admin"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
        }

        // Создаем тестового пользователя
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoderProvider.getPasswordEncoder().encode("user"));
            user.setRole("ROLE_USER");
            userRepository.save(user);
        }
    }
}