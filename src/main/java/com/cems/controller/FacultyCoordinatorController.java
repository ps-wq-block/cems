package com.cems.controller;

import com.cems.model.FacultyCoordinatorAssignment;
import com.cems.service.FacultyCoordinatorAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin(origins = "*") // Allow requests from any origin for simplicity in this demo
public class FacultyCoordinatorController {

    private final FacultyCoordinatorAssignmentService service;

    public FacultyCoordinatorController(FacultyCoordinatorAssignmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> createAssignment(
            @RequestBody FacultyCoordinatorAssignment assignment) {
        try {
            FacultyCoordinatorAssignment savedAssignment = service.assignCoordinator(assignment);
            return ResponseEntity.ok(savedAssignment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<FacultyCoordinatorAssignment>> getAllAssignments() {
        return ResponseEntity.ok(service.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyCoordinatorAssignment> getAssignmentById(@PathVariable String id) {
        FacultyCoordinatorAssignment assignment = service.getAssignmentById(id);
        if (assignment != null) {
            return ResponseEntity.ok(assignment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(
            @PathVariable String id,
            @RequestBody FacultyCoordinatorAssignment assignment) {
        try {
            FacultyCoordinatorAssignment updatedAssignment = service.updateAssignment(id, assignment);
            if (updatedAssignment != null) {
                return ResponseEntity.ok(updatedAssignment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable String id) {
        if (service.deleteAssignment(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
