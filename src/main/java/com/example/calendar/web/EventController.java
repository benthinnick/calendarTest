package com.example.calendar.web;

import com.example.calendar.domain.Event;
import com.example.calendar.repository.EventRepository;
import com.example.calendar.repository.UserRepository;
import com.example.calendar.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final EventService eventService;

    @Autowired
    private EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(value = "/date/{date}", method = RequestMethod.GET)
    public List<Event> getEventsByDateAndUser(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @AuthenticationPrincipal User user) {
        log.info("REST Request to get calendar events by date");

        return eventService.getEventsByDateAndUser(date, user.getUsername());
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Event> getEvents(Pageable pageable, @AuthenticationPrincipal User user) {
        log.info("REST Request to get calendar events by date");

        return eventService.getUserEvents(user.getUsername(), pageable);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Event> create(@RequestBody Event event, @AuthenticationPrincipal User user) {
        log.info("REST Request to create new calendar event");

        return new ResponseEntity<>(eventService.create(event, user.getUsername()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Event> update(@RequestBody Event event, @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(eventService.update(event, user.getUsername()), HttpStatus.OK);
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE)
    public void deleteEvent(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        log.info("REST Request to delete calendar event, id: {}", eventId);

        eventService.delete(eventId);
    }


}
