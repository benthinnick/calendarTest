package com.example.calendar.domain;

import com.example.calendar.domain.enums.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "calendar", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Column
    private Collection<Event> events = new ArrayList<>();

    //For this application purpose all will be public
    private Visibility visibility = Visibility.PUBLIC;

    //private LocalDate startDate;
    //private LocalDate endDate;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<Event> getEvents() {
        return events;
    }

    public void addEvents(Collection<Event> events) {
        this.events.addAll(events);
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
