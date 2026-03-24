package com.cems.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "events")
public class Event {

    @Id
    private String id;

    private String name;
    private String category;
    private String organizerEmail;

    // Using String to ensure clean JSON serialization with Jackson & MongoDB
    private String eventDate;
    private String eventTime;

    private String venue;
    private String description;

    private Integer maxParticipants;
    private String registrationDeadline;

    private String rules;

    private String status = "Pending"; // Pending, Approved, Rejected
    private String adminComments;
    
    private boolean registrationOpen = true;

    // Constructors
    public Event() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrganizerEmail() {
        return organizerEmail;
    }

    public void setOrganizerEmail(String organizerEmail) {
        this.organizerEmail = organizerEmail;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public String getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(String registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminComments() {
        return adminComments;
    }

    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }

    public boolean isRegistrationOpen() {
        return registrationOpen;
    }

    public void setRegistrationOpen(boolean registrationOpen) {
        this.registrationOpen = registrationOpen;
    }
}
