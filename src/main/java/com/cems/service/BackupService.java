package com.cems.service;

import com.cems.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BackupService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventPhotoRepository eventPhotoRepository;
    private final FacultyCoordinatorAssignmentRepository assignmentRepository;
    private final NotificationRepository notificationRepository;
    private final ReminderSettingRepository reminderRepository;
    private final StudentRegistrationRepository registrationRepository;
    private final ObjectMapper objectMapper;

    public BackupService(UserRepository userRepository,
                         EventRepository eventRepository,
                         EventPhotoRepository eventPhotoRepository,
                         FacultyCoordinatorAssignmentRepository assignmentRepository,
                         NotificationRepository notificationRepository,
                         ReminderSettingRepository reminderRepository,
                         StudentRegistrationRepository registrationRepository,
                         ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.eventPhotoRepository = eventPhotoRepository;
        this.assignmentRepository = assignmentRepository;
        this.notificationRepository = notificationRepository;
        this.reminderRepository = reminderRepository;
        this.registrationRepository = registrationRepository;
        this.objectMapper = objectMapper;
    }

    public String exportAllData() throws IOException {
        Map<String, Object> backup = new HashMap<>();
        backup.put("users", userRepository.findAll());
        backup.put("events", eventRepository.findAll());
        backup.put("event_photos", eventPhotoRepository.findAll());
        backup.put("assignments", assignmentRepository.findAll());
        backup.put("notifications", notificationRepository.findAll());
        backup.put("reminders", reminderRepository.findAll());
        backup.put("registrations", registrationRepository.findAll());

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(backup);
    }

    @SuppressWarnings("unchecked")
    public void importAllData(MultipartFile file) throws IOException {
        Map<String, Object> data = objectMapper.readValue(file.getInputStream(), Map.class);

        // Clear existing data (Restore in case data is lost)
        userRepository.deleteAll();
        eventRepository.deleteAll();
        eventPhotoRepository.deleteAll();
        assignmentRepository.deleteAll();
        notificationRepository.deleteAll();
        reminderRepository.deleteAll();
        registrationRepository.deleteAll();

        // Restore data
        if (data.containsKey("users")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("users");
            list.forEach(m -> userRepository.save(objectMapper.convertValue(m, com.cems.model.User.class)));
        }
        if (data.containsKey("events")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("events");
            list.forEach(m -> eventRepository.save(objectMapper.convertValue(m, com.cems.model.Event.class)));
        }
        if (data.containsKey("event_photos")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("event_photos");
            list.forEach(m -> eventPhotoRepository.save(objectMapper.convertValue(m, com.cems.model.EventPhoto.class)));
        }
        if (data.containsKey("assignments")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("assignments");
            list.forEach(m -> assignmentRepository.save(objectMapper.convertValue(m, com.cems.model.FacultyCoordinatorAssignment.class)));
        }
        if (data.containsKey("notifications")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("notifications");
            list.forEach(m -> notificationRepository.save(objectMapper.convertValue(m, com.cems.model.Notification.class)));
        }
        if (data.containsKey("reminders")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("reminders");
            list.forEach(m -> reminderRepository.save(objectMapper.convertValue(m, com.cems.model.ReminderSetting.class)));
        }
        if (data.containsKey("registrations")) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("registrations");
            list.forEach(m -> registrationRepository.save(objectMapper.convertValue(m, com.cems.model.StudentRegistration.class)));
        }
    }
}
