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

    public User registerUser(String username, String password, UserRole role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role != null ? role : UserRole.MEMBER);
        return userRepository.save(user);
    }

    // Đảm bảo rằng phương thức này đã được định nghĩa trong UserRepository
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    // Các phương thức khác
}
