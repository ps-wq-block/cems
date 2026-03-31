package com.cems.repository;

import com.cems.model.StudentRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRegistrationRepository extends MongoRepository<StudentRegistration, String> {
    List<StudentRegistration> findByEventName(String eventName);

    List<StudentRegistration> findByEventNameIn(List<String> eventNames);

    List<StudentRegistration> findByEmail(String email);

    Optional<StudentRegistration> findByEmailAndEventName(String email, String eventName);
}
