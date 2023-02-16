package com.example.calendar.web;

import com.example.calendar.domain.Event;
import com.example.calendar.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final Logger log = LoggerFactory.getLogger(EventController.class);
    private final EventRepository eventRepository;

    @Autowired
    private EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @RequestMapping(value = "/date/{date}", method = RequestMethod.GET)
    public List<Event> getEventsByDateAndUser(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @AuthenticationPrincipal User user) {
        log.info("REST Request to get calendar events by date");

        return eventRepository.findAllByEventDateAndUsername(date, user.getUsername());
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Event> getEvents(Pageable pageable, @AuthenticationPrincipal User user) {
        log.info("REST Request to get calendar events by date");

        return eventRepository.getAllByUsernameOrderByEventDateAsc(user.getUsername(), pageable);
    }


    @RequestMapping(method = RequestMethod.POST)
    public Event create(@RequestBody Event event, @AuthenticationPrincipal User user) {
        log.info("REST Request to create new calendar event");

        event.setUsername(user.getUsername());

        return eventRepository.saveAndFlush(event);
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.PUT)
    public Event update(@PathVariable Long eventId,
                        @RequestBody Event event,
                        @AuthenticationPrincipal User user) {
        log.info("REST Request to update calendar event, id: {}", eventId);
        Optional<Event> oFound = eventRepository.findById(eventId);

        if (oFound.isEmpty()) {
            log.error("Event {} not found", eventId);
            return null;
        }

        Event found = oFound.get();

        if (found.getUsername() != user.getUsername()) {
            log.error("User {} doesn't have rights to edit event {}", user.getUsername(), eventId);

            return null;
        }

        found.setEventDate(event.getEventDate());
        found.setName(event.getName());
        found.setDescription(event.getDescription());

        return eventRepository.saveAndFlush(found);
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE)
    public void deleteEvent(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        log.info("REST Request to delete calendar event, id: {}", eventId);

        eventRepository.deleteById(eventId);
    }


}
