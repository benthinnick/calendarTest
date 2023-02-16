package com.example.calendar.web;

import com.example.calendar.domain.Event;
import com.example.calendar.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    @Autowired
    private EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @RequestMapping(value = "/{dateString}", method = RequestMethod.GET)
    public List<Event> getEventsByDateAndUser(@PathVariable("dateString") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date, @AuthenticationPrincipal User user) {
        return eventRepository.findAllByEventDateAndUsername(date, user.getUsername());
    }


}
