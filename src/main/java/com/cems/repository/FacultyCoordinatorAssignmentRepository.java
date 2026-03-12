package com.cems.repository;

import com.cems.model.FacultyCoordinatorAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyCoordinatorAssignmentRepository extends JpaRepository<FacultyCoordinatorAssignment, Long> {
}
