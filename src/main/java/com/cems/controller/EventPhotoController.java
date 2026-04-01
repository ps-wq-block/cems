package com.cems.controller;

import com.cems.model.EventPhoto;
import com.cems.repository.EventPhotoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gallery")
@CrossOrigin(origins = "*")
public class EventPhotoController {

    private final EventPhotoRepository repository;

    public EventPhotoController(EventPhotoRepository repository) {
        this.repository = repository;
    }

    // Get all photos, optional filter by category
    @GetMapping("/photos")
    public ResponseEntity<List<EventPhoto>> getAllPhotos(@RequestParam(required = false) String category) {
        List<EventPhoto> photos = (category != null && !category.isEmpty())
                ? repository.findByCategory(category)
                : repository.findAll();
        return ResponseEntity.ok(photos);
    }

    // Upload a new photo (ADMIN or ORGANIZER only)
    @PostMapping("/upload")
    public ResponseEntity<EventPhoto> uploadPhoto(@RequestBody Map<String, String> payload) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(401).build();
        }
        String role = auth.getAuthorities().stream()
                .findFirst().map(Object::toString).orElse("");
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("ORGANIZER")) {
            return ResponseEntity.status(403).build();
        }
        String category = payload.get("category");
        String photoData = payload.get("photoData");
        EventPhoto photo = new EventPhoto();
        photo.setCategory(category);
        photo.setPhotoData(photoData);
        EventPhoto saved = repository.save(photo);
        return ResponseEntity.ok(saved);
    }

    // Delete a photo by id (ADMIN or ORGANIZER only)
    @DeleteMapping("/photos/{id}")
    public ResponseEntity<Void> deletePhoto(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(401).build();
        }
        String role = auth.getAuthorities().stream()
                .findFirst().map(Object::toString).orElse("");
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("ORGANIZER")) {
            return ResponseEntity.status(403).build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
