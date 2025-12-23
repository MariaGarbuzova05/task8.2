package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final UserRepository userRepository;

    public DebugController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String getUsers() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total users: ").append(userRepository.count()).append("\n");
        userRepository.findAll().forEach(user -> {
            sb.append("User: ").append(user.getUsername())
                    .append(", ID: ").append(user.getId())
                    .append(", Role: ").append(user.getRole())
                    .append(", Password length: ").append(user.getPassword() != null ? user.getPassword().length() : 0)
                    .append("\n");
        });
        return sb.toString();
    }
}