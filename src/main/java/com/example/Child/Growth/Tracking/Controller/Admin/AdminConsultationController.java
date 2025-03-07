package com.example.Child.Growth.Tracking.Controller.Admin;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Child.Growth.Tracking.Model.Children;
import com.example.Child.Growth.Tracking.Model.Consultations;
import com.example.Child.Growth.Tracking.Service.ChildrenService;
import com.example.Child.Growth.Tracking.Service.ConsultationService;
import com.example.Child.Growth.Tracking.Service.CustomUserDetailsService;
import com.example.Child.Growth.Tracking.Service.UserService;
import com.example.Child.Growth.Tracking.ulti.ConsultationStatus;
import com.example.Child.Growth.Tracking.ulti.Gender;
import com.example.Child.Growth.Tracking.ulti.UserRole;
import com.example.Child.Growth.Tracking.Model.User;
import com.example.Child.Growth.Tracking.Repository.UserRepository;

@Controller
@RequestMapping("/manageConsultationsAdmin")
public class AdminConsultationController {
    private final ConsultationService consultationService;
    private final ChildrenService childrenService;
    private final UserService userService;

    @Autowired
    public AdminConsultationController(ConsultationService consultationService, ChildrenService childrenService, UserService userService) {
        this.consultationService = consultationService;
        this.childrenService = childrenService;
        this.userService = userService;
    }

    @GetMapping
    public String manageConsultationsAdmin(Model model) {
        model.addAttribute("page", "manageConsultationsAdmin");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            User user = userService.findByUsername(username).orElse(null);

            if (user != null) {
                model.addAttribute("user", user);
            }
        }
        
        List<Consultations> consultations = consultationService.findAll();
        model.addAttribute("consultations", consultations);
        return "admin/manageConsultation";
    }
    @GetMapping("/create")
    public String showCreateConsultationForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
    
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElse(null);
    
        if (user == null) {
            return "redirect:/manageConsultationsAdmin"; 
        }
    
        List<User> members = userService.findByRole(UserRole.MEMBER);
        List<User> doctors = userService.findByRole(UserRole.DOCTOR);
        Map<Long, List<Children>> allChildrenMap = new HashMap<>();
        for (User member : members) {
            List<Children> children = childrenService.findByUserId(member.getId());
            List<Children> childrenDTOs = children.stream()
                .collect(Collectors.toList());
            
            allChildrenMap.put(member.getId(), childrenDTOs);
        }
        model.addAttribute("user", user);
        model.addAttribute("members", members);
        model.addAttribute("doctors", doctors);
        model.addAttribute("allChildrenMap", allChildrenMap);

        model.addAttribute("consultation", new Consultations());
        return "admin/createConsultation";
    }
}