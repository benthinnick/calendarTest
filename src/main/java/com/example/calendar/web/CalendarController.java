package com.example.calendar.web;

import com.example.calendar.domain.Calendar;
import com.example.calendar.domain.Event;
import com.example.calendar.service.CalendarService;
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

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final Logger log = LoggerFactory.getLogger(CalendarController.class);
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }


    @RequestMapping(value = "/{calendarId}", method = RequestMethod.GET)
    public Calendar getEventsByDateAndUser(@PathVariable Long calendarId,
                                              @AuthenticationPrincipal User user,
                                              @RequestParam(required = false) boolean share) {
        log.info("REST Request to get calendar events by date");


        return calendarService.retrieveCalendar(calendarId, user.getUsername(), share);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Calendar> create(@RequestBody Calendar calendar,
                                           @AuthenticationPrincipal User user) {
        log.info("REST Request to create new calendar event");

        return new ResponseEntity<>(calendarService.create(calendar, user.getUsername()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Calendar> update(@RequestBody Calendar calendar,
                                           @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(calendarService.update(calendar, user.getUsername()), HttpStatus.OK);
    }

    @RequestMapping(value = "{calendarId}", method = RequestMethod.DELETE)
    public void deleteCalendar(@PathVariable Long calendarId,
                               @AuthenticationPrincipal User user) {
        log.info("REST Request to delete calendar event, id: {}", calendarId);

        calendarService.deleteEvent(calendarId);
    }
}
