package com.example.calendar.service;

import com.example.calendar.domain.Event;
import com.example.calendar.domain.User;
import com.example.calendar.exception.EventNotFoundException;
import com.example.calendar.exception.InsufficentRightsException;
import com.example.calendar.exception.UserNotFoundException;
import com.example.calendar.repository.EventRepository;
import com.example.calendar.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private static Logger log = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Event> getEventsByDateAndUser(LocalDate date, String username) {
        User user = getUser(username);

        return eventRepository.findAllByEventDateAndUser(date, user.getId());
    }

    public Page<Event> getUserEvents(String username, Pageable pageable) {
        User user = getUser(username);

        return eventRepository.getAllByUserOrderByEventDateAsc(user.getId(), pageable);
    }

    public Event create(Event event, String username) {
        User user = getUser(username);

        event.setUser(user.getId());

        return eventRepository.saveAndFlush(event);
    }

    public Event update(Event event, String username) {
        Long eventId = event.getId();

        Optional<Event> oFound = eventRepository.findById(eventId);

        if (oFound.isEmpty()) {
            log.error("Event {} not found", eventId);
            throw new EventNotFoundException();
        }

        Event found = oFound.get();

        User user = getUser(username);

        if (found.getUser() != user.getId()) {
            log.error("User {} doesn't have rights to edit event {}", user.getUsername(), eventId);

            throw new InsufficentRightsException();
        }

        found.setEventDate(event.getEventDate());
        found.setName(event.getName());
        found.setDescription(event.getDescription());

        return eventRepository.saveAndFlush(found);
    }

    public void delete(Long eventId) {
        eventRepository.deleteById(eventId);
    }

    private User getUser(String username) {
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);

        if (oUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        return oUser.get();
    }
}
