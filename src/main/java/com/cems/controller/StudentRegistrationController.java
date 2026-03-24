package com.cems.controller;

import com.cems.model.Event;
import com.cems.model.StudentRegistration;
import com.cems.repository.EventRepository;
import com.cems.service.StudentRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class StudentRegistrationController {

    private final StudentRegistrationService service;
    private final EventRepository eventRepository;

    public StudentRegistrationController(StudentRegistrationService service, EventRepository eventRepository) {
        this.service = service;
        this.eventRepository = eventRepository;
    }

    @PostMapping
    public ResponseEntity<StudentRegistration> registerStudent(@RequestBody StudentRegistration registration) {
        // Auto-populate eventDate from Event model if not set
        if (registration.getEventDate() == null && registration.getEventName() != null) {
            eventRepository.findAll().stream()
                .filter(e -> registration.getEventName().equals(e.getName()))
                .findFirst()
                .ifPresent(e -> registration.setEventDate(e.getEventDate()));
        }
        return ResponseEntity.ok(service.registerStudent(registration));
    }

    @GetMapping
    public ResponseEntity<List<StudentRegistration>> getAllRegistrations() {
        return ResponseEntity.ok(service.getAllRegistrations());
    }

    @GetMapping("/event/{eventName}")
    public ResponseEntity<List<StudentRegistration>> getRegistrationsByEvent(@PathVariable String eventName) {
        return ResponseEntity.ok(service.getRegistrationsByEvent(eventName));
    }

    @GetMapping("/student/{email}")
    public ResponseEntity<List<StudentRegistration>> getRegistrationsByStudent(@PathVariable String email) {
        return ResponseEntity.ok(service.getRegistrationsByEmail(email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable String id) {
        service.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/bulk-update")
    public ResponseEntity<List<StudentRegistration>> bulkUpdateRegistrations(@RequestBody List<StudentRegistration> registrations) {
        return ResponseEntity.ok(service.saveAll(registrations));
    }
}
