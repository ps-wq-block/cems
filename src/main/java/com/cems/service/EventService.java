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

    public Event getEventById(String id) {
        return repository.findById(id).orElse(null);
    }

    public List<Event> getEventsByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<Event> getEventsByOrganizerEmail(String email) {
        return repository.findByOrganizerEmail(email);
    }

    public Event updateEventStatus(String id, String status, String adminComments) {
        return repository.findById(id).map(event -> {
            event.setStatus(status);
            event.setAdminComments(adminComments);
            return repository.save(event);
        }).orElse(null);
    }

    public Event updateEvent(String id, Event eventDetails) {
        return repository.findById(id).map(event -> {
            event.setName(eventDetails.getName());
            event.setCategory(eventDetails.getCategory());
            event.setEventDate(eventDetails.getEventDate());
            event.setEventTime(eventDetails.getEventTime());
            event.setVenue(eventDetails.getVenue());
            event.setDescription(eventDetails.getDescription());
            event.setMaxParticipants(eventDetails.getMaxParticipants());
            event.setRegistrationDeadline(eventDetails.getRegistrationDeadline());
            event.setRules(eventDetails.getRules());
            event.setRegistrationOpen(eventDetails.isRegistrationOpen());
            // We keep the original organizer and status (or reset if needed)
            return repository.save(event);
        }).orElse(null);
    }
}
