package com.cems.service;

import com.cems.model.Event;
import com.cems.model.Notification;
import com.cems.model.ReminderSetting;
import com.cems.model.StudentRegistration;
import com.cems.repository.EventRepository;
import com.cems.repository.NotificationRepository;
import com.cems.repository.ReminderSettingRepository;
import com.cems.repository.StudentRegistrationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
public class NotificationScheduler {

    private final EventRepository eventRepository;
    private final StudentRegistrationRepository registrationRepository;
    private final NotificationRepository notificationRepository;
    private final ReminderSettingRepository reminderSettingRepository;

    public NotificationScheduler(EventRepository eventRepository,
            StudentRegistrationRepository registrationRepository,
            NotificationRepository notificationRepository,
            ReminderSettingRepository reminderSettingRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.notificationRepository = notificationRepository;
        this.reminderSettingRepository = reminderSettingRepository;
    }

    // Runs every hour to check for reminders based on configurable timing
    @Scheduled(cron = "0 0 * * * ?")
    public void checkAndSendReminders() {
        List<Event> allEvents = eventRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Event event : allEvents) {
            if (event.getEventDate() == null) continue;

            // Look up the reminder setting for this event
            Optional<ReminderSetting> settingOpt = reminderSettingRepository.findByEventName(event.getName());

            // Default: 24 hours before, enabled
            int hoursBefore = 24;
            boolean enabled = true;

            if (settingOpt.isPresent()) {
                ReminderSetting setting = settingOpt.get();
                enabled = setting.isEnabled();
                hoursBefore = setting.getReminderHoursBefore();
            }

            // Skip if reminders are cancelled/disabled for this event
            if (!enabled) continue;

            // Calculate the event datetime
            LocalDate eventDate = LocalDate.parse(event.getEventDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDateTime eventDateTime = eventDate.atStartOfDay();

            // If event has a time like "10:00", parse it
            if (event.getEventTime() != null && !event.getEventTime().isEmpty()) {
                try {
                    String timePart = event.getEventTime().split("-")[0].trim();
                    // Handle formats: "10:00", "10 AM", "10:00 AM"
                    timePart = timePart.toUpperCase();
                    int hour = 0, minute = 0;
                    if (timePart.contains(":")) {
                        String[] parts = timePart.replaceAll("[^0-9:]", "").split(":");
                        hour = Integer.parseInt(parts[0]);
                        minute = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                    } else {
                        hour = Integer.parseInt(timePart.replaceAll("[^0-9]", ""));
                    }
                    if (timePart.contains("PM") && hour < 12) hour += 12;
                    if (timePart.contains("AM") && hour == 12) hour = 0;
                    eventDateTime = eventDate.atTime(hour, minute);
                } catch (Exception e) {
                    // Fallback to start of day
                    eventDateTime = eventDate.atStartOfDay();
                }
            }

            // Calculate when the reminder should fire
            LocalDateTime reminderTime = eventDateTime.minusHours(hoursBefore);

            // Check if we're within the reminder window (current hour matches)
            long hoursUntilReminder = ChronoUnit.HOURS.between(now, reminderTime);
            if (hoursUntilReminder >= -1 && hoursUntilReminder <= 0) {
                // Time to send reminders for this event
                sendEventReminders(event, hoursBefore);
            }
        }
    }

    // Also keep the legacy day-before and event-day triggers as fallback
    @Scheduled(cron = "0 0 10 * * ?")
    public void scheduleDayBeforeReminders() {
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        sendDateBasedReminders(tomorrow, false);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduleEventDayReminders() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        sendDateBasedReminders(today, true);
    }

    private void sendEventReminders(Event event, int hoursBefore) {
        List<StudentRegistration> registrations = registrationRepository.findAll();
        String hoursLabel = hoursBefore >= 24 ? (hoursBefore / 24) + " day(s)" : hoursBefore + " hour(s)";
        String title = "Reminder: " + event.getName() + " in " + hoursLabel + "!";
        String message = "Don't forget to attend " + event.getName()
                       + " in " + hoursLabel
                       + (event.getEventTime() != null ? " at " + event.getEventTime() : "")
                       + (event.getVenue() != null ? " at " + event.getVenue() : "")
                       + ".";

        for (StudentRegistration reg : registrations) {
            if (event.getName().equals(reg.getEventName())) {
                boolean alreadySent = notificationRepository.findAll().stream()
                        .anyMatch(n -> title.equals(n.getTitle()) && reg.getEmail().equals(n.getReceiver()));

                if (!alreadySent) {
                    Notification notification = new Notification();
                    notification.setTitle(title);
                    notification.setMessage(message);
                    notification.setReceiver(reg.getEmail());
                    notification.setEventDate(event.getEventDate());
                    notification.setEventTime(event.getEventTime());
                    notification.setCreatedAt(LocalDateTime.now());
                    notificationRepository.save(notification);
                }
            }
        }
    }

    private void sendDateBasedReminders(String targetDate, boolean isToday) {
        List<Event> allEvents = eventRepository.findAll();
        for (Event event : allEvents) {
            if (targetDate.equals(event.getEventDate())) {
                // Check if reminders are disabled for this event
                Optional<ReminderSetting> settingOpt = reminderSettingRepository.findByEventName(event.getName());
                if (settingOpt.isPresent() && !settingOpt.get().isEnabled()) continue;

                List<StudentRegistration> registrations = registrationRepository.findAll();

                for (StudentRegistration reg : registrations) {
                    if (event.getName().equals(reg.getEventName())) {

                        String title = "Reminder: " + event.getName() + (isToday ? " is TODAY!" : " is tomorrow!");
                        String message = "Don't forget to attend " + event.getName()
                                       + (isToday ? " today" : " tomorrow")
                                       + (event.getEventTime() != null ? " at " + event.getEventTime() : "")
                                       + (event.getVenue() != null ? " at " + event.getVenue() : "")
                                       + ".";

                        boolean alreadySent = notificationRepository.findAll().stream()
                                .anyMatch(n -> title.equals(n.getTitle()) && reg.getEmail().equals(n.getReceiver()));

                        if (!alreadySent) {
                            Notification notification = new Notification();
                            notification.setTitle(title);
                            notification.setMessage(message);
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
