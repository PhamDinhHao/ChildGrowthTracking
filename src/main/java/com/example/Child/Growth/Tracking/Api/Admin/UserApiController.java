package com.example.Child.Growth.Tracking.Api.Admin;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.Child.Growth.Tracking.Model.BlogPost;
import com.example.Child.Growth.Tracking.Model.User;
import com.example.Child.Growth.Tracking.Service.BlogService;
import com.example.Child.Growth.Tracking.Service.UserService;
import com.example.Child.Growth.Tracking.ulti.UserRole;

@RestController
@RequestMapping("/api/users")
public class UserApiController {
    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Kiểm tra xem username đã tồn tại chưa
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Username already exists"));
        }

        // Kiểm tra xem email đã tồn tại chưa
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email already exists"));
        }

        // Kiểm tra xem số điện thoại đã tồn tại chưa
        if (userService.existsByPhone(user.getPhoneNumber())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Phone number already exists"));
        }

        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.findById(id)
                .map(existingUser -> {
                    // Kiểm tra xem email đã tồn tại chưa
                    if (!existingUser.getEmail().equals(user.getEmail()) && userService.existsByEmail(user.getEmail())) {
                        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Email already exists"));
                    }

                    // Kiểm tra xem số điện thoại đã tồn tại chưa
                    if (!existingUser.getPhoneNumber().equals(user.getPhoneNumber()) && userService.existsByPhone(user.getPhoneNumber())) {
                        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Phone number already exists"));
                    }

                    user.setId(id);
                    User updatedUser = userService.save(user);
                    return ResponseEntity.ok(updatedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-username-edit")
    public ResponseEntity<Map<String, Boolean>> checkUsernameForEdit(
            @RequestParam String username,
            @RequestParam Long userId) {
        boolean exists = userService.existsByUsernameAndIdNot(username, userId);
        return ResponseEntity.ok(Collections.singletonMap("available", !exists));
    }

    @GetMapping("/check-email-edit")
    public ResponseEntity<Map<String, Boolean>> checkEmailForEdit(
            @RequestParam String email,
            @RequestParam Long userId) {
        boolean exists = userService.existsByEmailAndIdNot(email, userId);
        return ResponseEntity.ok(Collections.singletonMap("available", !exists));
    }

    @GetMapping("/check-phone-edit")
    public ResponseEntity<Map<String, Boolean>> checkPhoneForEdit(
            @RequestParam String phone,
            @RequestParam Long userId) {
        boolean exists = userService.existsByPhoneNumberAndIdNot(phone, userId);
        return ResponseEntity.ok(Collections.singletonMap("available", !exists));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<User>> getDoctors() {
        List<User> doctors = userService.findByRole(UserRole.DOCTOR);
        return ResponseEntity.ok(doctors);
    }
}
