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
            long myEventsCount = eventRepository.countByOrganizerEmail(organizerEmail);
            stats.put("myEvents", myEventsCount);

            // Note: In a fully relationships-mapped system, we'd query registrations linked
            // to specific events.
            // For now, we return a simple total registration count for demonstration.
            stats.put("totalRegistrations", studentRegistrationRepository.count());
        } else {
            stats.put("myEvents", 0);
            stats.put("totalRegistrations", 0);
        }

        return ResponseEntity.ok(stats);
    }
}
