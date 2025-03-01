package com.example.Child.Growth.Tracking.Controller.User;

import java.time.LocalDate;
import java.util.List;

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
public class ConsultationController {
    private final ConsultationService consultationService;
    private final ChildrenService childrenService;
    private final UserService userService;

    @Autowired
    public ConsultationController(ConsultationService consultationService, ChildrenService childrenService, UserService userService) {
        this.consultationService = consultationService;
        this.childrenService = childrenService;
        this.userService = userService;
    }

    @PostMapping("/consultations/create/{childId}/memberId")
    public String manageConsultations(@ModelAttribute Consultations consultation, @PathVariable Long childId, @RequestParam Long memberId) {
        consultation.setCreatedAt(LocalDate.now());
        consultationService.save(consultation);
        return "redirect:/children";
    }
    @GetMapping("/consultations/list/{childId}")
    public String listConsultations(@PathVariable Long childId, Model model) {
        List<Consultations> consultationsPending = consultationService.findByChildIdAndStatusPending(childId);
        List<Consultations> consultationsCompleted = consultationService.findByChildIdAndStatus(childId);
        consultationsPending.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        consultationsCompleted.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        model.addAttribute("consultationsPending", consultationsPending);
        model.addAttribute("consultationsCompleted", consultationsCompleted);
        return "user/Consultation/index";
    }

    @GetMapping("/manageConsultations")
    public String manageConsultations(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        List<Consultations> consultations = consultationService.findByDoctorId(user.getId());
        // consultations.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        model.addAttribute("consultations", consultations);
        return "doctor/Consultation/index";
    }
    @GetMapping("/manageConsultations/pending")
    public String showPendingConsultations(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        List<Consultations> consultations = consultationService.findByStatusAndDoctorId(ConsultationStatus.PENDING, user.getId());
        model.addAttribute("consultations", consultations);
        return "doctor/Consultation/index";
    }
    @GetMapping("/manageConsultations/completed")
    public String showCompletedConsultations(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).orElse(null);
        List<Consultations> consultations = consultationService.findByStatusAndDoctorId(ConsultationStatus.COMPLETED, user.getId());
        model.addAttribute("consultations", consultations);
        return "doctor/Consultation/index";
    }
}
