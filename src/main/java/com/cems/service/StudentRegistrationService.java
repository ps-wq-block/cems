package com.cems.service;

import com.cems.model.StudentRegistration;
import com.cems.repository.StudentRegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentRegistrationService {

    private final StudentRegistrationRepository repository;

    public StudentRegistrationService(StudentRegistrationRepository repository) {
        this.repository = repository;
    }

    public List<StudentRegistration> saveAll(List<StudentRegistration> registrations) {
        return repository.saveAll(registrations);
    }

    public StudentRegistration registerStudent(StudentRegistration registration) {
        if (registration.getRegistrationDate() == null) {
            registration.setRegistrationDate(LocalDate.now().toString());
        }
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

    public void deleteRegistration(String id) {
        repository.deleteById(id);
    }
}
