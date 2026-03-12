package com.cems.repository;

import com.cems.model.StudentRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRegistrationRepository extends JpaRepository<StudentRegistration, Long> {
    List<StudentRegistration> findByEventName(String eventName);
}
