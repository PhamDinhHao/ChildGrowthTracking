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
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // User user = userService.findByUsername(auth.getName()).orElse(null);
        // if (user == null) {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        // }
        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.findById(id)
                .map(existingUser -> {
                    user.setId(id);
                    // if (user.getCreatedAt() == null) {
                    //     user.setCreatedAt(existingUser.getCreatedAt());
                    // }
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
}
