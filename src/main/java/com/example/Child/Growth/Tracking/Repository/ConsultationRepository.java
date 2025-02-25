package com.example.Child.Growth.Tracking.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Child.Growth.Tracking.Model.Children;
import com.example.Child.Growth.Tracking.Model.Consultations;

public interface ConsultationRepository extends JpaRepository<Consultations, Long> {
    Consultations save(Consultations consultation);
}
