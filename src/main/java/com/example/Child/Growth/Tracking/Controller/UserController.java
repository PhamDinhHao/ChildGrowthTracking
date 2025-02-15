package com.example.Child.Growth.Tracking.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Child.Growth.Tracking.Model.User;
import com.example.Child.Growth.Tracking.Service.UserService;
import com.example.Child.Growth.Tracking.ulti.UserRole;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute("page", "profile");
        return "profile";
    }

    @GetMapping("/manageUsers")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "manageUsers";
    }

    @GetMapping("/manageUsers/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("availableRoles", UserRole.values());
        return "editUser";
    }

    @PostMapping("/manageUsers/update")
    public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            return "redirect:/manageUsers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
            return "redirect:/manageUsers";
        }
    }

    @GetMapping("/manageUsers/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/manageUsers"; // Redirect về trang quản lý users sau khi xóa
    }

    @GetMapping("/manageUsers/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("availableRoles", UserRole.values());
        return "createUser";
    }

    @PostMapping("/manageUsers/create")
    public String createUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/manageUsers";
    }
}
