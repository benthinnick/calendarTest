package com.example.calendar.repository;

import com.example.calendar.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByEventDateAndUsername(Date date, String username);

    List<Event> getAllByUsernameOrderByEventDateAsc(String username);
}
