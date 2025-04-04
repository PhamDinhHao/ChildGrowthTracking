package com.example.Child.Growth.Tracking.Controller.Admin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Child.Growth.Tracking.Model.User;
import com.example.Child.Growth.Tracking.Service.UserService;

@Controller
@RequestMapping("/manageChildren")
public class ManageChildrenController {
    private final UserService userService;

    public ManageChildrenController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String manageChildren(Model model) {
        model.addAttribute("page", "manageChildren");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);
            if (user != null) {
                model.addAttribute("user", user);
            }
        }

        return "admin/manageChildren";
    }
}
