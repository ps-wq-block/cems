package com.cems.repository;

import com.cems.model.StudentRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRegistrationRepository extends MongoRepository<StudentRegistration, String> {
    List<StudentRegistration> findByEventName(String eventName);
}
