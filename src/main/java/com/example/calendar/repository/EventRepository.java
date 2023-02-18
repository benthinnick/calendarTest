package com.example.calendar.repository;

import com.example.calendar.domain.Calendar;
import com.example.calendar.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>, JpaRepository<Event, Long> {
    List<Event> findAllByEventDateAndCalendar(LocalDate date, Calendar calendar);

    Page<Event> getAllByCalendarOrderByEventDateAsc(Calendar user, Pageable pageable);
}
