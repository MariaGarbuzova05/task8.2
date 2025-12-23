package com.example.demo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        System.out.println("=== REGISTRATION ATTEMPT ===");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + (user.getPassword() != null ? "[SET]" : "[NULL]"));
        System.out.println("Role: " + user.getRole());
        System.out.println("Validation errors: " + result.hasErrors());

        if (result.hasErrors()) {
            System.out.println("Validation errors details:");
            result.getAllErrors().forEach(error -> System.out.println(" - " + error.getDefaultMessage()));
            return "register";
        }

        try {
            // Устанавливаем роль автоматически
            user.setRole("ROLE_USER");
            System.out.println("Role set to: ROLE_USER");

            System.out.println("Calling userService.registerUser()...");
            userService.registerUser(user);
            System.out.println("User registered successfully!");

            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            System.out.println("Registration failed: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}