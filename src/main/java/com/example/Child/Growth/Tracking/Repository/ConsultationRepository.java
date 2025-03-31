package com.example.Child.Growth.Tracking.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Child.Growth.Tracking.Model.Consultations;
import com.example.Child.Growth.Tracking.ulti.ConsultationStatus;

public interface ConsultationRepository extends JpaRepository<Consultations, Long> {
    Consultations save(Consultations consultation);
    List<Consultations> findByChildId(Long childId);
    List<Consultations> findByChildIdAndStatus(Long childId, ConsultationStatus status);
    List<Consultations> findByDoctorId(Long doctorId);
    List<Consultations> findByStatus(ConsultationStatus status);
    List<Consultations> findByStatusAndDoctorId(ConsultationStatus status, Long doctorId);

    @Query("SELECT c, ch.fullName as childName, u.fullName as userName, u.phoneNumber as userPhone " +
           "FROM Consultations c " +
           "JOIN Children ch ON c.childId = ch.id " +
           "JOIN User u ON ch.userId = u.id " +
           "WHERE c.doctorId = :doctorId")
    List<Object[]> findConsultationDetailsByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT c, ch.fullName as childName, u.fullName as userName, u.phoneNumber as userPhone " +
           "FROM Consultations c " +
           "JOIN Children ch ON c.childId = ch.id " +
           "JOIN User u ON ch.userId = u.id " +
           "WHERE c.doctorId = :doctorId AND c.status = :status")
    List<Object[]> findConsultationDetailsByStatusAndDoctorId(
        @Param("status") ConsultationStatus status, 
        @Param("doctorId") Long doctorId
    );
}
