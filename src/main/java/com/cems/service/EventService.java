package com.cems.service;

import com.cems.model.Event;
import com.cems.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event createEvent(Event event) {
        event.setStatus("Pending"); // Always pending upon creation
        return repository.save(event);
    }

    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    public Event getEventById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Event> getEventsByStatus(String status) {
        return repository.findByStatus(status);
    }

    public Event updateEventStatus(Long id, String status, String adminComments) {
        return repository.findById(id).map(event -> {
            event.setStatus(status);
            event.setAdminComments(adminComments);
            return repository.save(event);
        }).orElse(null);
    }
}
