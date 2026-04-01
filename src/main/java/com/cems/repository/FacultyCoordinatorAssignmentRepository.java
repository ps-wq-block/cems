package com.cems.repository;

import com.cems.model.FacultyCoordinatorAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyCoordinatorAssignmentRepository extends MongoRepository<FacultyCoordinatorAssignment, String> {
    Optional<FacultyCoordinatorAssignment> findByEvent(String event);
    Optional<FacultyCoordinatorAssignment> findByEmailAddress(String emailAddress);
    Optional<FacultyCoordinatorAssignment> findByFacultyId(String facultyId);
}
