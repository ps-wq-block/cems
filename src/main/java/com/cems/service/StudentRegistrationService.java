package com.cems.service;

import com.cems.model.StudentRegistration;
import com.cems.repository.StudentRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentRegistrationService {

    private final StudentRegistrationRepository repository;

    public StudentRegistrationService(StudentRegistrationRepository repository) {
        this.repository = repository;
    }

    public StudentRegistration registerStudent(StudentRegistration registration) {
        // You could later add checks here (e.g., max participants limit)
        return repository.save(registration);
    }

    public List<StudentRegistration> getAllRegistrations() {
        return repository.findAll();
    }

    public List<StudentRegistration> getRegistrationsByEvent(String eventName) {
        return repository.findByEventName(eventName);
    }

    public List<StudentRegistration> getRegistrationsByEmail(String email) {
        return repository.findByEmail(email);
    }
}
