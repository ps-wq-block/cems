package com.cems.controller;

import com.cems.model.StudentRegistration;
import com.cems.service.StudentRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class StudentRegistrationController {

    private final StudentRegistrationService service;

    public StudentRegistrationController(StudentRegistrationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<StudentRegistration> registerStudent(@RequestBody StudentRegistration registration) {
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
}
