package com.example.Child.Growth.Tracking.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Child.Growth.Tracking.Model.Children;
import com.example.Child.Growth.Tracking.Model.Consultations;
import com.example.Child.Growth.Tracking.Repository.ChildrenRepository;
import com.example.Child.Growth.Tracking.Repository.ConsultationRepository;
import com.example.Child.Growth.Tracking.ulti.ConsultationStatus;

@Service
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    
    public ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    public List<Consultations> findAll() {
        return consultationRepository.findAll();
    }
    public Consultations save(Consultations consultation) {
        try {
            consultation.setCreatedAt(LocalDate.now());
            return consultationRepository.save(consultation);
        } catch (Exception e) {
            throw new RuntimeException("Error creating consultation: " + e.getMessage());
        }
    }
    public List<Map<String, Object>> findByDoctorId(Long doctorId) {
        List<Object[]> results = consultationRepository.findConsultationDetailsByDoctorId(doctorId);
        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            Consultations consultation = (Consultations) result[0];
            map.putAll(convertConsultationToMap(consultation));
            map.put("childName", result[1]);
            map.put("userName", result[2]);
            map.put("userPhone", result[3]);
            return map;
        }).collect(Collectors.toList());
    }
    public List<Consultations> findByChildId(Long childId) {
        return consultationRepository.findByChildId(childId);
    }
    public List<Consultations> findByChildIdAndStatus(Long childId) {
        return consultationRepository.findByChildIdAndStatus(childId, ConsultationStatus.COMPLETED);
    }
    public List<Consultations> findByChildIdAndStatusPending(Long childId) {
        return consultationRepository.findByChildIdAndStatus(childId, ConsultationStatus.PENDING);
    }
    public List<Map<String, Object>> findByStatusAndDoctorId(ConsultationStatus status, Long doctorId) {
        List<Object[]> results = consultationRepository.findConsultationDetailsByStatusAndDoctorId(status, doctorId);
        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            Consultations consultation = (Consultations) result[0];
            map.putAll(convertConsultationToMap(consultation));
            map.put("childName", result[1]);
            map.put("userName", result[2]);
            map.put("userPhone", result[3]);
            return map;
        }).collect(Collectors.toList());
    }
    public void updateResponse(Long id, String response) {
        Consultations consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found"));
        consultation.setResponse(response);
        consultation.setStatus(ConsultationStatus.COMPLETED);
        consultationRepository.save(consultation);
    }
    public void deleteById(Long id) {
        consultationRepository.deleteById(id);
    }

    private Map<String, Object> convertConsultationToMap(Consultations consultation) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", consultation.getId());
        map.put("request", consultation.getRequest());
        map.put("response", consultation.getResponse());
        map.put("status", consultation.getStatus());
        map.put("createdAt", consultation.getCreatedAt());
        map.put("memberId", consultation.getMemberId());
        map.put("doctorId", consultation.getDoctorId());
        map.put("childId", consultation.getChildId());
        return map;
    }
}
