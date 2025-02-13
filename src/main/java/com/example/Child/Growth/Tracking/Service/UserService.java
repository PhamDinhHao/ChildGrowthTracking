package com.example.Child.Growth.Tracking.Service;

import com.example.Child.Growth.Tracking.Model.User;
import com.example.Child.Growth.Tracking.Repository.UserRepository;
import com.example.Child.Growth.Tracking.ulti.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register user with additional fields like fullname, email, phoneNumber
    public User registerUser(String username, String password, UserRole role, String fullname, String email, String phoneNumber) {
        // Check if username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        // Create new user object
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);  // Lưu mật khẩu gốc (plain text)
        user.setRole(role != null ? role : UserRole.MEMBER);  // Set role (default to MEMBER if null)
        user.setFullname(fullname);  // Set fullname
        user.setEmail(email);  // Set email
        user.setPhoneNumber(phoneNumber);  // Set phone number

        // Save user to database
        return userRepository.save(user);
    }

    // Method to find user by username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Other methods if needed
}
