package com.cems.repository;

import com.cems.model.FacultyCoordinatorAssignment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyCoordinatorAssignmentRepository extends MongoRepository<FacultyCoordinatorAssignment, String> {
}
