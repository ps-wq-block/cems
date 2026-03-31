package com.cems.controller;

import com.cems.model.ReminderSetting;
import com.cems.repository.ReminderSettingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderSettingController {

    private final ReminderSettingRepository repository;

    public ReminderSettingController(ReminderSettingRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ReminderSetting>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/event/{eventName}")
    public ResponseEntity<?> getByEvent(@PathVariable String eventName) {
        return repository.findByEventName(eventName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReminderSetting setting) {
        // Prevent duplicates per event
        if (repository.findByEventName(setting.getEventName()).isPresent()) {
            return ResponseEntity.badRequest().body("Reminder setting already exists for this event. Use PUT to update.");
        }
        return ResponseEntity.ok(repository.save(setting));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody ReminderSetting updated) {
        return repository.findById(id).map(existing -> {
            existing.setReminderHoursBefore(updated.getReminderHoursBefore());
            existing.setEnabled(updated.isEnabled());
            return ResponseEntity.ok(repository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
