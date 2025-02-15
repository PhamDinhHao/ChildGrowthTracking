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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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
    public String showLoginForm(Model model) {
        model.addAttribute("page", "login"); 
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        Optional<User> userOpt = userService.findByUsername(username);
        
        // Kiểm tra mật khẩu
        if (userOpt.isPresent()) {

            logger.debug("Password matches: {}", passwordEncoder.matches(password, userOpt.get().getPassword()));  // In ra kết quả kiểm tra mật khẩu
            System.out.println("Password matches: {}" + passwordEncoder.matches(password, userOpt.get().getPassword()));  // In ra kết quả kiểm tra mật khẩu

        }

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
    public String showRegisterPage(Model model) {
        model.addAttribute("page", "register"); 

        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, 
                        @RequestParam String password,
                        @RequestParam String fullname,
                        @RequestParam String email,
                        @RequestParam String phoneNumber,
                        @RequestParam(required = false) String role, 
                        Model model) {
        try {
            // Kiểm tra tên người dùng đã tồn tại
            if (userService.findByUsername(username).isPresent()) {
                model.addAttribute("error", "Username already exists");
                return "register";  // Trả về trang đăng ký với thông báo lỗi
            }

            // Kiểm tra vai trò người dùng
            UserRole userRole = (role != null && !role.isEmpty()) ? getUserRole(role, model) : UserRole.MEMBER;
            if (userRole == null) return "register"; // Trả về nếu vai trò không hợp lệ

            // Đăng ký người dùng
            userService.registerUser(username, password, userRole, fullname, email, phoneNumber);
            return "login";  // Chuyển hướng đến trang đăng nhập
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";  // Trả về trang đăng ký nếu có lỗi
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
