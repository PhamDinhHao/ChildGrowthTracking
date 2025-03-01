package com.example.Child.Growth.Tracking.Controller.Admin;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Child.Growth.Tracking.Model.User;
import com.example.Child.Growth.Tracking.Model.FeedBack;
import com.example.Child.Growth.Tracking.Service.UserService;
import com.example.Child.Growth.Tracking.Service.FeedBackService;

import java.util.List;

@Controller
@RequestMapping("/manageFeedbacks")
public class ManagerFeedbacksController {
    private final UserService userService;
    private final FeedBackService feedbackService;

    // Constructor Injection
    public ManagerFeedbacksController(UserService userService, FeedBackService feedbackService) {
        this.userService = userService;
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String manageFeedbacks(Model model) {
        model.addAttribute("page", "manageFeedbacks");  // Đặt trang hiện tại

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName(); 
            User user = userService.findByUsername(username).orElse(null);  

            if (user != null) {
                model.addAttribute("user", user);
            }
        }

        // Lấy danh sách feedback từ service
        List<FeedBack> feedbackList = feedbackService.getAllFeedbacks();
        model.addAttribute("feedbacks", feedbackList);

        return "admin/manageFeedbacks";
    }

    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        feedbackService.deleteFeedbackById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Feedback deleted successfully!");
        return "redirect:/manageFeedbacks"; // Kiểm tra xem đường dẫn này có đúng với trang hiển thị danh sách không
    }
}
