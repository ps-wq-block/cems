package com.cems.repository;

import com.cems.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByStatus(String status);

    long countByOrganizerEmail(String organizerEmail);
}
