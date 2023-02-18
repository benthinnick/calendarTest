package com.example.calendar.service;

import com.example.calendar.domain.Calendar;
import com.example.calendar.domain.Event;
import com.example.calendar.domain.User;
import com.example.calendar.domain.enums.Visibility;
import com.example.calendar.exception.EntityNotFoundException;
import com.example.calendar.exception.InsufficentRightsException;
import com.example.calendar.exception.UserNotFoundException;
import com.example.calendar.repository.CalendarRepository;
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
public class CalendarService {

    private static Logger log = LoggerFactory.getLogger(CalendarService.class);

    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    CalendarService(CalendarRepository calendarRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.calendarRepository = calendarRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public Calendar retrieveCalendar(Long calendarId, String username, boolean share) {
        User user = getUser(username);

        Calendar calendar = getCalendar(calendarId);

        log.info("" + calendar.getEvents().size());

        if (share && calendar.getVisibility() == Visibility.PUBLIC) {
            return calendar;
        } else if (calendar.getUser().getId() == user.getId()) {
            return calendar;
        } else {
            throw new InsufficentRightsException();
        }
    }

    public Calendar create(Calendar calendar, String username) {
        User user = getUser(username);

        calendar.setUser(user);

        for (Event event : calendar.getEvents()) {
            event.setCalendar(calendar);
        }

        return calendarRepository.saveAndFlush(calendar);
    }

    public Calendar update(Calendar calendar, String username) {
        Long calendarId = calendar.getId();

        Optional<Calendar> oFound = calendarRepository.findById(calendarId);

        if (oFound.isEmpty()) {
            log.error("Calendar {} not found", calendarId);
            throw new EntityNotFoundException("Calendar not found, id:" + calendarId);
        }

        Calendar found = oFound.get();

        User user = getUser(username);

        if (found.getUser().getId() != user.getId()) {
            log.error("User {} doesn't have rights to edit calendar {}", user.getUsername(), calendarId);
            throw new InsufficentRightsException();
        }

        found.getEvents().clear();

        found.addEvents(calendar.getEvents());

        for (Event event : calendar.getEvents()) {
            event.setCalendar(calendar);
        }

        return calendarRepository.saveAndFlush(found);
    }

    public void deleteEvent(Long calendarId) {
        Calendar calendar = getCalendar(calendarId);

        calendarRepository.delete(calendar);
    }

    private User getUser(String username) {
        Optional<User> oUser = userRepository.findByUsernameOrEmail(username, username);

        if (oUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        return oUser.get();
    }

    private Calendar getCalendar(Long calendarId) {
        Optional<Calendar> optionalCalendar = calendarRepository.findById(calendarId);

        if (optionalCalendar.isEmpty()) {
            throw new EntityNotFoundException("Calendar not found, id:" + calendarId);
        }

        return optionalCalendar.get();
    }
}
