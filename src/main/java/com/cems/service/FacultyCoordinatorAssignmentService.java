package com.cems.service;

import com.cems.model.FacultyCoordinatorAssignment;
import com.cems.repository.FacultyCoordinatorAssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyCoordinatorAssignmentService {

    private final FacultyCoordinatorAssignmentRepository repository;

    public FacultyCoordinatorAssignmentService(FacultyCoordinatorAssignmentRepository repository) {
        this.repository = repository;
    }

    public FacultyCoordinatorAssignment assignCoordinator(FacultyCoordinatorAssignment assignment) {
        // Here we could add logic to actually notify the faculty via email.
        return repository.save(assignment);
    }

    public List<FacultyCoordinatorAssignment> getAllAssignments() {
        return repository.findAll();
    }

    public FacultyCoordinatorAssignment getAssignmentById(String id) {
        return repository.findById(id).orElse(null);
    }

    public FacultyCoordinatorAssignment updateAssignment(String id, FacultyCoordinatorAssignment updatedAssignment) {
        return repository.findById(id).map(assignment -> {
            assignment.setEvent(updatedAssignment.getEvent());
            assignment.setFacultyName(updatedAssignment.getFacultyName());
            assignment.setFacultyId(updatedAssignment.getFacultyId());
            assignment.setDepartment(updatedAssignment.getDepartment());
            assignment.setContactNumber(updatedAssignment.getContactNumber());
            assignment.setEmailAddress(updatedAssignment.getEmailAddress());
            assignment.setResponsibilities(updatedAssignment.getResponsibilities());
            return repository.save(assignment);
        }).orElse(null);
    }

    public boolean deleteAssignment(String id) {
        return repository.findById(id).map(assignment -> {
            repository.delete(assignment);
            return true;
        }).orElse(false);
    }
}
