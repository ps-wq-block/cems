package com.cems.service;

import com.cems.model.StudentRegistration;
import com.cems.repository.StudentRegistrationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentRegistrationService {

    private final StudentRegistrationRepository repository;
    private final com.cems.repository.EventRepository eventRepository;
    private final com.cems.repository.NotificationRepository notificationRepository;

    public StudentRegistrationService(StudentRegistrationRepository repository, com.cems.repository.EventRepository eventRepository, com.cems.repository.NotificationRepository notificationRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<StudentRegistration> saveAll(List<StudentRegistration> registrations) {
        return repository.saveAll(registrations);
    }

    public StudentRegistration registerStudent(StudentRegistration registration) {
        // Prevent duplicate registration
        if (repository.findByEmailAndEventName(registration.getEmail(), registration.getEventName()).isPresent()) {
            throw new IllegalArgumentException("You are already registered for this event.");
        }

        // Check if event has already started
        com.cems.model.Event event = eventRepository.findAll().stream()
                .filter(e -> e.getName().equals(registration.getEventName()))
                .findFirst()
                .orElse(null);

        if (event != null && isEventStarted(event)) {
            throw new IllegalArgumentException("Registration failed: This event has already started.");
        }

        if (registration.getRegistrationDate() == null) {
            registration.setRegistrationDate(java.time.LocalDate.now().toString());
        }
        
        StudentRegistration savedRegistration = repository.save(registration);

        // Send Notification
        com.cems.model.Notification successNotification = new com.cems.model.Notification();
        successNotification.setReceiver(savedRegistration.getEmail());
        successNotification.setTitle("Registration Successful \uD83C\uDF89");
        successNotification.setMessage("You have successfully registered for the event: " + savedRegistration.getEventName());
        successNotification.setEventDate(savedRegistration.getEventDate() != null ? savedRegistration.getEventDate() : "TBD");
        successNotification.setEventTime("");
        notificationRepository.save(successNotification);

        return savedRegistration;
    }

    private boolean isEventStarted(com.cems.model.Event event) {
        if (event.getEventDate() == null || event.getEventDate().isEmpty()) return false;
        
        try {
            java.time.LocalDate date = java.time.LocalDate.parse(event.getEventDate());
            java.time.LocalTime time = java.time.LocalTime.MIDNIGHT;

            // Attempt to parse start time from eventTime string (e.g., "10 AM - 12 PM")
            String timeStr = event.getEventTime();
            if (timeStr != null && !timeStr.isEmpty()) {
                // Split by '-' and take the first part
                String startPart = timeStr.split("-")[0].trim().toUpperCase();
                
                // Common formats: "10 AM", "10:00 AM", "14:00", "2 PM"
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d{1,2})(:(\\d{2}))?\\s*(AM|PM)?");
                java.util.regex.Matcher matcher = pattern.matcher(startPart);
                
                if (matcher.find()) {
                    int hour = Integer.parseInt(matcher.group(1));
                    int minute = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) : 0;
                    String ampm = matcher.group(4);

                    if ("PM".equals(ampm) && hour < 12) hour += 12;
                    if ("AM".equals(ampm) && hour == 12) hour = 0;
                    
                    time = java.time.LocalTime.of(hour, minute);
                }
            }

            java.time.LocalDateTime eventStart = java.time.LocalDateTime.of(date, time);
            return java.time.LocalDateTime.now().isAfter(eventStart);
        } catch (Exception e) {
            // If parsing fails, fall back to comparing just the date
            try {
                return java.time.LocalDate.now().isAfter(java.time.LocalDate.parse(event.getEventDate()));
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public List<StudentRegistration> getAllRegistrations() {
        return repository.findAll();
    }

    public List<StudentRegistration> getRegistrationsByEvent(String eventName) {
        return repository.findByEventName(eventName);
    }

    public List<StudentRegistration> getRegistrationsByEmail(String email) {
        return repository.findByEmail(email);
    }

    public void deleteRegistration(String id) {
        repository.deleteById(id);
    }
}
