package com.example.Child.Growth.Tracking.Controller;

import com.example.Child.Growth.Tracking.Model.User;
import com.example.Child.Growth.Tracking.ulti.UserRole;
import com.example.Child.Growth.Tracking.Security.JwtUtil;
import com.example.Child.Growth.Tracking.Service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection for dependencies
    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        Optional<User> userOpt = userService.findByUsername(username);

        // If user is found and password matches
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            String token = jwtUtil.generateToken(userOpt.get().getUsername());
            model.addAttribute("token", token); // Add token to the model for the view
            return "home";  // Redirect to home page
        }

        model.addAttribute("error", "Invalid username or password");
        return "login";  // Return to login page with error message
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, 
                       @RequestParam String password,
                       @RequestParam(required = false) String role, 
                       Model model) {
        try {
            // Check if the username already exists
            if (userService.findByUsername(username).isPresent()) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }
            
            // Determine role (default to MEMBER if not provided)
            UserRole userRole = (role != null && !role.isEmpty()) ? getUserRole(role, model) : UserRole.MEMBER;
            if (userRole == null) return "register"; // Return if invalid role

            // Register user with encoded password
            userService.registerUser(username, passwordEncoder.encode(password), userRole);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    // Helper method to convert role string to UserRole enum
    private UserRole getUserRole(String role, Model model) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid role");
            return null; // Return null for invalid role
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("page", "home"); 
        return "home";
    }
    
}
