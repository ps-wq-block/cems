package com.cems.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reminder_settings")
public class ReminderSetting {

    @Id
    private String id;

    private String eventName;
    private int reminderHoursBefore; // e.g. 24 = 1 day before, 1 = 1 hour before
    private boolean enabled;

    public ReminderSetting() {
        this.reminderHoursBefore = 24;
        this.enabled = true;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public int getReminderHoursBefore() { return reminderHoursBefore; }
    public void setReminderHoursBefore(int reminderHoursBefore) { this.reminderHoursBefore = reminderHoursBefore; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
