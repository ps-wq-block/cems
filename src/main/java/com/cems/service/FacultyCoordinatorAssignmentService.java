package com.cems.service;

import com.cems.model.FacultyCoordinatorAssignment;
import com.cems.repository.FacultyCoordinatorAssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class FacultyCoordinatorAssignmentService {

    private final FacultyCoordinatorAssignmentRepository repository;
    private final com.cems.repository.NotificationRepository notificationRepository;

    public FacultyCoordinatorAssignmentService(FacultyCoordinatorAssignmentRepository repository, 
                                               com.cems.repository.NotificationRepository notificationRepository) {
        this.repository = repository;
        this.notificationRepository = notificationRepository;
    }

    public FacultyCoordinatorAssignment assignCoordinator(FacultyCoordinatorAssignment assignment) {
        if (repository.findByEvent(assignment.getEvent()).isPresent()) {
            throw new IllegalArgumentException("A coordinator is already assigned to this event.");
        }
        if (repository.findByEmailAddress(assignment.getEmailAddress()).isPresent()) {
            throw new IllegalArgumentException("This email address is already registered as a coordinator for another event.");
        }
        if (repository.findByFacultyId(assignment.getFacultyId()).isPresent()) {
            throw new IllegalArgumentException("This Faculty ID is already registered as a coordinator for another event.");
        }
        
        FacultyCoordinatorAssignment saved = repository.save(assignment);
        
        // Notify Coordinator
        com.cems.model.Notification n = new com.cems.model.Notification();
        n.setReceiver(saved.getEmailAddress());
        n.setTitle("New Assignment: " + saved.getEvent());
        n.setMessage("You have been assigned as the Faculty Coordinator for " + saved.getEvent() + 
                     ". Your responsibilities: " + saved.getResponsibilities());
        n.setEventDate(saved.getEventDate());
        n.setEventTime(saved.getEventTime());
        n.setCreatedAt(java.time.LocalDateTime.now());
        notificationRepository.save(n);
        
        return saved;
    }

    public List<FacultyCoordinatorAssignment> getAllAssignments() {
        return repository.findAll();
    }

    public FacultyCoordinatorAssignment getAssignmentById(String id) {
        return repository.findById(id).orElse(null);
    }

    public FacultyCoordinatorAssignment updateAssignment(String id, FacultyCoordinatorAssignment updatedAssignment) {
        return repository.findById(id).map(assignment -> {

            // Validate Email uniqueness on update
            Optional<FacultyCoordinatorAssignment> byEmail = repository.findByEmailAddress(updatedAssignment.getEmailAddress());
            if (byEmail.isPresent() && !byEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("This email address is already in use by another coordinator.");
            }

            // Validate FacultyId uniqueness on update
            Optional<FacultyCoordinatorAssignment> byFacultyId = repository.findByFacultyId(updatedAssignment.getFacultyId());
            if (byFacultyId.isPresent() && !byFacultyId.get().getId().equals(id)) {
                throw new IllegalArgumentException("This Faculty ID is already in use by another coordinator.");
            }

            assignment.setEvent(updatedAssignment.getEvent());
            assignment.setFacultyName(updatedAssignment.getFacultyName());
            assignment.setFacultyId(updatedAssignment.getFacultyId());
            assignment.setDepartment(updatedAssignment.getDepartment());
            assignment.setContactNumber(updatedAssignment.getContactNumber());
            assignment.setEmailAddress(updatedAssignment.getEmailAddress());
            assignment.setResponsibilities(updatedAssignment.getResponsibilities());
            assignment.setEventDate(updatedAssignment.getEventDate());
            assignment.setEventTime(updatedAssignment.getEventTime());
            FacultyCoordinatorAssignment saved = repository.save(assignment);

            // Notify Coordinator of update
            com.cems.model.Notification n = new com.cems.model.Notification();
            n.setReceiver(saved.getEmailAddress());
            n.setTitle("Updated Assignment: " + saved.getEvent());
            n.setMessage("Your assignment details for " + saved.getEvent() + 
                         " have been updated. Your new responsibilities: " + saved.getResponsibilities());
            n.setEventDate(saved.getEventDate());
            n.setEventTime(saved.getEventTime());
            n.setCreatedAt(java.time.LocalDateTime.now());
            notificationRepository.save(n);

            return saved;
        }).orElse(null);
    }

    public boolean deleteAssignment(String id) {
        return repository.findById(id).map(assignment -> {
            repository.delete(assignment);
            return true;
        }).orElse(false);
    }
}
