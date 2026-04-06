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
    private final com.cems.service.BackupService backupService;

    public AdminController(UserRepository userRepository, 
                           EventRepository eventRepository,
                           com.cems.service.BackupService backupService) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.backupService = backupService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalEvents", eventRepository.count());
        stats.put("pendingApprovals", eventRepository.findByStatus("Pending").size());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    @GetMapping("/backup")
    public ResponseEntity<byte[]> downloadBackup() {
        try {
            String json = backupService.exportAllData();
            byte[] data = json.getBytes();
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=cems_backup_" + System.currentTimeMillis() + ".json")
                    .header("Content-Type", "application/json")
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @org.springframework.web.bind.annotation.PostMapping("/restore")
    public ResponseEntity<String> restoreBackup(@org.springframework.web.bind.annotation.RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            backupService.importAllData(file);
            return ResponseEntity.ok("System restored successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Restore failed: " + e.getMessage());
        }
    }
}
