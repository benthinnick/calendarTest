package com.example.calendar.domain;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.User;

import java.sql.Date;

@Entity
@Table(name = "calendar_event")
public class Event {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Date eventDate;

    @Column
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}