package com.cems.controller;

import com.cems.repository.EventRepository;
import com.cems.repository.StudentRegistrationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organizer")
public class OrganizerController {

    private final EventRepository eventRepository;
    private final StudentRegistrationRepository studentRegistrationRepository;

    public OrganizerController(EventRepository eventRepository,
            StudentRegistrationRepository studentRegistrationRepository) {
        this.eventRepository = eventRepository;
        this.studentRegistrationRepository = studentRegistrationRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getOrganizerStats() {
        Map<String, Object> stats = new HashMap<>();

        // Extract the currently logged-in organizer's email from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String organizerEmail = null;

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            organizerEmail = userDetails.getUsername(); // We use email as the username
        }

        if (organizerEmail != null) {
            List<com.cems.model.Event> myEvents = eventRepository.findByOrganizerEmail(organizerEmail);
            long myEventsCount = myEvents.size();
            stats.put("myEvents", myEventsCount);

            List<String> eventNames = myEvents.stream().map(com.cems.model.Event::getName)
                    .collect(java.util.stream.Collectors.toList());
            long myRegistrationsCount = studentRegistrationRepository.findByEventNameIn(eventNames).size();
            stats.put("totalRegistrations", myRegistrationsCount);
        } else {
            stats.put("myEvents", 0);
            stats.put("totalRegistrations", 0);
        }

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/events")
    public ResponseEntity<List<com.cems.model.Event>> getMyEvents() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(eventRepository.findByOrganizerEmail(userDetails.getUsername()));
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/registrations")
    public ResponseEntity<List<com.cems.model.StudentRegistration>> getMyRegistrations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            List<com.cems.model.Event> myEvents = eventRepository.findByOrganizerEmail(email);
            List<String> eventNames = myEvents.stream().map(com.cems.model.Event::getName)
                    .collect(java.util.stream.Collectors.toList());

            System.out.println("DEBUG: Organizer " + email + " has events: " + eventNames);

            List<com.cems.model.StudentRegistration> regs = studentRegistrationRepository.findByEventNameIn(eventNames);
            System.out.println("DEBUG: Found " + regs.size() + " registrations for these events.");

            return ResponseEntity.ok(regs);
        }
        return ResponseEntity.status(403).build();
    }
}
