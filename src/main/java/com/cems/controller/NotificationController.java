package com.cems.controller;

import com.cems.model.Notification;
import com.cems.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody Notification notification) {
        Notification saved = notificationRepository.save(notification);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationRepository.findAll());
    }

    @PutMapping("/{id}/seen")
    public ResponseEntity<?> markAsSeen(@PathVariable String id) {
        return notificationRepository.findById(id).map(n -> {
            n.setSeen(true);
            notificationRepository.save(n);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable String id, @RequestBody Notification updated) {
        return notificationRepository.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setMessage(updated.getMessage());
            existing.setReceiver(updated.getReceiver());
            existing.setEventDate(updated.getEventDate());
            existing.setEventTime(updated.getEventTime());
            notificationRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable String id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
