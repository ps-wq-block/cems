package com.cems.service;

import com.cems.model.Event;
import com.cems.model.Notification;
import com.cems.model.StudentRegistration;
import com.cems.repository.EventRepository;
import com.cems.repository.NotificationRepository;
import com.cems.repository.StudentRegistrationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class NotificationScheduler {

    private final EventRepository eventRepository;
    private final StudentRegistrationRepository registrationRepository;
    private final NotificationRepository notificationRepository;

    public NotificationScheduler(EventRepository eventRepository,
                                 StudentRegistrationRepository registrationRepository,
                                 NotificationRepository notificationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.notificationRepository = notificationRepository;
    }

    // Runs every day at 1:00 AM server time
    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduleEventReminders() {
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        List<Event> allEvents = eventRepository.findAll();
        for (Event event : allEvents) {
            if (tomorrow.equals(event.getEventDate())) {
                List<StudentRegistration> registrations = registrationRepository.findAll();
                
                for (StudentRegistration reg : registrations) {
                    if (event.getName().equals(reg.getEventName())) {
                        
                        // Check if reminder already sent to prevent duplicates
                        String expectedTitle = "Reminder: " + event.getName() + " is tomorrow!";
                        boolean alreadySent = notificationRepository.findAll().stream()
                                .anyMatch(n -> expectedTitle.equals(n.getTitle()) && reg.getEmail().equals(n.getReceiver()));
                        
                        if (!alreadySent) {
                            Notification notification = new Notification();
                            notification.setTitle(expectedTitle);
                            notification.setMessage("Don't forget to attend " + event.getName() + " tomorrow at " + event.getEventTime() + " at " + event.getVenue() + ".");
                            notification.setReceiver(reg.getEmail());
                            notification.setEventDate(event.getEventDate());
                            notification.setEventTime(event.getEventTime());
                            notification.setCreatedAt(LocalDateTime.now());
                            
                            notificationRepository.save(notification);
                        }
                    }
                }
            }
        }
    }
}
