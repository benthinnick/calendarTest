package com.example.calendar.repository;

import com.example.calendar.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.sql.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByEventDate(Date date);

    List<Event> getAllByUserOrderByEventDateAsc(User user);
}
