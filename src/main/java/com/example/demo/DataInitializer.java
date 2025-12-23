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
            admin.setRole("ROLE_ADMIN"); // Добавляем префикс ROLE_
            userRepository.save(admin);
            System.out.println("Default admin user created: admin/admin");
        }

        // Создаем тестового пользователя
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoderProvider.getPasswordEncoder().encode("user"));
            user.setRole("ROLE_USER"); // Добавляем префикс ROLE_
            userRepository.save(user);
            System.out.println("Test user created: user/user");
        }

        // Выводим информацию о созданных пользователях
        System.out.println("Total users in database: " + userRepository.count());
    }
}