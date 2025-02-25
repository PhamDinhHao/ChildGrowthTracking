package com.example.Child.Growth.Tracking.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Child.Growth.Tracking.Model.Children;
import com.example.Child.Growth.Tracking.Model.Consultations;
import com.example.Child.Growth.Tracking.Repository.ChildrenRepository;
import com.example.Child.Growth.Tracking.Repository.ConsultationRepository;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    
    public ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    // public List<Consultations> findAll() {
    //     return consultationRepository.findAll();
    // }
    // public List<Consultations> findByUserId(Long userId) {
    //     return consultationRepository.findByUserId(userId);
    // }
    // public Consultations findById(Long id) {
    //     return consultationRepository.findById(id).orElse(null);
    // }
    // public Consultations updateConsultation(Consultations consultation) {
    //     try {
    //         System.out.println("Updating consultation with ID: " + consultation);
    //         Consultations existingConsultation = consultationRepository.findById(consultation.getId())
    //             .orElseThrow(() -> new RuntimeException("Children not found"));
    //         existingConsultation.setRequest(consultation.getRequest());
    //         existingConsultation.setResponse(consultation.getResponse());
    //         existingConsultation.setStatus(consultation.getStatus());
    //         return consultationRepository.save(existingConsultation);
    //     } catch (Exception e) {
    //         throw new RuntimeException("Error updating consultation: " + e.getMessage());
    //     }
    // }
    // public void deleteById(Long id) {
    //     consultationRepository.deleteById(id);
    // }
    public Consultations save(Consultations consultation) {
        try {
            return consultationRepository.save(consultation);
        } catch (Exception e) {
            throw new RuntimeException("Error creating consultation: " + e.getMessage());
        }
    }
    
}
