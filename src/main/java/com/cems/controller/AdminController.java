package com.cems.controller;

import com.cems.repository.EventRepository;
import com.cems.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public AdminController(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalEvents", eventRepository.count());
        stats.put("pendingApprovals", eventRepository.findByStatus("Pending").size());

        return ResponseEntity.ok(stats);
    }
}
